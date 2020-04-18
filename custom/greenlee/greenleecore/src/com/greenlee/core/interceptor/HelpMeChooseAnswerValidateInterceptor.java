/**
 *
 */
package com.greenlee.core.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.apache.commons.lang.StringUtils;

import com.greenlee.core.model.HelpMeChooseAnswerModel;



/**
 * @author raja.santhanam
 *
 */
public class HelpMeChooseAnswerValidateInterceptor implements ValidateInterceptor<HelpMeChooseAnswerModel>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.interceptor.ValidateInterceptor#onValidate(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onValidate(final HelpMeChooseAnswerModel model, final InterceptorContext ctx) throws InterceptorException
	{
		if (StringUtils.isBlank(model.getTargetURL()) && model.getNextQuestion() == null)
		{
			throw new InterceptorException("Both targetURL and nextQuestion cannot be null for a HelpMeChooseAnswer.");
		}
	}

}
