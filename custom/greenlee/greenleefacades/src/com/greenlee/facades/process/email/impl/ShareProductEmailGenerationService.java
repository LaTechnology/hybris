/**
 *
 */
package com.greenlee.facades.process.email.impl;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageTemplateModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.EmailContextFactory;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.greenlee.core.model.ShareProductProcessModel;
import com.greenlee.facades.process.email.context.ShareProductEmailContext;


/**
 *
 */
public class ShareProductEmailGenerationService extends AbstractBusinessService
        implements com.greenlee.facades.process.email.EmailGenerationService
{
    private static final Logger                           LOG = Logger.getLogger(DefaultEmailGenerationService.class);

    private EmailService                                  emailService;
    private RendererService                               rendererService;
    private EmailContextFactory<ShareProductProcessModel> emailContextFactory;

    protected boolean validate(final ShareProductEmailContext emailContext)
    {
        boolean valid = true;
        if (StringUtils.isBlank(emailContext.getToEmail()))
        {
            LOG.error("Missing ToEmail in ShareProductEmailContext");
            valid = false;
        }

        if (StringUtils.isBlank(emailContext.getFromEmail()))
        {
            LOG.error("Missing FromEmail in ShareProductEmailContext");
            valid = false;
        }
        return valid;
    }

    protected EmailMessageModel createEmailMessage(final String emailSubject, final String emailBody,
            final ShareProductEmailContext emailContext)
    {
        final List<EmailAddressModel> toEmails = new ArrayList<>();
        final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
                emailContext.getToDisplayName());
        toEmails.add(toAddress);
        final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
                emailContext.getFromDisplayName());
        return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(),
                new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getSender(), emailSubject, emailBody, null);
    }

    protected EmailService getEmailService()
    {
        return emailService;
    }

    @Required
    public void setEmailService(final EmailService emailService)
    {
        this.emailService = emailService;
    }

    protected RendererService getRendererService()
    {
        return rendererService;
    }

    @Required
    public void setRendererService(final RendererService rendererService)
    {
        this.rendererService = rendererService;
    }

    protected EmailContextFactory<ShareProductProcessModel> getEmailContextFactory()
    {
        return emailContextFactory;
    }

    @Required
    public void setEmailContextFactory(final EmailContextFactory<ShareProductProcessModel> emailContextFactory)
    {
        this.emailContextFactory = emailContextFactory;
    }

    /* (non-Javadoc)
     * @see com.greenlee.facades.process.email.EmailGenerationService#generate(com.greenlee.core.model.ShareProductProcessModel, de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel)
     */
    @Override
    public EmailMessageModel generate(final ShareProductProcessModel businessProcessModel, final EmailPageModel emailPageModel)
    {
        ServicesUtil.validateParameterNotNull(emailPageModel, "EmailPageModel cannot be null");
        Assert.isInstanceOf(EmailPageTemplateModel.class, emailPageModel.getMasterTemplate(),
                "MasterTemplate associated with EmailPageModel should be EmailPageTemplate");

        final EmailPageTemplateModel emailPageTemplateModel = (EmailPageTemplateModel) emailPageModel.getMasterTemplate();
        final RendererTemplateModel bodyRenderTemplate = emailPageTemplateModel.getHtmlTemplate();
        Assert.notNull(bodyRenderTemplate, "HtmlTemplate associated with MasterTemplate of EmailPageModel cannot be null");
        final RendererTemplateModel subjectRenderTemplate = emailPageTemplateModel.getSubject();
        Assert.notNull(subjectRenderTemplate, "Subject associated with MasterTemplate of EmailPageModel cannot be null");

        final EmailMessageModel emailMessageModel;
        //This call creates the context to be used for rendering of subject and body templates.
        final ShareProductEmailContext emailContext = (ShareProductEmailContext) getEmailContextFactory()
                .create(businessProcessModel, emailPageModel, bodyRenderTemplate);

        if (emailContext == null)
        {
            LOG.error("Failed to create email context for businessProcess [" + businessProcessModel + "]");
            throw new RuntimeException("Failed to create email context for businessProcess [" + businessProcessModel + "]");
        }
        else
        {
            if (!validate(emailContext))
            {
                LOG.error("Email context for businessProcess [" + businessProcessModel + "] is not valid: "
                        + ReflectionToStringBuilder.toString(emailContext));
                throw new RuntimeException("Email context for businessProcess [" + businessProcessModel + "] is not valid: "
                        + ReflectionToStringBuilder.toString(emailContext));
            }

            final StringWriter subject = new StringWriter();
            getRendererService().render(subjectRenderTemplate, emailContext, subject);

            final StringWriter body = new StringWriter();
            getRendererService().render(bodyRenderTemplate, emailContext, body);

            emailMessageModel = createEmailMessage(subject.toString(), body.toString(), emailContext);

            if (LOG.isDebugEnabled())
            {
                LOG.debug("Email Subject: " + emailMessageModel.getSubject());
                LOG.debug("Email Body: " + emailMessageModel.getBody());
            }

        }

        return emailMessageModel;
    }
}
