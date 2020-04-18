package de.hybris.mediaconversionimageresizeplugin.service;

import de.hybris.platform.mediaconversion.model.ConversionGroupModel;


/**
 * author: dariusz.malachowski
 */
public interface ConversionGroupService
{

	public ConversionGroupModel getConvertionGroupForSite(final String siteUid);

	public ConversionGroupModel getConvertionGroup(final String code);

	public ConversionGroupModel getDefaultConvertionGroup();

}
