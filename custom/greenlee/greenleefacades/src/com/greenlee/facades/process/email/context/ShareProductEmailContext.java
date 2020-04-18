/**
 *
 */
package com.greenlee.facades.process.email.context;


import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.greenlee.core.model.GreenleeProductModel;
import com.greenlee.core.model.ShareProductProcessModel;



/**
 *
 */
public class ShareProductEmailContext extends AbstractEmailContext<ShareProductProcessModel>
{
    private static final Logger  LOG = Logger.getLogger(ShareProductEmailContext.class);
    private String               replyToAddress;
    private GreenleeProductModel greenleeProductModel;
    private String               message;

    /**
     * @return the replyToAddress
     */
    protected String getReplyToAddress()
    {
        return replyToAddress;
    }

    /**
     * @param replyToAddress
     *            the replyToAddress to set
     */
    public void setReplyToAddress(final String replyToAddress)
    {
        this.replyToAddress = replyToAddress;
    }

    /**
     * @return the message
     */
    protected String getMessage()
    {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(final String message)
    {
        this.message = message;
    }

    /**
     * @return the greenleeProductModel
     */
    public GreenleeProductModel getGreenleeProductModel()
    {
        return greenleeProductModel;
    }

    /**
     * @param greenleeProductModel
     *            the greenleeProductModel to set
     */
    public void setGreenleeProductModel(final GreenleeProductModel greenleeProductModel)
    {
        this.greenleeProductModel = greenleeProductModel;
    }

    public String getShareMessage()
    {
        return getMessage();
    }

    public String getSender()
    {
        return getReplyToAddress();
    }

    public String getProductName()
    {
        return getGreenleeProductModel().getName();
    }

    public String getProductDescription()
    {
        return getGreenleeProductModel().getDescription();
    }

    public String getProductImage()
    {
        if (getGreenleeProductModel().getPicture() != null)
        {
            return getSiteBaseUrlResolutionService().getMediaUrlForSite(getBaseSite(), true,
                    getGreenleeProductModel().getPicture().getURL());
        }
        else
        {
            return null;
        }
    }

    public String getSecureProductURL() throws UnsupportedEncodingException
    {
        return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), true,
                "/p/" + getGreenleeProductModel().getCode());
    }

    public String getProductURL() throws UnsupportedEncodingException
    {
        return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), false,
                "/p/" + getGreenleeProductModel().getCode());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.greenlee.facades.process.email.context.CustomerEmailContext#init(de.hybris.platform.commerceservices.model.
     * process.StoreFrontCustomerProcessModel, de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel)
     */
    @Override
    public void init(final ShareProductProcessModel shareProductProcessModel, final EmailPageModel emailPageModel)
    {
        final BaseSiteModel baseSite = getSite(shareProductProcessModel);
        if (baseSite == null)
        {
            LOG.error("Failed to lookup Site for BusinessProcess [" + shareProductProcessModel + "]");
        }
        else
        {
            put(BASE_SITE, baseSite);
            setUrlEncodingAttributes(getUrlEncoderService().getUrlEncodingPatternForEmail(shareProductProcessModel));
            final SiteBaseUrlResolutionService siteBaseUrlResolutionService = getSiteBaseUrlResolutionService();
            // Lookup the site specific URLs
            put(BASE_URL, siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSite, getUrlEncodingAttributes(), false, ""));
            put(BASE_THEME_URL, siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSite, false, ""));
            put(SECURE_BASE_URL,
                    siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSite, getUrlEncodingAttributes(), true, ""));
            put(MEDIA_BASE_URL, siteBaseUrlResolutionService.getMediaUrlForSite(baseSite, false));
            put(MEDIA_SECURE_BASE_URL, siteBaseUrlResolutionService.getMediaUrlForSite(baseSite, true));

            put(THEME, baseSite.getTheme() != null ? baseSite.getTheme().getCode() : null);
        }
        put(TITLE, "");
        put(DISPLAY_NAME, shareProductProcessModel.getToAddress());
        put(EMAIL, shareProductProcessModel.getToAddress());
        setGreenleeProductModel(shareProductProcessModel.getGreenleeProductModel());
        setMessage(shareProductProcessModel.getMessage());
        put(FROM_EMAIL, emailPageModel.getFromEmail());
        put(FROM_DISPLAY_NAME, emailPageModel.getFromName());
        setReplyToAddress(shareProductProcessModel.getFromAddress());
    }

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getSite(de.hybris.platform.
     * processengine.model.BusinessProcessModel)
     */
    @Override
    protected BaseSiteModel getSite(final ShareProductProcessModel businessProcessModel)
    {
        return businessProcessModel.getSite();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getCustomer(de.hybris.platform.
     * processengine.model.BusinessProcessModel)
     */
    @Override
    protected CustomerModel getCustomer(final ShareProductProcessModel businessProcessModel)
    {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getEmailLanguage(de.hybris.
     * platform.processengine.model.BusinessProcessModel)
     */
    @Override
    protected LanguageModel getEmailLanguage(final ShareProductProcessModel businessProcessModel)
    {
        return null;
    }
}
