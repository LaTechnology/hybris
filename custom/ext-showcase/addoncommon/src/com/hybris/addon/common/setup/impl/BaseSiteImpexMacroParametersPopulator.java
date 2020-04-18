/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.hybris.addon.common.setup.impl;

import java.util.List;

import de.hybris.platform.addonsupport.setup.impl.AddOnDataImportEventContext;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;


public class BaseSiteImpexMacroParametersPopulator implements Populator<AddOnDataImportEventContext, ImpexMacroParameterData>
{

	@Override
	public void populate(final AddOnDataImportEventContext source, final ImpexMacroParameterData target)
			throws ConversionException
	{
		target.setSiteUid(source.getBaseSite().getUid());
		target.setStoreUid(source.getBaseSite().getUid());
		String solrIndexedType = null;
		SolrFacetSearchConfigModel solrFacetSearchConfiguration = source.getBaseSite().getSolrFacetSearchConfiguration();
		if (solrFacetSearchConfiguration != null) {
			List<SolrIndexedTypeModel> solrIndexedTypes = solrFacetSearchConfiguration.getSolrIndexedTypes();
			if (solrIndexedTypes != null && !solrIndexedTypes.isEmpty()) {
				solrIndexedType = solrIndexedTypes.get(0).getIdentifier();
			}
		}
		target.setSolrIndexedType(solrIndexedType);

		if (source.getBaseSite().getChannel() != null)
		{
			target.setChannel(StringUtils.lowerCase(source.getBaseSite().getChannel().getCode()));
		}
	}
}
