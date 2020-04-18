/**
 *
 */
package com.greenlee.storefront.forms.validation;

import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ReviewValidator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.greenlee.storefront.forms.GreenleeReviewForm;


/**
 * @author tarun.ranka
 *
 */

@Component("greenleeReviewValidator")
public class GreenleeReviewValidator extends ReviewValidator
{
	@Override
	public void validate(final Object object, final Errors errors)
	{
		final ReviewForm reviewForm = (ReviewForm) object;
		final String headLine = reviewForm.getHeadline();
		final String comment = reviewForm.getComment();
		final Double rating = reviewForm.getRating();

		if (StringUtils.isEmpty(headLine) || StringUtils.length(headLine) > 255)
		{
			errors.rejectValue("headline", "review.headline.invalid");
		}

		if (StringUtils.isEmpty(comment) || StringUtils.length(comment) > 4000)
		{
			errors.rejectValue("comment", "review.comment.invalid");
		}

		if (rating == null || rating.doubleValue() < 0.5 || rating.doubleValue() > 5)
		{
			errors.rejectValue("rating", "review.rating.invalid");
		}

		if (object instanceof GreenleeReviewForm)
		{
			final GreenleeReviewForm greenleeReviewForm = (GreenleeReviewForm) object;

			if (!greenleeReviewForm.isAgree())
			{
				errors.rejectValue("agree", "review.error.terms.not.accepted");
			}
		}
	}
}
