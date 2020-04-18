/**
 *
 */
package com.greenlee.storefront.forms.validation;


import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.greenlee.storefront.forms.UpdateProfileForm;


/**
 *
 */
@Component("greenleeProfileValidator")
public class ProfileValidator extends de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ProfileValidator
{
	public static final Pattern EMAIL_REGEX = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b");

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ProfileValidator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(final Class<?> aClass)
	{
		return UpdateProfileForm.class.equals(aClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ProfileValidator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object object, final Errors errors)
	{
		super.validate(object, errors);

		/*
		 * Commented because the email address in profile page will not be editable and this validation does not hold good
		 * anymore.
		 * 
		 * final String email = profileForm.getEmail();
		 * 
		 * if (StringUtils.isEmpty(email)) { errors.rejectValue("email", "profile.email.invalid"); } else if
		 * (StringUtils.length(email) > 255 || !EMAIL_REGEX.matcher(email).matches()) { errors.rejectValue("email",
		 * "profile.email.invalid"); }
		 */}
}
