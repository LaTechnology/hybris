/**
 * 
 */
package com.hybris.platform.mediaperspective.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cmscockpit.components.sectionpanel.renderer.CatalogVersionSectionSelectorSectionRenderer;
import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstracttSectionSelectorSectionListRenderer;
import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultSectionSelectorSectionRenderer;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sync.dialog.ManySourceManyTargetVersionSyncDialog;
import de.hybris.platform.cockpit.components.sync.dialog.OneSourceManyTargetVersionSyncDialog;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.UITools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;


/**
 * @author wojciech.piotrowiak
 * 
 */
public class MediaCatalogVersionSectionSelectorSectionRenderer extends DefaultSectionSelectorSectionRenderer
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CatalogVersionSectionSelectorSectionRenderer.class.getName());

	private SynchronizationService synchronizationService;

	protected static final String CATALOG_VERSION_SELECTOR_CAPTION_BUTTON = "catalogVersionSelectorCaptionButton";
	protected static final String PERSP_TAG = "persp";
	protected static final String EVENTS_TAG = "events";
	protected static final String LIVE_EDIT_PERSPECTIVE_ID = "cmscockpit.perspective.liveedit";
	protected static final String CMS_NAVIGATION_EVENT = "cmsnavigation";
	protected static final String CMS_NAV_SITE = "nav-site";
	protected static final String CMS_NAV_CATALOG = "nav-catalog";
	protected static final String TITLE = "title";

	private CatalogVersionSelectorListRenderer listItemRenderer = null;

	@Override
	public ListitemRenderer getListRenderer()
	{
		if (listItemRenderer == null)
		{
			this.listItemRenderer = new CatalogVersionSelectorListRenderer();
		}
		return this.listItemRenderer;
	}



	public class CatalogVersionSelectorListRenderer extends AbstracttSectionSelectorSectionListRenderer
	{

		@Override
		protected String getListcellSclass()
		{
			return "activeCatalogVersionNavigationEntry";
		}

		@Override
		protected String getListitemSclass()
		{
			return "catalogVersionNavigationParentEntry";
		}



		@Override
		protected Component loadCaptionComponent(final HtmlBasedComponent parent, final TypedObject value)
		{

			final String currentPrespectiveUid = UISessionUtils.getCurrentSession().getCurrentPerspective().getUid();
			if (StringUtils.isNotBlank(currentPrespectiveUid))
			{
				final String cssStyleName = currentPrespectiveUid.replace(".", "-") + "-selector-section";
				parent.setSclass(cssStyleName);
			}
			return parent;

		}



		@Override
		public void createContextMenu(final HtmlBasedComponent parent, final TypedObject value)
		{
			final Menupopup ctxMenu = new Menupopup();
			final Menuitem menuItem = new Menuitem(Labels.getLabel("sync.content.catalog"));

			final CatalogVersionModel version = (CatalogVersionModel) value.getObject();
			final Collection<CatalogVersionModel> selectedVersions = Collections.singletonList(version);
			final boolean hasMultipleRules = getSynchronizationService().hasMultipleRules(selectedVersions);

			//menuItem.addEventListener(Events.ON_CLICK, new EventListener()
			UITools.addBusyListener(menuItem, Events.ON_CLICK, new EventListener()
			{
				@Override
				public void onEvent(final Event event)
				{
					final BaseUICockpitPerspective perspective = (BaseUICockpitPerspective) UISessionUtils.getCurrentSession()
							.getCurrentPerspective();

					if (hasMultipleRules)
					{
						if (selectedVersions.size() > 1)
						{
							final Map<String, String>[] rules = getSynchronizationService().getAllSynchronizationRules(selectedVersions);
							final ManySourceManyTargetVersionSyncDialog dialog = new ManySourceManyTargetVersionSyncDialog(rules,
									selectedVersions)
							{

								@Override
								public void updateBackground(final List<String> chosenRules)
								{
									perspective.getBrowserArea().update();
									sendNotification(perspective, chosenRules);
								}
							};
							dialog.addEventListener(Events.ON_OPEN, new EventListener()
							{
								@Override
								public void onEvent(final Event dialogEvent) throws Exception //NOPMD: ZK Specific
								{
									if (dialogEvent instanceof OpenEvent)
									{
										if (!((OpenEvent) dialogEvent).isOpen())
										{
											dialog.detach();
										}
									}
								}
							});
							parent.appendChild(dialog);
							dialog.doHighlighted();
						}
						else if (selectedVersions.size() == 1)
						{
							final CatalogVersionModel selectedVersion = new ArrayList<CatalogVersionModel>(selectedVersions).get(0);
							final List<SyncItemJobModel>[] matrix = getSynchronizationService().getSyncJobs(selectedVersion, null);
							final OneSourceManyTargetVersionSyncDialog dialog = new OneSourceManyTargetVersionSyncDialog(
									selectedVersion, matrix)
							{

								@Override
								public void updateBackground(final List<String> chosenRules)
								{
									perspective.getBrowserArea().update();
									sendNotification(perspective, chosenRules);
								}
							};
							dialog.addEventListener(Events.ON_OPEN, new EventListener()
							{
								@Override
								public void onEvent(final Event dialogEvent) throws Exception //NOPMD: ZK Specific
								{
									if (dialogEvent instanceof OpenEvent)
									{
										if (!((OpenEvent) dialogEvent).isOpen())
										{
											dialog.detach();
										}
									}
								}
							});
							parent.appendChild(dialog);
							dialog.doHighlighted();
						}
					}
					else
					{
						try
						{

							Clients.showBusy(null, false);
							if (Messagebox.show(Labels.getLabel("sync.confirm"), Labels.getLabel("general.confirm"), Messagebox.YES
									+ Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES)
							{
								Clients.showBusy("busy.sync", true);

								getSynchronizationService().performCatalogVersionSynchronization(selectedVersions, null, null, null);
								final List<String> versions = new ArrayList<String>();
								versions.add(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(value));
								perspective.getBrowserArea().update();
								sendNotification(perspective, versions);
							}
						}
						catch (final InterruptedException e)
						{
							// Just continue...
						}
					}
				}
			}, null, "busy.sync");

			menuItem.setParent(ctxMenu);

			if (hasMultipleRules)
			{
				parent.appendChild(ctxMenu);
			}
			else
			{

				boolean canWrite = false;

				// check if version is a sync source, if not, retrieve its source
				final CatalogVersionModel sourceVersion = version;

				final List<SyncItemJobModel>[] syncJobs = getSynchronizationService().getSyncJobs(sourceVersion, null);
				canWrite = !syncJobs[0].isEmpty();

				if (canWrite)
				{
					parent.appendChild(ctxMenu);
				}
			}
			parent.addEventListener(Events.ON_RIGHT_CLICK, new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
				{
					ctxMenu.open(parent);
				}
			});

		}

		@Override
		public DefaultSectionSelectorSectionRenderer getSectionRenderer()
		{
			return MediaCatalogVersionSectionSelectorSectionRenderer.this;
		}


		private void sendNotification(final BaseUICockpitPerspective perspective, final List<String> chosenRules)
		{
			final StringBuilder detailInformation = new StringBuilder();
			for (final String chosenRule : chosenRules)
			{
				detailInformation.append(", " + chosenRule);
			}
			detailInformation.append(" ");
			final Notification notification = new Notification(Labels.getLabel("synchronization.finished.start")
					+ detailInformation.substring(1) + Labels.getLabel("synchronization.finished.end"));
			if (perspective.getNotifier() != null)
			{
				perspective.getNotifier().setNotification(notification);
			}
		}
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
	public void setSynchronizationService(final SynchronizationService synchronizationService)
	{
		this.synchronizationService = synchronizationService;
	}


}
