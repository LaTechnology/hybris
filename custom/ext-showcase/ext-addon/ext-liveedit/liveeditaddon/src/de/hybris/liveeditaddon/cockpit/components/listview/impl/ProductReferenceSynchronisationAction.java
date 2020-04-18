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
package de.hybris.liveeditaddon.cockpit.components.listview.impl;

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 * 
 */

import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.components.listview.impl.AbstractSynchronizationAction;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService.SyncContext;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;



public class ProductReferenceSynchronisationAction extends AbstractSynchronizationAction
{
	private static final Logger LOG = Logger.getLogger(ProductReferenceSynchronisationAction.class);

	private SynchronizationService synchronizationService;

	protected TypedObject getProductFromContext(final Context context)
	{
		final ProductModel product = ((ProductReferenceModel) context.getItem().getObject()).getSource();
		return getTypeService().wrapItem(product);
	}


	@Override
	protected void doCreateContext(final Context context)
	{
		final SyncContext syncCtx = getSynchronizationService().getSyncContext(getProductFromContext(context));

		context.getMap().put(SYNC_JOBS_KEY, syncCtx.getSyncJobs());
		context.getMap().put(SOURCE_CATALOG_VERSION_KEY,
				getSynchronizationService().getCatalogVersionForItem(getProductFromContext(context)));
		context.getMap().put(CURRENT_STATUS_KEY, Integer.valueOf(syncCtx.isProductSynchronized()));
		context.getMap().put(AFFECTED_ITEMS_KEY, syncCtx.getAffectedItems());
	}

	@Override
	protected String getSyncInitImg()
	{
		return "cockpit/images/synchronization_init.gif";
	}


	@Override
	protected String getSyncNotOKImg()
	{
		return "cockpit/images/synchronization_notok.gif";
	}


	@Override
	protected String getSyncOKImg()
	{
		return "cockpit/images/synchronization_ok.gif";
	}




	@Override
	public EventListener getEventListener(final Context context)
	{
		return new EventListener()
		{
			private final EventListener listener = new EventListener()
			{
				public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
				{
					try
					{
						final List<SyncItemJobModel>[] matrixRules = getSyncJobs(context);
						final int size = matrixRules[0].size() + matrixRules[1].size();
						if (size > 1)
						{ //NOPMD
						  // only one source/one target syncs are supported atm
						}
						else if (matrixRules[0].size() == 1 && matrixRules[1].size() == 0)
						{
							getSynchronizationService().performSynchronization(
									Collections.singletonList(getProductFromContext(context)), null, null, null);
						}
					}
					catch (final Exception e)
					{
						LOG.error(e.getMessage(), e);
					}
					finally
					{
						Clients.showBusy(null, false);
						UISessionUtils.getCurrentSession().sendGlobalEvent(
								new ItemChangedEvent(ProductReferenceSynchronisationAction.this, context.getItem(), null));

						//update view after the staged item is synchronized
						final BaseUICockpitPerspective perspective = (BaseUICockpitPerspective) UISessionUtils.getCurrentSession()
								.getCurrentPerspective();
						perspective.getNavigationArea().update();
						for (final BrowserModel visBrowser : perspective.getBrowserArea().getVisibleBrowsers())
						{
							visBrowser.updateItems();
						}
					}
				}
			};

			public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
			{
				final Object status = context.getMap().get(CURRENT_STATUS_KEY);
				final boolean syncOK = (status instanceof Integer && ((Integer) status).intValue() == SynchronizationService.SYNCHRONIZATION_OK);

				if (!syncOK)
				{
					final Component target = event.getTarget();
					target.removeEventListener("onSync", this.listener);
					target.addEventListener("onSync", this.listener);

					if (target instanceof Image)
					{
						((Image) target).setSrc(getSyncBusyImg());
					}
					else
					{
						Clients.showBusy(Labels.getLabel("busy.sync"), true);
					}
					Events.echoEvent("onSync", event.getTarget(), null);
				}
			}
		};
	}

	@Override
	protected SynchronizationService getSynchronizationService()
	{
		return synchronizationService;
	}

	public void setSynchronizationService(final SynchronizationService synchronizationService)
	{
		this.synchronizationService = synchronizationService;
	}

}
