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

import de.hybris.liveeditaddon.admin.facades.impl.PreviewTicketInvalidException;
import de.hybris.liveeditaddon.controller.response.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


/**
 * 
 */
public abstract class AbstractAdminController
{

	@ExceptionHandler(PreviewTicketInvalidException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ModelAndView handleException(final PreviewTicketInvalidException e, final HttpServletRequest request)
	{
		return new ErrorResponse(e.getMessage()).asModelAndView();
	}

}
