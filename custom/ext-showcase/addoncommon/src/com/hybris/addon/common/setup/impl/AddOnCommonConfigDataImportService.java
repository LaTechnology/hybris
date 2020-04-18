package com.hybris.addon.common.setup.impl;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;

import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.addonsupport.setup.impl.DefaultAddOnConfigDataImportService;

public class AddOnCommonConfigDataImportService extends DefaultAddOnConfigDataImportService {

	@Override
	protected boolean executeCMSImport(final String extensionName, final ImpexMacroParameterData macroParameters)
	{
		final String path = "/" + extensionName + "/import/contentCatalogs/template/";
		boolean imported = false;
		imported |= getSetupImpexService().importImpexFile(path + "catalog.impex", macroParameters, false, false);
		
		for (UiExperienceLevel level : macroParameters.getSupportedUiExperienceLevels())
		{
			String suffix = StringUtils.EMPTY;
			if (!level.equals(UiExperienceLevel.DESKTOP))
			{
				suffix = "-" + StringUtils.lowerCase(level.getCode());
			}
			
			imported |= getSetupImpexService().importImpexFile(path + "cms-content" + suffix + ".impex", macroParameters, false, false);
		}
		
		imported |= getSetupImpexService().importImpexFile(path + "email-content.impex", macroParameters, false, false);
		
		return imported;
	}

	@Override
	protected boolean executeSOLRImport(String extensionName, ImpexMacroParameterData macroParameters) {
		final String path = "/" + extensionName + "/import/solr/template/";
		boolean imported = false;
		if (macroParameters.getSolrIndexedType() != null) {
			imported |= getSetupImpexService().importImpexFile(path + "solr.impex", macroParameters, false, false);
			imported |= getSetupImpexService().importImpexFile(path + "solrtrigger.impex", macroParameters, false, false);
		}
		return imported;
	}
	
}
