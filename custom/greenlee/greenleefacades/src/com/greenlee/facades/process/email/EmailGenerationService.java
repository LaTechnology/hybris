/**
 *
 */
package com.greenlee.facades.process.email;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;

import com.greenlee.core.model.ShareProductProcessModel;


/**
 *
 */
public interface EmailGenerationService
{
    /**
     * Generates EmailMessage give share product process and cms email page.
     *
     * @param shareProductProcessModel
     * @param emailPageModel
     * @return
     */
    EmailMessageModel generate(ShareProductProcessModel shareProductProcessModel, EmailPageModel emailPageModel);
}
