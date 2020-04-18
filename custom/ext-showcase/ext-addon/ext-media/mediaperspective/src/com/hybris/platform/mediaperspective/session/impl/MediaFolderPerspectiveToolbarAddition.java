/**
 * 
 */
package com.hybris.platform.mediaperspective.session.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.zkoss.util.resource.Labels;

import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.servicelayer.search.SearchResult;

/**
 * @author mkostic
 *
 */
public class MediaFolderPerspectiveToolbarAddition extends AbstractMediaPerspectiveToolbarAddition {

	@Override
	protected Collection<? extends BrowserFilter> addFilters() {
		List<BrowserFilter> filters = new ArrayList<>();
		final SearchResult<MediaFolderModel> searchResult = getFlexibleSearchService().search(
				"SELECT {" + MediaFolderModel.PK + "} FROM {" + MediaFolderModel._TYPECODE + "}");

		
		filters.add(new EmptyBrowserFilterImpl(Labels.getLabel("mediaperspective.folder.filter.all", "All Folders")));
		for (final MediaFolderModel mediaFolder : searchResult.getResult())
		{
			String qualifier = mediaFolder.getQualifier();
			filters.add(new MediaFolderBrowserFilterImpl(qualifier, mediaFolder));
		}
		return filters;
	}
	
	@Override
	protected String getTooltipText() {
		return Labels.getLabel("mediaperspective.folder.filter.tooltip");
	}

}
