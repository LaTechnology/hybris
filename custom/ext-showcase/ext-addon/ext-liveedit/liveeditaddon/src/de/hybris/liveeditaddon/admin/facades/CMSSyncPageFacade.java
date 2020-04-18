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
package de.hybris.liveeditaddon.admin.facades;

import de.hybris.liveeditaddon.admin.facades.impl.PreviewTicketInvalidException;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;


public interface CMSSyncPageFacade {

    /**
     * this method checks whether the page as a whole is synchronized
     * @param previewTicket
     * @param pageUid
     * @return
     * @throws PreviewTicketInvalidException
     * @throws CMSItemNotFoundException
     */
    boolean isPageSynchronized(String previewTicket, String pageUid) throws PreviewTicketInvalidException, CMSItemNotFoundException;

    /**
     * performs the action of synchronising and return status and pertaining message
     * @param previewTicket
     * @param pageUid
     * @return
     * @throws PreviewTicketInvalidException
     * @throws CMSItemNotFoundException
     */
    public SyncResponse synchronizePage(final String previewTicket, final String pageUid) throws PreviewTicketInvalidException, CMSItemNotFoundException;


}
