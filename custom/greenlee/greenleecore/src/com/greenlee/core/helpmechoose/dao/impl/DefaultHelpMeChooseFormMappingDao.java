/**
 *
 */
package com.greenlee.core.helpmechoose.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import org.apache.commons.lang.StringUtils;

import com.greenlee.core.helpmechoose.dao.HelpMeChooseFormMappingDao;
import com.greenlee.core.model.HelpMeChooseAnswerModel;
import com.greenlee.core.model.HelpMeChooseQuestionModel;


/**
 * @author HelpMeChoose.santhanam
 * 
 */
public class DefaultHelpMeChooseFormMappingDao extends AbstractItemDao implements HelpMeChooseFormMappingDao
{

	private static final String LEVEL1_QUESTION = "select  {question.pk} from {HelpMeChooseQuestion as question} where {question.levelOne} = 1 order by {question.code}";

	private static final String GET_QUESTION = "select  {question.pk} from {HelpMeChooseQuestion as question} where {question.code} = ?questionCode  order by {question.code}";

	private static final String GET_ANSWER = "select  {answer.pk} from {HelpMeChooseAnswer as answer} where {answer.code} = ?answerCode  order by {answer.code}";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.core.helpmechoose.dao.HelpMeChooseFormMappingDao#getQuestion(java.lang.String)
	 */
	@Override
	public HelpMeChooseQuestionModel getQuestion(final String questionCode)
	{
		FlexibleSearchQuery query = null;
		if (StringUtils.isEmpty(questionCode))
		{
			query = new FlexibleSearchQuery(LEVEL1_QUESTION);
		}
		else
		{
			query = new FlexibleSearchQuery(GET_QUESTION);
			query.addQueryParameter("questionCode", questionCode);
		}
		return getFlexibleSearchService().searchUnique(query);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.core.helpmechoose.dao.HelpMeChooseFormMappingDao#getSuggestedAnswer()
	 */
	@Override
	public HelpMeChooseAnswerModel getAnswer(final String answerCode)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ANSWER);
		query.addQueryParameter("answerCode", answerCode);
		return getFlexibleSearchService().searchUnique(query);
	}

}
