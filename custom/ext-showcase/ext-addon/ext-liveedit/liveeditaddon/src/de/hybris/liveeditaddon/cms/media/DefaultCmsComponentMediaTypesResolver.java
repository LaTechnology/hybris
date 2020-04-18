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
package de.hybris.liveeditaddon.cms.media;

import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * This bean hold a list of all the component types that are have a media attribute. It's used by the drag and drop
 * image functionality so that we can start a wizard with only the types having a media attribute. It also stores the
 * media attribute
 * 
 * 
 */
public class DefaultCmsComponentMediaTypesResolver implements InitializingBean, ApplicationContextAware,
		CmsComponentMediaTypesResolver
{
	private Map<String, String> mediaComponentTypes;
	private ApplicationContext appContext;

	@Override
	public boolean isMediaComponent(final String typeCode)
	{
		return mediaComponentTypes.containsKey(typeCode);
	}

	@Override
	public String getMediaComponentAttribute(final String typeCode)
	{
		if (!isMediaComponent(typeCode))
		{
			return null;
		}
		else
		{
			return mediaComponentTypes.get(typeCode);
		}
	}

	protected Set<CMSComponentTypeModel> getValidComponentTypes(final String position, final AbstractPageModel page)
	{
		final ContentSlotNameModel slotName = getContentSlotName(position, page);
		final Set<CMSComponentTypeModel> validComponentTypes = new HashSet<CMSComponentTypeModel>();
		
		if (slotName != null && slotName.getCompTypeGroup() != null)
		{	
			validComponentTypes.addAll(slotName.getValidComponentTypes());
			validComponentTypes.addAll(slotName.getCompTypeGroup().getCmsComponentTypes());
		}
		return validComponentTypes;
	}

	@Override
	public boolean isContentSlotPositionSupportingMedia(final String position, final AbstractPageModel page)
	{
		for (final CMSComponentTypeModel type : getValidComponentTypes(position, page))
		{
			if (mediaComponentTypes.containsKey(type.getCode()))
			{
				return true;
			}
		}
		return false;
	}

	protected Set<CMSComponentTypeModel> getSupportedMediaComponentTypes(final String position, final AbstractPageModel page)
	{
		final Set<CMSComponentTypeModel> supportedMediaComponentTypes = new HashSet<CMSComponentTypeModel>();
		for (final CMSComponentTypeModel type : getValidComponentTypes(position, page))
		{
			if (mediaComponentTypes.containsKey(type.getItemtype()))
			{
				supportedMediaComponentTypes.add(type);
			}
		}

		return supportedMediaComponentTypes;
	}

	protected ContentSlotNameModel getContentSlotName(final String position, final AbstractPageModel page)
	{
		for (final ContentSlotNameModel name : page.getMasterTemplate().getAvailableContentSlots())
		{
			if (name.getName().equalsIgnoreCase(position))
			{
				return name;
			}
		}
		return null;
	}

	/**
	 * Returns all the media component types as a comma sep string
	 * 
	 */
	@Override
	public String getMediaComponentTypesAsStringList()
	{
		final StringBuilder strBuilder = new StringBuilder();
		for (final String type : mediaComponentTypes.keySet())
		{
			strBuilder.append(type + ",");
		}
		return StringUtils.removeEnd(strBuilder.toString(), ",");

	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		this.mediaComponentTypes = new HashMap<String, String>();

		final Collection<CmsComponentMediaTypeMapping> mappings = this.appContext
				.getBeansOfType(CmsComponentMediaTypeMapping.class).values();
		for (final CmsComponentMediaTypeMapping mapping : mappings)
		{
			this.mediaComponentTypes.putAll(mapping.getMappings());
		}

	}

	@Override
	public void setApplicationContext(final ApplicationContext arg0) throws BeansException
	{
		this.appContext = arg0;

	}
}
