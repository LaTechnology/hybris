package de.hybris.mediaconversionimageresizeplugin.dao.impl;

import de.hybris.mediaconversionimageresizeplugin.dao.ConversionGroupDao;
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;


/**
 * author: dariusz.malachowski
 */
public class DefaultConversionGroupDao implements ConversionGroupDao
{

	private final String QUERY_BY_SITE = "Select {pk} FROM {ConversionGroup} WHERE {code}=?code";
	private final static String DEFAULT_GROUP = "MediaConversionPluginConversionGroup";
	private final static String PATTERN_GROUP = "ConversionGroup";

	private FlexibleSearchService searchService;

	@Override
	public ConversionGroupModel getConvertionGroupBySite(final String siteUid)
	{
		String code = getNameBySiteUid(siteUid);
		ConversionGroupModel conversionGroupModel;
		try
		{
			conversionGroupModel = getConvertionGroupByCode(code);
		}
		catch (ModelNotFoundException e)
		{
			conversionGroupModel = getDefaultConvertionGroup();
		}
		return conversionGroupModel;
	}

	@Override
	public ConversionGroupModel getConvertionGroupByCode(final String code)
	{
		final HashMap<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY_BY_SITE, map);
		return getSearchService().searchUnique(query);
	}

	@Override
	public ConversionGroupModel getDefaultConvertionGroup()
	{
		return getConvertionGroupByCode(DEFAULT_GROUP);
	}

	private String getNameBySiteUid(final String siteUid)
	{
		return siteUid + PATTERN_GROUP;
	}

	public FlexibleSearchService getSearchService()
	{
		return searchService;
	}

	@Required
	public void setSearchService(final FlexibleSearchService searchService)
	{
		this.searchService = searchService;
	}
}
