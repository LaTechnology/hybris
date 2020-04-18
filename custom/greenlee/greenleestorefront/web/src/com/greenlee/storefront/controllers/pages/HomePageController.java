/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.greenlee.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greenlee.core.helpmechoose.services.HelpMeChooseService;
import com.greenlee.helpmechoose.data.HelpMeChooseAnswerData;
import com.greenlee.helpmechoose.data.HelpMeChooseQuestionData;


/**
 * Controller for home page
 */
@Controller
@Scope("tenant")
@RequestMapping("/")
public class HomePageController extends AbstractPageController
{

	private static final Logger LOG = Logger.getLogger(HomePageController.class);
	@Resource(name = "defaultHelpMeChooseService")
	private HelpMeChooseService helpMeChooseService;

	@RequestMapping(method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String home(@RequestParam(value = "logout", defaultValue = "false") final boolean logout, final Model model,
			@RequestParam(value = "answerCode", required = false) final String answerCode,
			@RequestParam(value = "questionCode", required = false) final String questionCode,
			@RequestParam(value = "depth", required = false) final String depth,
			@RequestParam(value = "currentPosInDepth", required = false) final String currentPosInDepth,
			@RequestParam(value = "previousQuestionCode", required = false) final String previousQuestionCode,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (logout)
		{
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, "account.confirmation.signout.title");
			return REDIRECT_PREFIX + ROOT;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(null));

		if (StringUtils.isNotBlank(answerCode))
		{
			final HelpMeChooseAnswerData answer = helpMeChooseService.getAnswer(answerCode);
			if (answer.getNextQuestion() == null)
			{
				LOG.debug("User is redirected to a target page based on his responses.");
				return REDIRECT_PREFIX + answer.getTargetURL();
			}
			LOG.debug("User is redirected to next question.");
		}
		make(model, answerCode, questionCode, depth, currentPosInDepth, previousQuestionCode);

		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));

		updatePageTitle(model, getContentPageForLabelOrId(null));

		return getViewForPage(model);
	}

	protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveHomePageTitle(cmsPage.getTitle()));
	}


	public String make(final Model model, final String answerCode, final String questionCode, final String depth,
			final String currentPosInDepth, final String previousQuestionCode) throws CMSItemNotFoundException
	{
		processQuestionnaire(model, answerCode, questionCode, depth, currentPosInDepth, previousQuestionCode);
		return getViewForPage(model);
	}

	public void processQuestionnaire(final Model model, final String answerCode, final String questionCode, final String depth,
			final String currentPosInDepth, final String previousQuestionCode) throws CMSItemNotFoundException
	{
		if (StringUtils.isNotBlank(answerCode))
		{
			final HelpMeChooseAnswerData answer = helpMeChooseService.getAnswer(answerCode);
			/*
			 * if (answer.getNextQuestion() == null) {
			 * LOG.debug("User is redirected to a target page based on his responses."); return REDIRECT_PREFIX +
			 * answer.getTargetURL(); }
			 */
			LOG.debug("User is redirected to next question.");
			final Integer currentPos = Integer.valueOf(Integer.parseInt(currentPosInDepth) + 1);
			prepareModel(model, answer.getNextQuestion(), depth, questionCode, currentPos);
		}
		else if (StringUtils.isNotBlank(previousQuestionCode))
		{
			LOG.debug("User is redirected to previous question.");
			final HelpMeChooseQuestionData question = helpMeChooseService.getQuestion(previousQuestionCode);
			final Integer currentPos = Integer.valueOf(Integer.parseInt(currentPosInDepth) - 1);
			prepareModel(model, question, depth, previousQuestionCode, currentPos);
		}
		else
		{
			LOG.debug("Root level(Level 1) question to be shown");
			final HelpMeChooseQuestionData question = helpMeChooseService.getQuestion(null);
			final String depthCalc = String.valueOf(calculateDepth(0, question, new ArrayList<Integer>()));
			prepareModel(model, question, depthCalc, question.getCode(), Integer.valueOf(1));
		}
		//		return ControllerConstants.Views.Pages.HelpMeChoose.maketherightchoice;
	}

	private void prepareModel(final Model model, final HelpMeChooseQuestionData question, final String depth,
			final String previousQuestionCode, final Integer currentPosInDepth) throws CMSItemNotFoundException
	{
		storeContentPageTitleInModel(model, "HelpMeChoose");

		model.addAttribute("depth", depth);
		model.addAttribute("previousQuestionCode", previousQuestionCode);
		model.addAttribute("currentPosInDepth", currentPosInDepth);

		model.addAttribute("question", question);
		if (!question.isLevelOne())
		{
			model.addAttribute("hasPrevious", "hasPrevious");
		}
		if (CollectionUtils.isNotEmpty(question.getAnswers()))
		{
			for (final HelpMeChooseAnswerData answer : question.getAnswers())
			{
				if (answer.getNextQuestion() != null)
				{
					model.addAttribute("hasNext", "hasNext");
					break;
				}
			}
		}
	}

	/*
	 * This method will calculate the level for the lower most question
	 */
	private int calculateDepth(final int depth, final HelpMeChooseQuestionData question, final List<Integer> depthPerAnswer)
	{
		int calculatedDepth = depth;
		if (CollectionUtils.isNotEmpty(question.getAnswers()))
		{
			calculatedDepth = depth + 1;
			for (final HelpMeChooseAnswerData answer : question.getAnswers())
			{
				if (answer.getNextQuestion() == null)
				{
					return calculatedDepth;
				}
				calculatedDepth = calculateDepth(calculatedDepth, answer.getNextQuestion(), depthPerAnswer);
				depthPerAnswer.add(Integer.valueOf(calculatedDepth));
			}
		}

		final Integer[] depthPerAnswerArr = depthPerAnswer.toArray(new Integer[depthPerAnswer.size()]);
		if (depthPerAnswerArr.length > 0)
		{
			calculatedDepth = NumberUtils.max(ArrayUtils.toPrimitive(depthPerAnswerArr));
		}
		return calculatedDepth;
	}
}
