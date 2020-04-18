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

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;
import de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.fest.util.Collections;
import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public class RestrictedTypeCodeActionEnabledStrategy implements ComponentAdminActionEnabledStrategy
{
	private static final Logger LOG = Logger.getLogger(RestrictedTypeCodeActionEnabledStrategy.class);

	private List<String> typeCodes;
	private Set<ComposedTypeModel> composedTypes;
	private CMSComponentService cmsComponentService;
	private TypeService typeService;
	private boolean visibleIfDisabled = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy#isEnabled(de.hybris.liveeditaddon
	 * .admin.ComponentActionMenuRequestData)
	 */
	@Override
	public boolean isEnabled(final ComponentActionMenuRequestData request)
	{
		init();
		if (CollectionUtils.isEmpty(composedTypes))
		{
			return false;
		}
		try
		{
			final AbstractCMSComponentModel component = getCmsComponentService().getAbstractCMSComponent(request.getComponentUid());
			final ComposedTypeModel type = getTypeService().getComposedTypeForClass(component.getClass());

			return composedTypes.contains(type);
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new IllegalStateException("component is missing for uid [" + request.getComponentUid() + "]");
		}
	}

	protected void init()
	{
		if (!Collections.isEmpty(typeCodes) && composedTypes == null && isInitialized())
		{
			final Set<ComposedTypeModel> newTypes = new LinkedHashSet<ComposedTypeModel>();
			for (final String typeCode : typeCodes)
			{
				try
				{
					final ComposedTypeModel type = getTypeService().getComposedTypeForCode(typeCode);
					newTypes.add(type);
					newTypes.addAll(type.getAllSubTypes());

				}
				catch (final UnknownIdentifierException uie)
				{
					LOG.warn("typecode [" + typeCode + "] does not exist. Ignoring.");
				}
			}
			composedTypes = newTypes;
		}
	}

	protected boolean isInitialized()
	{
		final Tenant tenant = Registry.getCurrentTenantNoFallback();
		return tenant != null && tenant.getJaloConnection().isSystemInitialized();
	}

	public void setTypeCodes(final List<String> typeCodes)
	{
		this.typeCodes = typeCodes;
	}


	/**
	 * @return the cmsComponentService
	 */
	public CMSComponentService getCmsComponentService()
	{
		return cmsComponentService;
	}

	/**
	 * @param cmsComponentService
	 *           the cmsComponentService to set
	 */
	@Required
	public void setCmsComponentService(final CMSComponentService cmsComponentService)
	{
		this.cmsComponentService = cmsComponentService;
	}

	public TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
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
	 * @see
	 * de.hybris.liveeditaddon.admin.strategies.ComponentAdminActionEnabledStrategy#isVisible(de.hybris.liveeditaddon
	 * .admin.ComponentActionMenuRequestData, boolean)
	 */
	@Override
	public boolean isVisible(final ComponentActionMenuRequestData request, final boolean enabled)
	{
		return enabled || isVisibleIfDisabled();
	}


}
