/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 * 
 */
package com.hybris.addon.common.interceptors.helper;

import java.util.LinkedList;
import java.util.List;

import org.springframework.ui.ModelMap;

import de.hybris.platform.acceleratorservices.storefront.data.JavaScriptVariableData;

public final class JavaScriptVariableHelper {

	private JavaScriptVariableHelper()
	{
		
	}
	
	public static List<JavaScriptVariableData> getVariables(final ModelMap model)
	{
		List<JavaScriptVariableData> variables = (List<JavaScriptVariableData>) model.get("jsVariables");
		if (variables == null)
		{
			variables = new LinkedList<JavaScriptVariableData>();
			model.put("jsVariables", variables);
		}
		return variables;
	}

	public static JavaScriptVariableData createJavaScriptVariable(final String qualifier, final String value)
	{
		final JavaScriptVariableData variable = new JavaScriptVariableData();
		variable.setQualifier(qualifier);
		variable.setValue(value);
		return variable;
	}
}
