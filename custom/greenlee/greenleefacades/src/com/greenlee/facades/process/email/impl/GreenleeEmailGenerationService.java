/**
 *
 */
package com.greenlee.facades.process.email.impl;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.greenlee.facades.constants.GreenleeFacadesConstants;


/**
 * @author raja.santhanam
 * 
 */
public class GreenleeEmailGenerationService extends DefaultEmailGenerationService
{

    @Override
    protected EmailMessageModel createEmailMessage(final String emailSubject, final String emailBody,
            final AbstractEmailContext<BusinessProcessModel> emailContext)
    {
        final ArrayList<EmailAddressModel> ccAddresses = new ArrayList<>();
        final List<EmailAddressModel> toEmails = new ArrayList<>();
        //To addresses
        final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
                emailContext.getToDisplayName());
        toEmails.add(toAddress);
        //Additional to addresses
        final Collection<String> additionalToAddressCollection = (Collection<String>) emailContext
                .get(GreenleeFacadesConstants.ADDITIONAL_TO_EMAIL_ADDRESSES);
        if (CollectionUtils.isNotEmpty(additionalToAddressCollection) && !additionalToAddressCollection.contains(""))
        {
            for (final String additionaltoAddress : additionalToAddressCollection)
            {
                final EmailAddressModel toAddressForCancel = getEmailService().getOrCreateEmailAddressForEmail(
                        additionaltoAddress, additionaltoAddress);
                toEmails.add(toAddressForCancel);
            }
        }
        //CC email addresses
        final Collection<String> ccAddressCollection = (Collection<String>) emailContext
                .get(GreenleeFacadesConstants.EMAIL_CC_ADDRESSES);
        if (CollectionUtils.isNotEmpty(ccAddressCollection) && !ccAddressCollection.contains(""))
        {
            for (final String ccAddress : ccAddressCollection)
            {
                final EmailAddressModel ccAddressModel = getEmailService().getOrCreateEmailAddressForEmail(ccAddress, ccAddress);
                ccAddresses.add(ccAddressModel);
            }
        }
        //From addresses
        final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
                emailContext.getFromDisplayName());


        return getEmailService().createEmailMessage(toEmails, ccAddresses, new ArrayList<EmailAddressModel>(), fromAddress,
                emailContext.getFromEmail(), emailSubject, emailBody, null);
    }
}
