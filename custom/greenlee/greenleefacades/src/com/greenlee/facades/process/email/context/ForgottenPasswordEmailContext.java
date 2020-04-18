/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.greenlee.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.model.process.ForgottenPasswordProcessModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Velocity context for a forgotten password email.
 */
public class ForgottenPasswordEmailContext extends CustomerEmailContext
{
    private int                expiresInMinutes = 30;
    private String             token;
    private boolean            activeB2BUser;
    private boolean            userCreatedByAdmin;
    public static final String COMPANY_NAME     = "companyName";

    /**
     * @return the userCreatedByAdmin
     */
    public boolean isUserCreatedByAdmin()
    {
        return userCreatedByAdmin;
    }

    /**
     * @param userCreatedByAdmin
     *            the userCreatedByAdmin to set
     */
    public void setUserCreatedByAdmin(final boolean userCreatedByAdmin)
    {
        this.userCreatedByAdmin = userCreatedByAdmin;
    }

    public boolean isActiveB2BUser()
    {
        return activeB2BUser;
    }

    public void setActiveB2BUser(final boolean activeB2BUser)
    {
        this.activeB2BUser = activeB2BUser;
    }

    public int getExpiresInMinutes()
    {
        return expiresInMinutes;
    }

    public void setExpiresInMinutes(final int expiresInMinutes)
    {
        this.expiresInMinutes = expiresInMinutes;
    }

    @Override
    public String getToken()
    {
        return token;
    }

    @Override
    public void setToken(final String token)
    {
        this.token = token;
    }

    @Override
    public String getURLEncodedToken() throws UnsupportedEncodingException
    {
        return URLEncoder.encode(token, "UTF-8");
    }

    public String getRequestResetPasswordUrl() throws UnsupportedEncodingException
    {
        return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), false,
                "/login/pw/request/external");
    }

    public String getSecureRequestResetPasswordUrl() throws UnsupportedEncodingException
    {
        return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), true,
                "/login/pw/request/external");
    }

    public String getResetPasswordUrl() throws UnsupportedEncodingException
    {
        return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), false,
                "/login/pw/change", "token=" + getURLEncodedToken());
    }

    public String getSecureResetPasswordUrl() throws UnsupportedEncodingException
    {
        return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), true,
                "/login/pw/change", "token=" + getURLEncodedToken());
    }

    public String getDisplayResetPasswordUrl() throws UnsupportedEncodingException
    {
        return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), false,
                "/my-account/update-password");
    }

    public String getDisplaySecureResetPasswordUrl() throws UnsupportedEncodingException
    {
        return getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getBaseSite(), getUrlEncodingAttributes(), true,
                "/my-account/update-password");
    }

    @Override
    public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
    {
        super.init(storeFrontCustomerProcessModel, emailPageModel);
        if (storeFrontCustomerProcessModel instanceof ForgottenPasswordProcessModel)
        {
            setToken(((ForgottenPasswordProcessModel) storeFrontCustomerProcessModel).getToken());
            final B2BCustomerModel customer = (B2BCustomerModel) storeFrontCustomerProcessModel.getCustomer();
            setActiveB2BUser(customer.getActive().booleanValue());
            setUserCreatedByAdmin(((ForgottenPasswordProcessModel) storeFrontCustomerProcessModel).isUserAddedByAdmin());
        }
        final CustomerModel customerModel = getCustomer(storeFrontCustomerProcessModel);
        for (final PrincipalGroupModel userGroupModel : customerModel.getGroups())
        {
            if (userGroupModel instanceof B2BUnitModel)
            {
                final B2BUnitModel b2bunit = (B2BUnitModel) userGroupModel;
                put(COMPANY_NAME, b2bunit.getLocName());
            }
        }
    }
}
