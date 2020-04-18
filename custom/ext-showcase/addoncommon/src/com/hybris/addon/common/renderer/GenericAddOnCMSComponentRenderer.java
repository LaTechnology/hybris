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
package com.hybris.addon.common.renderer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.model.ModelService;

public class GenericAddOnCMSComponentRenderer<C extends AbstractCMSComponentModel> extends DefaultAddOnCMSComponentRenderer<C>{

	private ModelService modelService;
	private CMSComponentService cmsComponentService;
	private String cmsComponentFolder = "cms";
	
	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		final Map<String,Object> variables = new HashMap<String,Object>();
		for (final String property : getCmsComponentService().getEditorProperties(component))
		{
			try
			{
				final Object value = modelService.getAttributeValue(component, property);
				variables.put(property, value);
			}
			catch (final AttributeNotSupportedException ignore)
			{
				// ignore
			}
		}
		return variables;
	}
	@Override
	protected String getCmsComponentFolder()
	{
		return this.cmsComponentFolder;
	}

	public ModelService getModelService() {
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	public CMSComponentService getCmsComponentService() {
		return cmsComponentService;
	}

	@Required
	public void setCmsComponentService(CMSComponentService cmsComponentService) {
		this.cmsComponentService = cmsComponentService;
	}
	
	public void setCmsComponentFolder(String cmsComponentFolder)
	{
		this.cmsComponentFolder = cmsComponentFolder;
	}
}
