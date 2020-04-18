/**
 * 
 */
package com.hybris.platform.mediaperspective.session.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author wojciech.piotrowiak
 * 
 */
public class MediaPerspectiveSectionSelectorCatalogVersionRenderer extends DefaultSectionSelectorSection
{
	private static final Logger LOG = Logger.getLogger(MediaPerspectiveSectionSelectorCatalogVersionRenderer.class);
	private CMSAdminSiteService cmsAdminSiteService = null;


	@Override
	public void selectionChanged()
	{
		//try close editor area when changed
		closeEditorArea();

		informCmsAboutCurrentCatalogVersion();
		final UINavigationArea navigationArea = getNavigationAreaModel().getNavigationArea();
		final SectionPanelModel sectionPanelModel = navigationArea.getSectionModel();

		if (sectionPanelModel instanceof AbstractSectionPanelModel)
		{
			((AbstractSectionPanelModel) sectionPanelModel).sectionUpdated(getRootSection());
		}
	}

	@Override
	public List<TypedObject> getItems()
	{
		final TypedObject selectedSiteObject = getParentSection().getRelatedObject();
		final CatalogModel selectedCatalog = (CatalogModel) selectedSiteObject.getObject();
		final List<TypedObject> ret = new ArrayList<TypedObject>();
		if (selectedCatalog != null)
		{

			ret.addAll(UISessionUtils.getCurrentSession().getTypeService().wrapItems(selectedCatalog.getCatalogVersions()));
		}
		else
		{
			LOG.warn("It is not possible to retrieve content catalogs for empty stotre!");
		}
		if (ret.isEmpty())
		{
			setItems(Collections.EMPTY_LIST);
		}
		else
		{
			setItems(UISessionUtils.getCurrentSession().getTypeService().wrapItems(ret));
		}
		return super.getItems();
	}

	@Override
	public void onCockpitEvent(final CockpitEvent event)
	{
		super.onCockpitEvent(event);

		if (event instanceof CmsNavigationEvent)
		{
			final CmsNavigationEvent cmsNavigationEvent = (CmsNavigationEvent) event;

			if (cmsNavigationEvent.getCatalog() != null)
			{
				setOpen(true);
				setRelatedObject(getTypeService().wrapItem(cmsNavigationEvent.getCatalog()));
				setSelectedItem(getTypeService().wrapItem(cmsNavigationEvent.getCatalog()));

			}
		}
	}

	/**
	 * Checks whether something is activated within <b>Editor Area</b> if so then we deselect this value and close
	 * <b>Editor Area<b/>
	 */
	protected void closeEditorArea()
	{
		final TypedObject currentEditorObject = UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea()
				.getCurrentObject();
		if (currentEditorObject != null)
		{
			final UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
			currentPerspective.getEditorArea().setCurrentObject(null);
			((BaseUICockpitPerspective) currentPerspective).collapseEditorArea();
		}
	}

	/**
	 * Informs CMS about changes made by user
	 * <p/>
	 * <b>Note:</b><br/>
	 * Informs about chosen catalog version and correct CMSSite
	 */
	protected void informCmsAboutCurrentCatalogVersion()
	{
		CatalogVersionModel catalogVersionModel = null;
		final Object object = this.getRelatedObject() == null ? null : this.getRelatedObject().getObject();
		if (object instanceof CatalogVersionModel)
		{
			catalogVersionModel = (CatalogVersionModel) object;
		}

		final UINavigationArea navigationArea = getNavigationAreaModel().getNavigationArea();
		final UIBrowserArea browserArea = navigationArea.getPerspective().getBrowserArea();
		final BrowserModel browserModel = browserArea.getFocusedBrowser();
		if (browserModel instanceof MediaPerspectiveSearchBrowserModel)
		{
			((MediaPerspectiveSearchBrowserModel) browserModel).setSelectedCatalogVersion((CatalogVersionModel) getSelectedItem()
					.getObject());
		}
		getCmsAdminSiteService().setActiveCatalogVersion(catalogVersionModel);

		refreshView();
	}

	@Override
	public void setSelectedItem(final TypedObject selectedItem)
	{
		final List<TypedObject> items = new ArrayList<TypedObject>(getItems());
		if (items.contains(selectedItem)
				&& ((this.getSelectedItem() == null && selectedItem != null) || (this.getSelectedItem() != null && !this
						.getSelectedItem().equals(selectedItem))))
		{
			this.selectedItems.clear();
			this.selectedItems.add(selectedItem);
			this.selectionChanged();
		}
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService siteService)
	{
		this.cmsAdminSiteService = siteService;
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return this.cmsAdminSiteService;
	}

	protected TypeService getTypeService()
	{
		return UISessionUtils.getCurrentSession().getTypeService();
	}
}
