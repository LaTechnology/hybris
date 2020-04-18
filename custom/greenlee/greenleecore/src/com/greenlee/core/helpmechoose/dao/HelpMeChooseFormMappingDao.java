package com.greenlee.core.helpmechoose.dao;

import de.hybris.platform.servicelayer.internal.dao.Dao;

import com.greenlee.core.model.HelpMeChooseAnswerModel;
import com.greenlee.core.model.HelpMeChooseQuestionModel;


/**
 * @author HelpMeChoose.santhanam
 * 
 */
public interface HelpMeChooseFormMappingDao extends Dao
{
	public HelpMeChooseQuestionModel getQuestion(String questionCode);

	public HelpMeChooseAnswerModel getAnswer(String answerCode);
}
