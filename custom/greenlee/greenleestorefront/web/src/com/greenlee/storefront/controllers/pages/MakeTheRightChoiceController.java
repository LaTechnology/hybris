/**
 *
 */
package com.greenlee.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

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

import com.greenlee.core.helpmechoose.services.HelpMeChooseService;
import com.greenlee.helpmechoose.data.HelpMeChooseAnswerData;
import com.greenlee.helpmechoose.data.HelpMeChooseQuestionData;
import com.greenlee.storefront.controllers.ControllerConstants;


/**
 * @author HelpMeChoose
 *
 *         Selvaraja.s
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/maketherightchoice")
public class MakeTheRightChoiceController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(MakeTheRightChoiceController.class);

	@Resource(name = "defaultHelpMeChooseService")
	private HelpMeChooseService helpMeChooseService;

	/**
	 * Handler method to process request and direct user to questions or target page based on user responses.
	 *
	 * @param model
	 * @param answerCode
	 *           - code of the selected answer.
	 * @param questionCode
	 *           - code of the current question displayed to the user.
	 * @param depth
	 *           - calculate the level for the lower most question
	 * @param currentPosInDepth
	 *           - required to highlight the progress bar for the current question
	 * @param previousQuestionCode
	 *           - question code to populate question when back button is pressed.
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String processQuestionnaire(final Model model,
			@RequestParam(value = "answerCode", required = false) final String answerCode,
			@RequestParam(value = "questionCode", required = false) final String questionCode,
			@RequestParam(value = "depth", required = false) final String depth,
			@RequestParam(value = "currentPosInDepth", required = false) final String currentPosInDepth,
			@RequestParam(value = "previousQuestionCode", required = false) final String previousQuestionCode)
			throws CMSItemNotFoundException
	{
		final List<HelpMeChooseQuestionData> questions = new ArrayList<HelpMeChooseQuestionData>();
		final HelpMeChooseQuestionData chooseQuestionData1 = new HelpMeChooseQuestionData();
		final HelpMeChooseQuestionData chooseQuestionData2 = new HelpMeChooseQuestionData();
		final HelpMeChooseQuestionData chooseQuestionData3 = new HelpMeChooseQuestionData();

		chooseQuestionData1.setCode("L1Q1");
		chooseQuestionData2.setCode("L2Q1");
		chooseQuestionData3.setCode("L2Q2");
		questions.add(chooseQuestionData1);
		questions.add(chooseQuestionData2);
		questions.add(chooseQuestionData3);

		/*
		 * for (final HelpMeChooseQuestionData helpMeChooseQuestionData : questions) { final HelpMeChooseQuestionData
		 * level1Question = helpMeChooseService.getQuestion(helpMeChooseQuestionData.getCode()); if (level1Question !=
		 * null) { final String code = level1Question.getCode(); final List<HelpMeChooseAnswerData> questionWithAnswers =
		 * level1Question.getAnswers(); final String l1question = level1Question.getQuestion();
		 * System.out.println("Level 1" + code + " " + l1question + " ans :" + questionWithAnswers.size()); for (final
		 * HelpMeChooseAnswerData helpMeChooseAnswerData : questionWithAnswers) {
		 * System.out.println("helpMeChooseAnswerData code: " + helpMeChooseAnswerData.getCode() + "nxt q " +
		 * helpMeChooseAnswerData.getNextQuestion() + "  url :" + helpMeChooseAnswerData.getTargetURL() + " ans " +
		 * helpMeChooseAnswerData.getAnswer());
		 * 
		 * final HelpMeChooseQuestionData level2Question = helpMeChooseAnswerData.getNextQuestion(); if
		 * (level2Question.getQuestion() != null && StringUtils.isNotEmpty(level2Question.getQuestion()) &&
		 * StringUtils.isNotBlank(level2Question.getQuestion())) {
		 * System.out.println("level2Question >> helpMeChooseAnswerData code: " + level2Question.getCode() + "nxt q " +
		 * level2Question.getQuestion() + "  url :" + level2Question.getAnswers().size()); if
		 * (helpMeChooseQuestionData.getCode() != null) { final HelpMeChooseQuestionData questionData =
		 * helpMeChooseService.getQuestion(helpMeChooseQuestionData .getCode()); if (questionData != null) { final String
		 * codes = questionData.getCode(); final List<HelpMeChooseAnswerData> questionWithAnswerss =
		 * questionData.getAnswers(); final String l1questions = level1Question.getQuestion();
		 * System.out.println("Level 1" + codes + " " + l1questions + " ans :" + questionWithAnswerss.size()); } } } } } }
		 */






		if (StringUtils.isNotBlank(answerCode))
		{
			final HelpMeChooseAnswerData answer = helpMeChooseService.getAnswer(answerCode);
			if (answer.getNextQuestion() == null)
			{
				LOG.debug("User is redirected to a target page based on his responses.");
				return REDIRECT_PREFIX + answer.getTargetURL();
			}
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
		return ControllerConstants.Views.Pages.HelpMeChoose.maketherightchoice;
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
