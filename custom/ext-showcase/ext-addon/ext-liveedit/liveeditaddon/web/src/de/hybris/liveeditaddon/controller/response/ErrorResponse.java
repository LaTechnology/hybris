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
package de.hybris.liveeditaddon.controller.response;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.common.collect.ImmutableMap;


/**
 * 
 */
public class ErrorResponse
{
	private final String message;
	private final boolean success = false;


	public ErrorResponse(final String message)
	{
		this.message = message;
	}

	public ModelAndView asModelAndView()
	{
		final MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		return new ModelAndView(jsonView, ImmutableMap.of("error", message, "success", Boolean.valueOf(success)));
	}
}
