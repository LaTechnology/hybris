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

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver;


/**
 * 
 */
@Component
public class AnnotatedExceptionResolver extends AnnotationMethodHandlerExceptionResolver
{
	public AnnotatedExceptionResolver()
	{
		setOrder(HIGHEST_PRECEDENCE);
	}
}
