/**
 *
 */
package com.greenlee.core.helpmechoose.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.lang.StringUtils;

import com.greenlee.core.model.HelpMeChooseAnswerModel;
import com.greenlee.core.model.HelpMeChooseQuestionModel;
import com.greenlee.helpmechoose.data.HelpMeChooseAnswerData;
import com.greenlee.helpmechoose.data.HelpMeChooseQuestionData;


/**
 * @author HelpMeChoose.santhanam
 * 
 */
public class HelpMeChooseAnswerPopulator implements Populator<HelpMeChooseAnswerModel, HelpMeChooseAnswerData>
{

	private Converter<HelpMeChooseQuestionModel, HelpMeChooseQuestionData> questionConverter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final HelpMeChooseAnswerModel source, final HelpMeChooseAnswerData target) throws ConversionException
	{
		target.setAnswer(source.getAnswer());
		target.setCode(source.getCode());

		if (StringUtils.isNotBlank(source.getTargetURL()))
		{
			target.setTargetURL(source.getTargetURL());
		}
		if (source.getNextQuestion() != null)
		{
			target.setNextQuestion(questionConverter.convert(source.getNextQuestion()));
		}
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

}
