package com.hybris.addon.common.setup.impl;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.addonsupport.setup.impl.DefaultSetupImpexAddOnService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

/**
 * Provides facility to support Legacy Mode import by default which bypasses SL to improve impex import performance. Using this feature will bypass SL validation however.
 * @author rmcotton
 *
 */
public class LegacyModeSwitchableSetupImpexAddOnService extends DefaultSetupImpexAddOnService {
private ConfigurationService configurationService;
	
	
	public boolean isLegacyMode()
	{
		return getConfigurationService().getConfiguration().getBoolean("setupimpexservice.legacymode.default",Boolean.FALSE);
	}
	
	@Override
	public void importImpexFile(final String file, final boolean errorIfMissing)
	{
		importImpexFile(file, errorIfMissing, isLegacyMode());
	}


	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	@Required
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}
