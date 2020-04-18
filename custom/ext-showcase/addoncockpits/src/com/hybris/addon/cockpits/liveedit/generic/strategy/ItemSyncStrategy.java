package com.hybris.addon.cockpits.liveedit.generic.strategy;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;

import java.util.Collection;


/**
 * Item Sync Strategy Interface
 *
 * @author aandone
 *
 */
public interface ItemSyncStrategy
{

	/**
	 * Synchronize items list for a specific catalog version.
	 *
	 * @param catalogVersion
	 *           catalog version
	 * @param items
	 *           array list of items to be synchronized
	 * @return collection of synchronized items
	 */
	public Collection<TypedObject> synchronize(final CatalogVersionModel catalogVersion, final TypedObject... items);

	/**
	 * Retrieves sync status.
	 *
	 * @param items
	 *           items to be checked
	 * @return SYNCHRONIZATION_OK = 0 - green status or SYNCHRONIZATION_NOT_OK = 1 - red status
	 */
	public int getSyncStatus(final TypedObject... items);
}
