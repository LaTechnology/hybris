/**
 * 
 */
package com.hybris.platform.mediaperspective.session.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.zkoss.util.resource.Labels;

import com.hybris.mediatags.model.MediaTagModel;


import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.servicelayer.search.SearchResult;

/**
 * @author mkostic
 *
 */
public class MediaTagsPerspectiveToolbarAddition extends AbstractMediaPerspectiveToolbarAddition {


	@Override
	protected Collection<? extends BrowserFilter> addFilters() {
		final List<BrowserFilter> filters = new ArrayList<>();
		final SearchResult<MediaTagModel> searchResult = getFlexibleSearchService().search(
				"SELECT {" + MediaTagModel.PK + "} FROM {" + MediaTagModel._TYPECODE + "}");

		
		filters.add(new EmptyBrowserFilterImpl(Labels.getLabel("mediaperspective.tags.filter.all", "All Tags")));
		for (final MediaTagModel mediaTag : searchResult.getResult())
		{
			filters.add(new MediaTagBrowserFilterImpl(mediaTag.getCode(), mediaTag));
		}
		return filters;
	}
	
	@Override
	protected String getTooltipText() {
		return Labels.getLabel("mediaperspective.tags.filter.tooltip");
	}

}
