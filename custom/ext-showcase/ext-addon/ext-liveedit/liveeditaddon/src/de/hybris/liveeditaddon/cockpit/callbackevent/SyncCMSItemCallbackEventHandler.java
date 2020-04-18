/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.liveeditaddon.cockpit.callbackevent;

import static de.hybris.platform.cockpit.services.sync.SynchronizationService.INITIAL_SYNC_IS_NEEDED;
import static de.hybris.platform.cockpit.services.sync.SynchronizationService.SYNCHRONIZATION_NOT_AVAILABLE;
import static de.hybris.platform.cockpit.services.sync.SynchronizationService.SYNCHRONIZATION_NOT_OK;
import static de.hybris.platform.cockpit.services.sync.SynchronizationService.SYNCHRONIZATION_OK;

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.core.model.ItemModel;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;



/**
 * 
 */
public class SyncCMSItemCallbackEventHandler extends AbstractLiveEditCallbackEventHandler<LiveEditView>
{
	private SynchronizationService synchronizationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#getEventId()
	 */
	@Override
	public String getEventId()
	{
		return "syncItem";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#onCallbackEvent(de.hybris.platform.cmscockpit
	 * .components.liveedit.LiveEditView, java.lang.String[])
	 */
    @Override
    public void onCallbackEvent(final LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{
		final String uid = (String)attributeMap.get("item_uid");
		final ItemModel item = super.getCMSObjectForUid(uid, view);
		final int syncStatus = getSynchronizationService().isObjectSynchronized(getCockpitTypeService().wrapItem(item));
		if (syncStatus == INITIAL_SYNC_IS_NEEDED || syncStatus == SYNCHRONIZATION_NOT_OK)
		{
			getSynchronizationService().performSynchronization(Collections.singletonList(item), null, null, null);
			final int syncStatusAfterSync = getSynchronizationService().isObjectSynchronized(getCockpitTypeService().wrapItem(item));

			if (syncStatusAfterSync == SYNCHRONIZATION_OK)
			{
				view.update();
			}
			else
			{
				Messagebox.show(Labels.getLabel("dialog.itemNotSynchronized.message"),
						Labels.getLabel("dialog.synchronizationNotPerformed.title"), Messagebox.OK, Messagebox.EXCLAMATION);
			}
		}
		else if (syncStatus == SYNCHRONIZATION_NOT_AVAILABLE)
		{
			Messagebox.show(Labels.getLabel("dialog.synchronizationNotPossible.message"),
					Labels.getLabel("dialog.synchronizationNotPerformed.title"), Messagebox.OK, Messagebox.EXCLAMATION);

		}
		else if (syncStatus == SYNCHRONIZATION_OK)
		{
			Messagebox.show(Labels.getLabel("dialog.synchronizationNotRequired.message"),
					Labels.getLabel("dialog.synchronizationNotPerformed.title"), Messagebox.OK, Messagebox.INFORMATION);
		}
		else
		{
			throw new IllegalStateException("unexpected sync status [" + syncStatus + "]");
		}

	}

	public SynchronizationService getSynchronizationService()
	{
		return synchronizationService;
	}

	@Required
	public void setSynchronizationService(final SynchronizationService synchronizationService)
	{
		this.synchronizationService = synchronizationService;
	}



}
