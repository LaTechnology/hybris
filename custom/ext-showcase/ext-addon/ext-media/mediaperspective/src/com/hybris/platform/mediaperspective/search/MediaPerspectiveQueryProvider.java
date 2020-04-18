/**
 * 
 */
package com.hybris.platform.mediaperspective.search;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.services.search.impl.GenericQuerySearchProvider;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author tmerven
 * 
 */
public class MediaPerspectiveQueryProvider extends GenericQuerySearchProvider
{
	@Override
	public List createConditions(final Query query, final GenericQuery genQuery)
	{
		final List conditions = new ArrayList();
		conditions.addAll(super.createConditions(query, genQuery));

		final CatalogVersionModel selectedVersion = (CatalogVersionModel) query.getContextParameter("selectedCatalogVersion");
		if (selectedVersion != null)
		{
			conditions.add(createCatalogConditions(selectedVersion));
		}
		createMediaFolderCondition();
		return conditions;

	}

	protected GenericCondition createCatalogConditions(final CatalogVersionModel selectedVersion)
	{
		if (selectedVersion != null)
		{
			final List<PK> args = new ArrayList<PK>(1);
			args.add(selectedVersion.getPk());

			return GenericCondition.createConditionForValueComparison(new GenericSearchField("catalogVersion"), Operator.IN, args);
		}
		return null;
	}

	protected GenericCondition createMediaFolderCondition()
	{
		final MediaService mediaService = (MediaService) Registry.getApplicationContext().getBean("mediaService");
		final MediaFolderModel imagesFolder = mediaService.getFolder("images");
		return GenericCondition.createConditionForValueComparison(new GenericSearchField("folder"), Operator.EQUAL,
				imagesFolder.getPk());
	}
}
