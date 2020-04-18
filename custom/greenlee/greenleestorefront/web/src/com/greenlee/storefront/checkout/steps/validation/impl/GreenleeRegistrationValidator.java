/**
 *
 */
package com.greenlee.storefront.checkout.steps.validation.impl;

import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.greenlee.core.constants.GeneratedGreenleeCoreConstants.Enumerations.UserTypes;
import com.greenlee.storefront.forms.B2BRegisterForm;


/**
 * @author aruna
 *
 */
@SuppressWarnings("deprecation")
@Component("greenLeeRegistrationValidator")
public class GreenleeRegistrationValidator implements Validator
{
	private static final Logger LOG = Logger.getLogger(GreenleeRegistrationValidator.class);

	final String regex_pattern_1 = "(\"(post\\s*(office)?)?\\s*box\\s*[#-]?\\s*(\\d+)?\")";
	final String regex_pattern_2 = "(\"[\\w\\s]*?p(ost)?\\s*[.-]?\\s*o?\\s*\\.?\\s*b?(ox)?\\.?\\s*(\\d+)?\")";
	final String regex_pattern_3 = "(\"(post\\s*(office)?)?\\s*box\\s*[#-]?\\s*(\\d+)?\"|\"[\\w\\s]*?p(ost)?\\s*[.-]?\\s*o?\\s*\\.?\\s*b?(ox)?\\.?\\s*(\\d+)?\")";

	final String regex_pattern_4 = "(\"[\\w\\s]*?p\\s*[.-]?\\s*o?\\s*\\.?\\s*b?(ox)?\\s*(\\d+)?\")";

	public static final Pattern EMAIL_REGEX = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b");
	public static final Pattern APLHABETIC_LETTERS = Pattern.compile("[a-zA-Z\\s']+");


