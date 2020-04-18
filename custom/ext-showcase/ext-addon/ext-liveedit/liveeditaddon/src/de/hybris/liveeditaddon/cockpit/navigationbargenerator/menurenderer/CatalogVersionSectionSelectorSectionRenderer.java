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
package de.hybris.liveeditaddon.cockpit.navigationbargenerator.menurenderer;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstracttSectionSelectorSectionListRenderer;
import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultSectionSelectorSectionRenderer;
import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.components.sync.dialog.ManySourceManyTargetVersionSyncDialog;
import de.hybris.platform.cockpit.components.sync.dialog.OneSourceManyTargetVersionSyncDialog;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.impl.DefaultWizardContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.A;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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

import de.hybris.liveeditaddon.cockpit.navigationbargenerator.wizard.DefaultNavigationGeneratorWizard;


/**
 * Renderer for catalog versions selector sections
 * <p/>
 * <b>Note:</b> This is used for both perspective (<b>WCMS Page Perspective</b> and <b>Live Edit Perspective</b>)
 * 
 * @see de.hybris.platform.cmscockpit.components.sectionpanel.CatalogVersionSectionSelectorSection
 * @see de.hybris.platform.cmscockpit.components.sectionpanel.LiveCatalogVersionSectionSelectorSection
 * 
 * 
 */


public class CatalogVersionSectionSelectorSectionRenderer extends
		de.hybris.platform.cmscockpit.components.sectionpanel.renderer.CatalogVersionSectionSelectorSectionRenderer
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CatalogVersionSectionSelectorSectionRenderer.class.getName());

	private CatalogVersionSelectorListRenderer listItemRenderer = null;

	private CMSAdminSiteService cmsAdminSiteService;

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
			A htmlLink = null;
			final Object currentParentObject = getSection().getParentSection().getRelatedObject().getObject();
			if (currentParentObject instanceof CMSSiteModel)
			{
				final CMSSiteModel currentSiteModel = (CMSSiteModel) currentParentObject;
				getCmsAdminSiteService().setActiveSite(currentSiteModel);
				htmlLink = new A();
				htmlLink.setDynamicProperty(TITLE, Labels.getLabel("liveedit.launch"));

				final String sitePk = currentSiteModel.getPk().toString();
				final String catalogPk = ((CatalogVersionModel) value.getObject()).getPk().toString();
				htmlLink.addEventListener(Events.ON_CLICK, new EventListener()
				{
					@Override
					public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
					{
						final StringBuilder urlBuilder = new StringBuilder();
						urlBuilder.append("?").append(PERSP_TAG);
						urlBuilder.append("=").append(LIVE_EDIT_PERSPECTIVE_ID);
						urlBuilder.append("&").append(EVENTS_TAG);
						urlBuilder.append("=").append(CMS_NAVIGATION_EVENT);
						urlBuilder.append("&").append(CMS_NAV_SITE);
						urlBuilder.append("=").append(sitePk);
						urlBuilder.append("&").append(CMS_NAV_CATALOG);
						urlBuilder.append("=").append(catalogPk);

						Executions.getCurrent().sendRedirect(urlBuilder.toString());

					}
				});
				final String currentPrespectiveUid = UISessionUtils.getCurrentSession().getCurrentPerspective().getUid();
				if (StringUtils.isNotBlank(currentPrespectiveUid))
				{
					final String cssStyleName = currentPrespectiveUid.replace(".", "-") + "-selector-section";
					parent.setSclass(cssStyleName);
				}
				htmlLink.setSclass(CATALOG_VERSION_SELECTOR_CAPTION_BUTTON);
				parent.appendChild(htmlLink);
				return htmlLink;

			}
			else
			{
				LOG.warn("Cannot find proper CMSSite object!");
			}
			return htmlLink;
		}


		@Override
		public void createContextMenu(final HtmlBasedComponent parent, final TypedObject value)
		{
			final Menupopup ctxMenu = new Menupopup();
			final Menuitem syncCatalogMenuItem = new Menuitem(Labels.getLabel("sync.content.catalog"));

			final CatalogVersionModel version = (CatalogVersionModel) value.getObject();
			final Collection<CatalogVersionModel> selectedVersions = Collections.singletonList(version);
			final boolean hasMultipleRules = getSynchronizationService().hasMultipleRules(selectedVersions);

			if (hasMultipleRules)
			{
				syncCatalogMenuItem.setParent(ctxMenu);
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
					syncCatalogMenuItem.setParent(ctxMenu);
				}
			}

			//menuItem.addEventListener(Events.ON_CLICK, new EventListener()
			UITools.addBusyListener(syncCatalogMenuItem, Events.ON_CLICK, new EventListener()
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

			/**
			 * Add generate navigation section
			 */
			//"Generate navigation"
//			final Menuitem generateNavigationMenuItem = new Menuitem(Labels.getLabel("nav.generator.wizard.title"));
//			generateNavigationMenuItem.addEventListener(Events.ON_CLICK, new EventListener()
//			{
//
//				@Override
//				public void onEvent(final Event event) throws Exception
//				{
//					final DefaultWizardContext wizardCtx = new DefaultWizardContext();
//					wizardCtx.setAttribute(DefaultNavigationGeneratorWizard.WIZARD_CONTEXT_CATALOG_VERSION, version);
//					wizardCtx.setAttribute(DefaultNavigationGeneratorWizard.WIZARD_CONTEXT_PRODUCT_CATALOG_VERSION,
//							getCurrentStagedProductCatalogVersion());
//					Wizard.show("generateNavigationWizard", wizardCtx);
//				}
//			});
//			generateNavigationMenuItem.setParent(ctxMenu);

			parent.appendChild(ctxMenu);
			parent.addEventListener(Events.ON_RIGHT_CLICK, new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
				{
					ctxMenu.open(parent);
				}
			});

		}

		private CatalogVersionModel getCurrentStagedProductCatalogVersion()
		{
			final Object currentParentObject = getSection().getParentSection().getRelatedObject().getObject();
			if (currentParentObject instanceof CMSSiteModel)
			{
				final CMSSiteModel currentSiteModel = (CMSSiteModel) currentParentObject;
				if (!currentSiteModel.getProductCatalogs().isEmpty())
				{
					for (final CatalogVersionModel cv : currentSiteModel.getProductCatalogs().get(0).getCatalogVersions())
					{
						if (cv.getVersion().equals("Staged"))
						{
							return cv;
						}
					}
				}
			}
			return null;
		}


		@Override
		public DefaultSectionSelectorSectionRenderer getSectionRenderer()
		{
			return CatalogVersionSectionSelectorSectionRenderer.this;
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


	public CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}
}
