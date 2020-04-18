/**
 * 
 */
package com.hybris.platform.mediaperspective.session.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UINavigationArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


/**
 * @author wojciech.piotrowiak
 * 
 */

public class MediaPerspectiveSection extends DefaultSectionSelectorSection
{
	private CatalogService catalogService;


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
	 * Informs about chosen CMSSite
	 */
	protected void informCmsAboutCurrentSiteChanged()
	{
		CatalogModel catalogModel = null;
		final Object object = this.getRelatedObject() == null ? null : this.getRelatedObject().getObject();
		if (object instanceof CatalogModel)
		{
			catalogModel = (CatalogModel) object;
		}
		final UINavigationArea navigationArea = getNavigationAreaModel().getNavigationArea();
		final UIBrowserArea browserArea = navigationArea.getPerspective().getBrowserArea();
		final BrowserModel browserModel = browserArea.getFocusedBrowser();
		if (browserModel instanceof MediaPerspectiveSearchBrowserModel)
		{
			final MediaPerspectiveSearchBrowserModel liveBrowserModel = (MediaPerspectiveSearchBrowserModel) browserModel;
			liveBrowserModel.setSelectedCatalogVersion(null);
			liveBrowserModel.setSelectedCatalog(catalogModel);
		}


	}



	@Override
	public List<TypedObject> getItems()
	{
		{
			// wrap site items
			final List<CatalogModel> catalogs = new LinkedList<CatalogModel>();

			for (final CatalogModel cm : catalogService.getAllCatalogs())
			{
				if ((!(cm instanceof ClassificationSystemModel)))
				{

					catalogs.add(cm);
				}
			}

			Collections.sort(catalogs, new Comparator<CatalogModel>()
			{

				@Override
				public int compare(final CatalogModel o1, final CatalogModel o2)
				{
					if (o1.getId().equals("assets"))
					{
						return -1;
					}
					return o1.getId().compareTo(o2.getId());
				}
			});

			setItems(UISessionUtils.getCurrentSession().getTypeService().wrapItems(catalogs));

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
			if (cmsNavigationEvent.getSite() != null)
			{
				//exit when comes from another perspective!
				if (!event.getSource().equals(getNavigationAreaModel().getNavigationArea().getPerspective()))
				{
					return;
				}
				this.setRelatedObject(getTypeService().wrapItem(cmsNavigationEvent.getSite()));
				setSelectedItem(getTypeService().wrapItem(cmsNavigationEvent.getSite()));

				// send event to the subsections
				for (final Section section : this.getSubSections())
				{
					if (section instanceof CockpitEventAcceptor)
					{
						final CockpitEventAcceptor sectionAcceptor = (CockpitEventAcceptor) section;
						if (cmsNavigationEvent.getCatalog() != null)
						{
							sectionAcceptor.onCockpitEvent(event);
						}
					}
				}
			}
		}
		for (final Section section : this.getSubSections())
		{
			if (section instanceof CockpitEventAcceptor)
			{
				final CockpitEventAcceptor sectionAcceptor = (CockpitEventAcceptor) section;
				{
					sectionAcceptor.onCockpitEvent(event);
				}
			}
		}
	}



	public void setCatalogService(final CatalogService cs)
	{
		this.catalogService = cs;
	}

	protected TypeService getTypeService()
	{
		return UISessionUtils.getCurrentSession().getTypeService();
	}
}
