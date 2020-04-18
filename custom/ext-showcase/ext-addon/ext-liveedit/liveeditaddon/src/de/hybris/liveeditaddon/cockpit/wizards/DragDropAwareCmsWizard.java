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
package de.hybris.liveeditaddon.cockpit.wizards;

import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.liveeditaddon.cms.media.CmsComponentMediaTypesResolver;

import org.apache.commons.lang.StringUtils;
import org.zkoss.spring.SpringUtil;


/**
 * 
 */
public class DragDropAwareCmsWizard extends CmsWizard
{

	/**
	 * @param browserModel
	 * @param browserSectionModel
	 */
	public DragDropAwareCmsWizard(final BrowserModel browserModel, final BrowserSectionModel browserSectionModel)
	{
		super(browserModel, browserSectionModel);
	}

	private MediaModel mediaModel;

	/**
	 * Override the loading of default values so we can set our media attribute
	 */
	@Override
	public void loadDefaultValues()
	{
		super.loadDefaultValues();
		addDefaultMediaValueIfExists();
	}

	protected void addDefaultMediaValueIfExists()
	{
		final ObjectType choosenType = getCurrentType();
		final CmsComponentMediaTypesResolver cmsComponentMediaTypesResolver = (CmsComponentMediaTypesResolver) SpringUtil
				.getBean("cmsComponentMediaTypesResolver");
		if (mediaModel != null && cmsComponentMediaTypesResolver.isMediaComponent(choosenType.getCode()))
		{
			final ObjectValueContainer globalObjectValueContainer = getObjectValueContainer();
			final String mediaQualifier = cmsComponentMediaTypesResolver.getMediaComponentAttribute(choosenType.getCode());

			//
			// Do some sanity checking
			//
			if (StringUtils.isEmpty(mediaQualifier))
			{
				throw new IllegalArgumentException("No media attribute configured for type [" + choosenType.getCode()
						+ "], check configuration of CmsComponentMediaTypesResolver");
			}

			PropertyDescriptor mediaDescriptor = null;
			for (final PropertyDescriptor descriptor : globalObjectValueContainer.getPropertyDescriptors())
			{
				if (descriptor.getQualifier().equalsIgnoreCase(mediaQualifier))
				{
					mediaDescriptor = descriptor;
					break;
				}
			}

			//
			// More sanity checking
			//
			if (mediaDescriptor == null)
			{
				throw new IllegalArgumentException("No attribute of qualifier [" + mediaQualifier + "] for type ["
						+ choosenType.getCode() + "], check configuration of CmsComponentMediaTypesResolver");
			}

			// 
			// Set the default value
			//
			ObjectValueHolder localValueHolder = null;
			if (mediaDescriptor.isLocalized())
			{
				localValueHolder = globalObjectValueContainer.getValue(mediaDescriptor, UISessionUtils.getCurrentSession()
						.getLanguageIso());
			}
			else
			{
				localValueHolder = globalObjectValueContainer.getValue(mediaDescriptor, null);
			}
			localValueHolder.setLocalValue(mediaModel);
			localValueHolder.stored();
		}
	}

	/**
	 * @return the mediaModel
	 */
	public MediaModel getMediaModel()
	{
		return mediaModel;
	}

	/**
	 * @param mediaModel
	 *           the mediaModel to set
	 */
	public void setMediaModel(final MediaModel mediaModel)
	{
		this.mediaModel = mediaModel;
	}
}
