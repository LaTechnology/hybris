/**
 *
 */
package com.hybris.addon.common.setup.impl;

import de.hybris.platform.addonsupport.setup.impl.DefaultAddonSampleDataImportService;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.core.initialization.SystemSetupContext;


/**
 * @author c.ursachi
 *
 */
public class AddOnCommonSampleDataImportService extends DefaultAddonSampleDataImportService
{
	@Override
	protected void importContentCatalog(final SystemSetupContext context, final String importDirectory, final String catalogName)
	{
		super.importContentCatalog(context, importDirectory, catalogName);

		final String importRoot = "/" + importDirectory + "/import";

		if (ResponsiveUtils.isResponsive())
		{
			logInfo(context, "Importing responsive cms sampledata for [" + catalogName + "]");
			importImpexFile(context, importRoot + "/contentCatalogs/" + catalogName + "ContentCatalog/cms-responsive-content.impex",
					false);
			logInfo(context, "Done importing responsive cms sampledata for [" + catalogName + "]");
		}

	}
}
