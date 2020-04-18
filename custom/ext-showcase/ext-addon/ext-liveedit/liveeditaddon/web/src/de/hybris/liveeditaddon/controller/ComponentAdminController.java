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

import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;
import de.hybris.liveeditaddon.admin.facades.CMSComponentAdminFacade;
import de.hybris.liveeditaddon.admin.facades.impl.PreviewTicketInvalidException;
import de.hybris.liveeditaddon.controller.response.SuccessResponse;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 
 */
@Controller
@RequestMapping(value = "/component/**")
public class ComponentAdminController extends AbstractAdminController
{

	@Resource(name = "cmsComponentAdminFacade")
	private CMSComponentAdminFacade cmsComponentAdminFacade;

	@ResponseBody
	@RequestMapping(value = "/menu.json", method = RequestMethod.GET)
	public SuccessResponse menu(@RequestParam(value = "previewTicket") final String previewTicket,
			@RequestParam(value = "componentUid") final String componentUid, @RequestParam(value = "slotUid") final String slotUid,
			@RequestParam(value = "pageUid") final String pageUid, @RequestParam(value = "url") final String url)
			throws PreviewTicketInvalidException

	{
		final ComponentActionMenuRequestData componentActionMenuRequestData = new ComponentActionMenuRequestData();
		componentActionMenuRequestData.setComponentUid(componentUid);
		componentActionMenuRequestData.setPreviewTicket(previewTicket);
		componentActionMenuRequestData.setPageUid(pageUid);
		componentActionMenuRequestData.setSlotUid(slotUid);
		componentActionMenuRequestData.setUrl(url);
		return new SuccessResponse(cmsComponentAdminFacade.getLiveEditAdminMenu(componentActionMenuRequestData));
	}


}
