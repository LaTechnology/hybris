/**
 *
 */
package com.greenlee.core.helpmechoose.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.greenlee.core.model.HelpMeChooseAnswerModel;
import com.greenlee.core.model.HelpMeChooseQuestionModel;
import com.greenlee.helpmechoose.data.HelpMeChooseAnswerData;
import com.greenlee.helpmechoose.data.HelpMeChooseQuestionData;


/**
 * @author HelpMeChoose.santhanam
 *
 */
public class HelpMeChooseQuestionPopulator implements Populator<HelpMeChooseQuestionModel, HelpMeChooseQuestionData>
{

	private Converter<HelpMeChooseAnswerModel, HelpMeChooseAnswerData> answerConverter;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final HelpMeChooseQuestionModel source, final HelpMeChooseQuestionData target) throws ConversionException
	{
		target.setLevelOne(source.isLevelOne());
		target.setCode(source.getCode());
		target.setQuestion(source.getQuestion());

		//GRE-1492 fix
		final List<HelpMeChooseAnswerModel> sortedAnswers = new ArrayList<HelpMeChooseAnswerModel>();
		sortedAnswers.addAll(source.getAnswers());
		Collections.sort(sortedAnswers, new AnswerComparable());
		//GRE-1492 fix

		target.setAnswers(Converters.convertAll(sortedAnswers, answerConverter));
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

	private class AnswerComparable implements Comparator<HelpMeChooseAnswerModel>
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final HelpMeChooseAnswerModel o1, final HelpMeChooseAnswerModel o2)
		{
			return o1.getCode().compareTo(o2.getCode());
		}
	}

}
