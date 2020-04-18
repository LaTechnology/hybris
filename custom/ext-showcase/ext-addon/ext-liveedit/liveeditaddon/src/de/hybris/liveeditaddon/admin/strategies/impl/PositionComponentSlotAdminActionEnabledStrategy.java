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
package de.hybris.liveeditaddon.admin.strategies.impl;

import de.hybris.liveeditaddon.admin.SlotActionMenuRequestData;
import de.hybris.liveeditaddon.admin.strategies.ComponentSlotAdminActionEnabledStrategy;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class PositionComponentSlotAdminActionEnabledStrategy implements ComponentSlotAdminActionEnabledStrategy
{
    private static final Logger LOG = Logger.getLogger(PositionComponentSlotAdminActionEnabledStrategy.class);

    CMSPageService cmsPageService;
	String typeCode;
	TypeService typeService;
	private boolean visibleIfDisabled = false;


	private ContentSlotNameModel getContentSlotName(final PageTemplateModel pageTemplate, final String position)
	{
		ContentSlotNameModel ret = null;
		for (final ContentSlotNameModel contentSlotName : pageTemplate.getAvailableContentSlots())
		{
			if (StringUtils.equals(position, contentSlotName.getName()))
			{
				ret = contentSlotName;
				break;
			}
		}
		return ret;
	}

	public boolean isValidType(final ContentSlotNameModel contentSlotName)
	{
		ServicesUtil.validateParameterNotNull(contentSlotName, "contentSlotName cannot be null");

		final TypeModel typeModel = typeService.getTypeForCode(typeCode);
		return isValidForComponentTypeGroup(contentSlotName, typeModel)
				|| isValidForValidComponentTypes(contentSlotName, typeModel);
	}

	@SuppressWarnings("deprecation")
	private boolean isValidForValidComponentTypes(final ContentSlotNameModel contentSlotName, final TypeModel typeModel)
	{
		if (contentSlotName.getValidComponentTypes() != null && !contentSlotName.getValidComponentTypes().isEmpty())
		{
			return contentSlotName.getValidComponentTypes().contains(typeModel);
		}
		else
		{
			return false;
		}
	}

	private boolean isValidForComponentTypeGroup(final ContentSlotNameModel contentSlotName, final TypeModel typeModel)
	{
		if (contentSlotName.getCompTypeGroup() != null && contentSlotName.getCompTypeGroup().getCmsComponentTypes() != null
				&& !contentSlotName.getCompTypeGroup().getCmsComponentTypes().isEmpty())
		{
			return contentSlotName.getCompTypeGroup().getCmsComponentTypes().contains(typeModel);
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean isEnabled(final SlotActionMenuRequestData request)
	{
		try
		{
			final AbstractPageModel page = cmsPageService.getPageForId(request.getPageUid());
			if (page != null && request.getPosition() != null)
			{
				final ContentSlotNameModel contentSlotName = getContentSlotName(page.getMasterTemplate(), request.getPosition());
				if (contentSlotName != null)
				{
					return isValidType(contentSlotName);
				}
			}
		}
		catch (final Exception e)
		{
            LOG.debug(String.format("unexpected exception when trying to assess whether the PositionComponentSlotAdminAction is enabled for typeCode %s", typeCode),e);
		}
		return false;
	}

	public boolean isVisibleIfDisabled()
	{
		return visibleIfDisabled;
	}

	public void setVisibleIfDisabled(final boolean visibleIfDisabled)
	{
		this.visibleIfDisabled = visibleIfDisabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.liveeditaddon.admin.strategies.ComponentSlotAdminActionEnabledStrategy#isVisible(de.hybris.
	 * liveeditaddon.admin.SlotActionMenuRequestData, boolean)
	 */
	@Override
	public boolean isVisible(final SlotActionMenuRequestData request, final boolean enabled)
	{
		return enabled || isVisibleIfDisabled();
	}

	@Required
	public void setTypeCode(final String typeCode)
	{
		this.typeCode = typeCode;
	}

	@Required
	public void setCmsPageService(final CMSPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}


}
