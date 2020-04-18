/**
 *
 */
package com.greenlee.core.helpmechoose.services.impl;


import de.hybris.platform.servicelayer.dto.converter.Converter;

import com.greenlee.core.helpmechoose.dao.HelpMeChooseFormMappingDao;
import com.greenlee.core.helpmechoose.services.HelpMeChooseService;
import com.greenlee.core.model.HelpMeChooseAnswerModel;
import com.greenlee.core.model.HelpMeChooseQuestionModel;
import com.greenlee.helpmechoose.data.HelpMeChooseAnswerData;
import com.greenlee.helpmechoose.data.HelpMeChooseQuestionData;


/**
 * @author HelpMeChoose.santhanam
 * 
 */
public class DefaultHelpMeChooseService implements HelpMeChooseService
{

	private HelpMeChooseFormMappingDao helpMeChooseFormMappingDao;

	private Converter<HelpMeChooseQuestionModel, HelpMeChooseQuestionData> questionConverter;
	private Converter<HelpMeChooseAnswerModel, HelpMeChooseAnswerData> answerConverter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.core.helpmechoose.services.HelpMeChooseService#getQuestion(java.lang.String)
	 */
	@Override
	public HelpMeChooseQuestionData getQuestion(final String answerCode)
	{
		final HelpMeChooseQuestionModel questionModel = helpMeChooseFormMappingDao.getQuestion(answerCode);
		return questionConverter.convert(questionModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.core.helpmechoose.services.HelpMeChooseService#getSuggestedAnswer()
	 */
	@Override
	public HelpMeChooseAnswerData getAnswer(final String answerCode)
	{
		final HelpMeChooseAnswerModel answerModel = helpMeChooseFormMappingDao.getAnswer(answerCode);
		return answerConverter.convert(answerModel);
	}


	/**
	 * @return the helpMeChooseFormMappingDao
	 */
	public HelpMeChooseFormMappingDao getHelpMeChooseFormMappingDao()
	{
		return helpMeChooseFormMappingDao;
	}

	/**
	 * @param helpMeChooseFormMappingDao
	 *           the helpMeChooseFormMappingDao to set
	 */
	public void setHelpMeChooseFormMappingDao(final HelpMeChooseFormMappingDao helpMeChooseFormMappingDao)
	{
		this.helpMeChooseFormMappingDao = helpMeChooseFormMappingDao;
	}

	/**
	 * @return the questionConverter
	 */
	public Converter<HelpMeChooseQuestionModel, HelpMeChooseQuestionData> getQuestionConverter()
	{
		return questionConverter;
	}

	/**
	 * @param questionConverter
	 *           the questionConverter to set
	 */
	public void setQuestionConverter(final Converter<HelpMeChooseQuestionModel, HelpMeChooseQuestionData> questionConverter)
	{
		this.questionConverter = questionConverter;
	}

	/**
	 * @return the answerConverter
	 */
	public Converter<HelpMeChooseAnswerModel, HelpMeChooseAnswerData> getAnswerConverter()
	{
		return answerConverter;
	}

	/**
	 * @param answerConverter
	 *           the answerConverter to set
	 */
	public void setAnswerConverter(final Converter<HelpMeChooseAnswerModel, HelpMeChooseAnswerData> answerConverter)
	{
		this.answerConverter = answerConverter;
	}

}
