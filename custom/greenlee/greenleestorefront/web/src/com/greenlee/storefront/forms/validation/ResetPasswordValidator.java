package com.greenlee.storefront.forms.validation;


import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.greenlee.storefront.forms.UpdatePwdForm;


/**
 * Validator for password forms.
 *
 * @author xiaochen bian
 *
 */
@Component("resetPasswordValidator")
public class ResetPasswordValidator implements Validator
{
	private static final String PASSWORD_MIN_LENGTH = "greenleestorefront.storefront.password.minimumLength";

	private final int passwordMinLength = Integer.parseInt(Config.getParameter(PASSWORD_MIN_LENGTH));

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return UpdatePwdForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final UpdatePwdForm passwordForm = (UpdatePwdForm) object;
		final String newPasswd = passwordForm.getPwd();
		final String checkPasswd = passwordForm.getCheckPwd();
		final boolean agreeToPrivacyPolicy = passwordForm.isAgreeToPrivacyPolicy();
		//		final boolean requestForInfo = passwordForm.isRequestForInfo();
		if (StringUtils.isNotEmpty(newPasswd) && StringUtils.isNotEmpty(checkPasswd) && !StringUtils.equals(newPasswd, checkPasswd))
		{
			errors.rejectValue("checkPwd", "validation.checkPwd.equals");
		}
		else if (StringUtils.isEmpty(newPasswd))
		{
			errors.rejectValue("pwd", "updatePwd.pwd.empty");
		}
		else if (StringUtils.length(newPasswd) < passwordMinLength || StringUtils.length(newPasswd) > 255)
		{
			errors.rejectValue("pwd", "updatePwd.pwd.invalid");
		}
		else if (StringUtils.isEmpty(checkPasswd))
		{
			errors.rejectValue("checkPwd", "updatePwd.pwd.empty");
		}
		else if (StringUtils.length(checkPasswd) < passwordMinLength || StringUtils.length(checkPasswd) > 255)
		{
			errors.rejectValue("checkPwd", "updatePwd.pwd.invalid");
		}
		else if (passwordForm.isUpdatedByAdmin() && agreeToPrivacyPolicy == false)
		{
			//GRE-2153, agreeToPrivacyPolicy is mandataory and requestForInfo is optional
			errors.rejectValue("agreeToPrivacyPolicy", "register.policy.not.selected");
		}
	}

}
