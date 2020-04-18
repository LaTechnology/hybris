package com.hybris.addon.cockpits.liveedit.generic.strategy.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.addon.cockpits.liveedit.generic.strategy.ItemSyncStrategy;


/**
 * Default Sync Strategy
 *
 * @author aandone
 */
public class DefaultItemSyncStrategy implements ItemSyncStrategy
{

	private static final Logger LOG = Logger.getLogger(DefaultItemSyncStrategy.class);

	private SynchronizationService synchronizationService;

	@Override
	public Collection<TypedObject> synchronize(final CatalogVersionModel catalogVersion, final TypedObject... items)
	{
		int safetyNetLock = 15;
		final Set<TypedObject> affectedItems = new HashSet<TypedObject>();

		int syncStatus = SynchronizationService.SYNCHRONIZATION_NOT_OK;
		while (syncStatus == SynchronizationService.SYNCHRONIZATION_NOT_OK && safetyNetLock > 0)
		{
			affectedItems.addAll(syncItems(Arrays.asList(items)));
			safetyNetLock--;
			syncStatus = getSyncStatus(items);

		}
		if (syncStatus == SynchronizationService.SYNCHRONIZATION_NOT_OK)
		{
			LOG.error("unable to synchronize completely, sync status: " + syncStatus);
		}
		return affectedItems;
	}

	@Override
	public int getSyncStatus(final TypedObject... items)
	{
		for (final TypedObject relatedItem : Arrays.asList(items))
		{
			final int syncStatus = synchronizationService.isObjectSynchronized(relatedItem);
			if (syncStatus != SynchronizationService.SYNCHRONIZATION_OK)
			{
				return syncStatus;
			}
		}

		return SynchronizationService.SYNCHRONIZATION_OK;
	}

	protected Collection<TypedObject> syncItems(final List<TypedObject> list)
	{
		final SynchronizationService.SyncContext syncCtx = synchronizationService.getSyncContext(list.get(0));

		final List<SyncItemJobModel>[] matrixRules = syncCtx.getSyncJobs();
		final int size = matrixRules[0].size() + matrixRules[1].size();
		if (size > 1)
		{
			LOG.warn("only one source/one target syncs are supported atm");
		}
		else if (matrixRules[0].size() == 1 && matrixRules[1].size() == 0)
		{
			return synchronizationService.performSynchronization(list, null, null, null);
		}
		else
		{
			LOG.info("no synchronization rule available");
		}
		return Collections.emptyList();
	}

	/**
	 * @return the synchronizationService
	 */
	public SynchronizationService getSynchronizationService()
	{
		return synchronizationService;
	}

	/**
	 * @param synchronizationService
	 *           the synchronizationService to set
	 */
	@Required
	public void setSynchronizationService(final SynchronizationService synchronizationService)
	{
		this.synchronizationService = synchronizationService;
	}


}
