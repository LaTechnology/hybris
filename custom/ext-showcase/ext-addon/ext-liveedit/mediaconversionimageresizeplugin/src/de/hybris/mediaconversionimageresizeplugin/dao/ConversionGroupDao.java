package de.hybris.mediaconversionimageresizeplugin.dao;

import de.hybris.platform.mediaconversion.model.ConversionGroupModel;


/**
 * author: dariusz.malachowski
 */
public interface ConversionGroupDao
{

	public ConversionGroupModel getConvertionGroupBySite(final String siteUid);

	public ConversionGroupModel getConvertionGroupByCode(final String code);

	public ConversionGroupModel getDefaultConvertionGroup();

}
