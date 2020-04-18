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
package com.hybris.addon.cockpits.components.listview;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author rmcotton
 * 
 */
public abstract class AbstractConfigurableImageUriListViewAction extends AbstractListViewAction
{
	private Map<? extends Object, String> imageUriMap;
	private String defaultUri;

	@Override
	public String getImageURI(final Context context)
	{
		if (!isValid(context))
		{
			return null;
		}
		final String uri = getImageUriMap().get(getKey(context));
		if (StringUtils.isBlank(uri))
		{
			return getDefaultUri();
		}
		return uri;


	}

	protected abstract Object getKey(final Context context);

	protected abstract boolean isValid(final Context context);

	public Map<? extends Object, String> getImageUriMap()
	{
		return imageUriMap;
	}

	@Required
	public void setImageUriMap(final Map<? extends Object, String> imageUriMap)
	{
		this.imageUriMap = imageUriMap;
	}

	public String getDefaultUri()
	{
		return defaultUri;
	}

	@Required
	public void setDefaultUri(final String defaultUri)
	{
		this.defaultUri = defaultUri;
	}


}