	@Override
	public boolean supports(final Class<?> aClass)
	{
		return RegisterForm.class.equals(aClass);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void validate(final Object object, final Errors errors)
	{
		final B2BRegisterForm registerForm = (B2BRegisterForm) object;

		final String userType = registerForm.getUserType();
		if (StringUtils.isEmpty(userType))
		{
			errors.rejectValue("userType", "register.userType.invalid");
		}
		if (userType != null)
		{
			if (UserTypes.B2C.equalsIgnoreCase(userType))
			{
				validateFormForB2C(userType, registerForm, errors);
			}
			else if (UserTypes.B2E.equalsIgnoreCase(userType))
			{
				validateFormForB2BANDB2E(userType, registerForm, errors);
			}
			else if (UserTypes.B2B.equalsIgnoreCase(userType))
			{
				validateFormForB2B(registerForm, errors);
			}
		}
		validateFormForUserDetails(registerForm, errors);

		if (registerForm.getAgreeToPrivacyPolicy() == null || !registerForm.getAgreeToPrivacyPolicy().booleanValue())
		{
			errors.rejectValue("agreeToPrivacyPolicy", "register.policy.not.selected");
		}
	}

	/**
	 * @param registerForm
	 * @param errors
	 */
	private void validateFormForUserDetails(final B2BRegisterForm registerForm, final Errors errors)
	{
		final String firstName = registerForm.getFirstName();
		final String lastName = registerForm.getLastName();

		if (StringUtils.isBlank(firstName))
		{
			errors.rejectValue("firstName", "register.firstName.invalid");
		}
		else if (StringUtils.length(firstName) > 255)
		{
			errors.rejectValue("firstName", "register.firstName.invalid");
		}

		if (StringUtils.isBlank(lastName))
		{
			errors.rejectValue("lastName", "register.lastName.invalid");
		}
		else if (StringUtils.length(lastName) > 255)
		{
			errors.rejectValue("lastName", "register.lastName.invalid");
		}

		if (StringUtils.length(firstName) + StringUtils.length(lastName) > 255)
		{
			errors.rejectValue("lastName", "register.name.invalid");
			errors.rejectValue("firstName", "register.name.invalid");
		}


		validateUID(registerForm, errors);
		validatePassword(registerForm, errors);

	}

	/**
	 * @param registerForm
	 * @param errors
	 */
	private void validateUID(final B2BRegisterForm registerForm, final Errors errors)
	{
		final String email = registerForm.getEmail();
		final String mobileNumber = registerForm.getMobileNumber();
		if (StringUtils.isEmpty(email))
		{
			errors.rejectValue("email", "register.email.invalid");
		}
		else if (StringUtils.length(email) > 255 || !validateEmailAddress(email))
		{
			errors.rejectValue("email", "register.email.invalid");
		}

		if (StringUtils.isEmpty(mobileNumber))
		{
			errors.rejectValue("mobileNumber", "register.mobileNumber.invalid");
		}
		if (StringUtils.isEmpty(mobileNumber))
		{
			errors.rejectValue("mobileNumber", "register.mobileNumber.invalid.format");
		}


	}

	/**
	 * @param registerForm
	 * @param errors
	 */
	private void validatePassword(final B2BRegisterForm registerForm, final Errors errors)
	{
		final String pwd = registerForm.getPwd();
		final String checkPwd = registerForm.getCheckPwd();
		if (StringUtils.isEmpty(pwd))
		{
			errors.rejectValue("pwd", "register.pwd.invalid");
		}
		else if (StringUtils.length(pwd) < 6 || StringUtils.length(pwd) > 255)
		{
			errors.rejectValue("pwd", "register.pwd.invalid");
		}

		if (StringUtils.isNotEmpty(pwd) && StringUtils.isNotEmpty(checkPwd) && !StringUtils.equals(pwd, checkPwd))
		{
			errors.rejectValue("checkPwd", "validation.checkPwd.equals");
		}
		else
		{
			if (StringUtils.isEmpty(checkPwd))
			{
				errors.rejectValue("checkPwd", "register.checkPwd.invalid");
			}
		}

	}

	/**
	 * @param userType
	 * @param registerForm
	 * @param errors
	 */
	@SuppressWarnings("deprecation")
	private void validateFormForB2BANDB2E(final String userType, final B2BRegisterForm registerForm, final Errors errors)
	{
		final String companyName = registerForm.getCompanyName();

		final String accountInformation = registerForm.getAccountInformation();
		final String accountInformationNumber = registerForm.getAccountInformationNumber();
		if (UserTypes.B2E.equalsIgnoreCase(userType) || UserTypes.B2B.equalsIgnoreCase(userType))
		{

			if (StringUtils.isEmpty(companyName))
			{
				errors.rejectValue("companyName", "register.companyName.invalid");
			}

			if (StringUtils.isEmpty(accountInformation) || StringUtils.isEmpty(accountInformationNumber))
			{
				if (UserTypes.B2E.equalsIgnoreCase(userType)
						&& (registerForm.getHasExistingGreenleeAccount() == null || !registerForm.getHasExistingGreenleeAccount()
								.booleanValue()))

				{
					validateFormForB2C(userType, registerForm, errors);
				}
				else
				{
					if (StringUtils.isEmpty(accountInformationNumber))
					{
						errors.rejectValue("accountInformationNumber", "register.accountInformationNumber.invalid");
					}
					else if (StringUtils.isEmpty(accountInformation))
					{
						errors.rejectValue("accountInformation", "register.accountInformation.invalid");
					}
				}
			}
		}

	}

	private void validateFormForB2B(final B2BRegisterForm registerForm, final Errors errors)
	{
		final String accountInformation = registerForm.getAccountInformation();
		final String companyName = registerForm.getCompanyName();
		final String accountInformationNumber = registerForm.getAccountInformationNumber();
		if (StringUtils.isEmpty(companyName))
		{
			errors.rejectValue("companyName", "register.companyName.invalid");
		}

		if (StringUtils.isEmpty(accountInformation))
		{
			errors.rejectValue("accountInformation", "register.accountInformation.invalid");
		}
		if (StringUtils.isEmpty(accountInformationNumber))
		{
			errors.rejectValue("accountInformationNumber", "register.accountInformationNumber.invalid");
		}
	}

	/**
	 * @param userType
	 * @param registerForm
	 * @param errors
	 */
	@SuppressWarnings("deprecation")
	private void validateFormForB2C(final String userType, final B2BRegisterForm registerForm, final Errors errors)
	{

		final String addressLane1 = registerForm.getAddressLane1();
		final String addressLane2 = registerForm.getAddressLane2();
		final String city = registerForm.getCity();
		final String state = registerForm.getState();

		final String country = registerForm.getCountry();

		final String enteredState = registerForm.getEnteredState();
		final String zipCode = registerForm.getZipCode();

		final boolean addressLineFlag1 = rejectIfContainsPObox(addressLane1);
		if (addressLineFlag1)
		{
			errors.rejectValue("addressLane1", "address.line1.pobox.notallowed.addressForm.line1");
		}
		final boolean addressLineFlag2 = rejectIfContainsPObox(addressLane2);
		if (addressLineFlag2)
		{
			errors.rejectValue("addressLane2", "address.line2.pobox.notallowed.addressForm.line2");
		}
		if (UserTypes.B2C.equalsIgnoreCase(userType) || UserTypes.B2E.equalsIgnoreCase(userType))
		{

			validateStringEmpty("addressLane1", addressLane1, "register.addressLane1.invalid", errors);

			validateStringEmpty("city", city, "register.city.invalid", errors);

			if (!APLHABETIC_LETTERS.matcher(city).matches() && !StringUtils.isEmpty(city))
			{
				errors.rejectValue("city", "register.city.invalid.format");

			}
			if (StringUtils.isEmpty(state) && StringUtils.isEmpty(enteredState))
			{

				errors.rejectValue("enteredState", "register.state.invalid");
				errors.rejectValue("state", "register.state.invalid");
			}

			if (!StringUtils.isEmpty(enteredState) && !APLHABETIC_LETTERS.matcher(enteredState).matches())
			{
				errors.rejectValue("enteredState", "register.state.invalid.format");

			}

			validateStringEmpty("country", country, "register.country.invalid", errors);

			validateStringEmpty("zipCode", zipCode, "register.zipcode.invalid", errors);
		}

	}

	protected boolean rejectIfContainsPObox(final String addressField)
	{
		if (StringUtils.isNotBlank(addressField))
		{
			final StringBuffer addressFields = new StringBuffer();
			addressFields.append("\"");//"\"HC73 P.O. Box 217\"";
			addressFields.append(addressField);
			addressFields.append("\"");

			final Pattern REGEX_POBOX_NUM_REGEX = Pattern.compile(regex_pattern_4, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			final Matcher matcher = REGEX_POBOX_NUM_REGEX.matcher(addressFields.toString());
			LOG.info("Field :[" + addressFields + "] [" + matcher.matches() + "]");
			return matcher.matches();
		}
		return false;
	}

	public void validateStringEmpty(final String fieldTitle, final String fieldValue, final String errorMessage,
			final Errors errors)
	{
		if (StringUtils.isEmpty(fieldValue))
		{
			errors.rejectValue(fieldTitle, errorMessage);
		}
	}

	public boolean validateEmailAddress(final String email)
	{
		final Matcher matcher = EMAIL_REGEX.matcher(email);
		return matcher.matches();
	}
}
