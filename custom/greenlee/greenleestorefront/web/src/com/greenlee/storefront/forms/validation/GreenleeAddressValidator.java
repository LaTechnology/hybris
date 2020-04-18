/**
 *
 */
package com.greenlee.storefront.forms.validation;

import com.greenlee.core.checkout.services.RegionCountryService;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.AddressValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.annotation.Resource;

/**
 * @author raja.santhanam
 *
 */
@Component("greenleeAddressValidator")
public class GreenleeAddressValidator extends AddressValidator
{

	@Resource(name = "greenleeRegionCountryService")
	private RegionCountryService regionCountryService;

	/**
	 * The following entries will be matched for the below regex
	 *
	 * "PO Box  ","PO Box","P O Box","P O Box 123","PO Box #123","PO. Box 123","PO Box 123", "P. O. Box","P.O.Box",
	 * "Post Box","Post Office Box","Post Office","P.O.B","P.O.B.","POB"
	 */
	//private static final String REGEX_POBOX = "(?i)^\\s*((P(OST)?[.]*[\\s]*(O(FF(ICE)?)?)?[.]*[\\s]*(B(IN|OX))?)|B(IN|OX)).*";
	private static final String REGEX_POBOX = "(\"[\\w\\s]*?p\\s*[.-]?\\s*o?\\s*\\.?\\s*b?(ox)?\\s*(\\d+)?\")";
	//GRE-2100
	private static final String REGEX_ALPHA = "^[a-zA-Z\\s]*$";

	private static final int MAX_FIELD_LENGTH = 255;
	private static final int MAX_POSTCODE_LENGTH = 10;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorstorefrontcommons.forms.validation.AddressValidator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object object, final Errors errors)
	{
		final AddressForm addressForm = (AddressForm) object;

		validateStringField(addressForm.getFirstName(), AddressField.FIRSTNAME, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getLastName(), AddressField.LASTNAME, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getPostcode(), AddressField.POSTCODE, MAX_POSTCODE_LENGTH, errors);

		rejectIfContainsPObox(addressForm.getLine1(), "line1", "address.line1.pobox.notallowed", errors);
		rejectIfContainsPObox(addressForm.getLine2(), "line2", "address.line2.pobox.notallowed", errors);
		rejectIfContainsNumeric(addressForm.getTownCity(), "townCity", "address.city.numeric.notallowed", errors);
		rejectIfInvalidPhone(addressForm.getPhone(), "phone", "address.phone.notallowed", errors);
		/*
		 * if (StringUtils.isNotBlank(addressForm.()) && StringUtils.isNotBlank(addressForm.getCountryIso())) { if
		 * (!addressForm.getCountryIso().equalsIgnoreCase(addressForm.getPrimaryAddressCountryIso())) {
		 * errors.rejectValue("countryIso", "address.regionIso.valid.country"); } }
		 */
		if (StringUtils.isNotBlank(addressForm.getCountryIso()))
		{
			if (regionCountryService.hasRegion(addressForm.getCountryIso()) && StringUtils.isBlank(addressForm.getRegionIso()))
			{
				errors.rejectValue("regionIso", "address.regionIso.invalid");
			}
		}
	}

	/**
	 * @param phone
	 * @param fieldKey
	 * @param errorKey
	 * @param errors
	 */
	private void rejectIfInvalidPhone(final String phone, final String fieldKey, final String errorKey, final Errors errors)
	{
		if (StringUtils.isBlank(phone))
		{
			errors.rejectValue(fieldKey, errorKey);
		}
	}

	protected void rejectIfContainsPObox(final String addressField, final String fieldKey, final String errorKey,
			final Errors errors)
	{
		if (StringUtils.isNotBlank(addressField) && addressField.matches(REGEX_POBOX))
		{
			errors.rejectValue(fieldKey, errorKey);
		}
	}

	protected void rejectIfContainsNumeric(final String city, final String fieldKey, final String errorKey, final Errors errors)
	{
		if (StringUtils.isNotBlank(city) && !city.matches(REGEX_ALPHA))
		{
			errors.rejectValue(fieldKey, errorKey);
		}
	}
}
