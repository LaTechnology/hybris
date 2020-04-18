/**
 * 
 */
package com.hybris.platform.mediaperspective.session.impl;

import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstracttSectionSelectorSectionListRenderer;
import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultSectionSelectorSectionRenderer;

import org.zkoss.zul.ListitemRenderer;


/**
 * @author wojciech.piotrowiak
 * 
 */

public class MediaPerspectiveSectionSelectorRenderer extends DefaultSectionSelectorSectionRenderer
{
	private CatalogVersionListRenderer listItemRenderer = null;

	@Override
	public ListitemRenderer getListRenderer()
	{
		if (listItemRenderer == null)
		{
			this.listItemRenderer = new CatalogVersionListRenderer();
		}
		return this.listItemRenderer;
	}

	private class CatalogVersionListRenderer extends AbstracttSectionSelectorSectionListRenderer
	{

		@Override
		public DefaultSectionSelectorSectionRenderer getSectionRenderer()
		{
			return MediaPerspectiveSectionSelectorRenderer.this;
		}

		@Override
		protected String getListcellSclass()
		{
			return "activeSiteNavigationEntry";
		}

		@Override
		protected String getListitemSclass()
		{
			return "siteNavigationParentEntry";
		}

	}

}
