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

import de.hybris.liveeditaddon.admin.SlotActionMenuRequestData;
import de.hybris.liveeditaddon.admin.facades.CMSSlotAdminFacade;
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
@RequestMapping(value = "/slot/**")
public class SlotAdminController extends AbstractAdminController
{

	@Resource(name = "cmsSlotAdminFacade")
	private CMSSlotAdminFacade cmsSlotAdminFacade;

	@ResponseBody
	@RequestMapping(value = "/menu.json", method = RequestMethod.GET)
	public SuccessResponse menu(@RequestParam(value = "previewTicket") final String previewTicket,
			@RequestParam(value = "slotUid") final String slotUid, @RequestParam(value = "pageUid") final String pageUid,
			@RequestParam(value = "position") final String position, @RequestParam(value = "url") final String url)
			throws PreviewTicketInvalidException

	{

		final SlotActionMenuRequestData slotActionMenuRequestData = new SlotActionMenuRequestData();
		slotActionMenuRequestData.setPreviewTicket(previewTicket);
		slotActionMenuRequestData.setPageUid(pageUid);
		slotActionMenuRequestData.setSlotUid(slotUid);
		slotActionMenuRequestData.setPosition(position);
		slotActionMenuRequestData.setUrl(url);
		return new SuccessResponse(cmsSlotAdminFacade.getLiveEditSlotAdminMenu(slotActionMenuRequestData));

	}

}
