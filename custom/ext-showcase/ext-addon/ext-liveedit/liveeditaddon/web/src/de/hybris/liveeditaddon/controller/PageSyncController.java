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
package de.hybris.liveeditaddon.controller;

import de.hybris.liveeditaddon.admin.facades.CMSSyncPageFacade;
import de.hybris.liveeditaddon.admin.facades.SyncResponse;
import de.hybris.liveeditaddon.admin.facades.impl.PreviewTicketInvalidException;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(value = "/page/{pageUid}/syncStatus")
public class PageSyncController extends AbstractAdminController {

    @Autowired
    @Qualifier("cmsSyncPageFacade")
    private CMSSyncPageFacade cmsSyncPageFacade;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public boolean isPageSynchronized(@RequestParam(value = "previewTicket") final String previewTicket, @PathVariable("pageUid") final String pageUid) throws PreviewTicketInvalidException, CMSItemNotFoundException {
        return cmsSyncPageFacade.isPageSynchronized(previewTicket, pageUid);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public SyncResponse synchronizePage(@RequestParam(value = "previewTicket") final String previewTicket, @PathVariable("pageUid") final String pageUid) throws PreviewTicketInvalidException, CMSItemNotFoundException {
        return cmsSyncPageFacade.synchronizePage(previewTicket, pageUid);
    }
}
