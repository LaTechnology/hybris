package de.hybris.mediaconversionimageresizeplugin.service.impl;

import de.hybris.mediaconversionimageresizeplugin.dao.ConversionGroupDao;
import de.hybris.mediaconversionimageresizeplugin.service.ConversionGroupService;
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import org.springframework.beans.factory.annotation.Required;
import static java.lang.String.format;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * author: dariusz.malachowski
 */
public class DefaultConversionGroupService implements ConversionGroupService
{
	private ConversionGroupDao conversionGroupDao;

	@Override
	public ConversionGroupModel getConvertionGroupForSite(final String siteUid)
	{
		validateParameterNotNull(siteUid, "Parameter siteUid must not be null");
		ConversionGroupModel conversionGroupModel = getConversionGroupDao().getConvertionGroupBySite(siteUid);
		validateParameterNotNull(conversionGroupModel, format("Conversion Group not found by given site uid: '%s'", siteUid));
		return conversionGroupModel;
	}

	@Override
	public ConversionGroupModel getConvertionGroup(final String code)
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		ConversionGroupModel conversionGroupModel = getConversionGroupDao().getConvertionGroupByCode(code);
		validateParameterNotNull(conversionGroupModel, format("Conversion Group not found by given code: '%s'", code));
		return conversionGroupModel;
	}

	@Override
	public ConversionGroupModel getDefaultConvertionGroup()
	{
		ConversionGroupModel conversionGroupModel = getConversionGroupDao().getDefaultConvertionGroup();
		validateParameterNotNull(conversionGroupModel, "Default Conversion Group not found");
		return conversionGroupModel;
	}

	public ConversionGroupDao getConversionGroupDao()
	{
		return conversionGroupDao;
	}

	@Required
	public void setConversionGroupDao(final ConversionGroupDao conversionGroupDao)
	{
		this.conversionGroupDao = conversionGroupDao;
	}
}
