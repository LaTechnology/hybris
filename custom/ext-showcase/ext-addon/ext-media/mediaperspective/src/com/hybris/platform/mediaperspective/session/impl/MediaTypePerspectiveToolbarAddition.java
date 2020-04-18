/**
 * 
 */
package com.hybris.platform.mediaperspective.session.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;

import com.hybris.platform.mediaperspective.media.impl.MimeTypeGroup;

import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

/**
 * @author mkostic
 *
 */
public class MediaTypePerspectiveToolbarAddition extends AbstractMediaPerspectiveToolbarAddition {


	@Override
	protected Collection<? extends BrowserFilter> addFilters() {
		List<BrowserFilter> filters = new ArrayList<>();
		FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT DISTINCT {" + MediaModel.MIME + "} FROM {" + MediaModel._TYPECODE + "}");
		query.setResultClassList(Arrays.asList(String.class) );
		final SearchResult<String> searchResult = getFlexibleSearchService().search(query);
		
		for (MimeTypeGroup mimeTypeGroup : getMimeTypeGroups()) {
			filters.add(new MimeTypeGroupBrowserFilterImpl(mimeTypeGroup.getIdentifier(), mimeTypeGroup.getMimeTypes()));
			
		}
		
		filters.add(new EmptyBrowserFilterImpl(Labels.getLabel("mediaperspective.mimetype.filter.all", "All MIME Types")));
		for (final String mime : searchResult.getResult())
		{
			if (!StringUtils.isEmpty(mime)) {
				filters.add(new MimeTypeBrowserFilterImpl(mime, mime));
			}
		}
		return filters;
	}
	
	@Override
	protected String getTooltipText() {
		return Labels.getLabel("mediaperspective.mimetype.filter.tooltip");
	}
	
	@SuppressWarnings("unchecked")
	private List<MimeTypeGroup> getMimeTypeGroups() {
		return (List<MimeTypeGroup>) SpringUtil.getBean("mimeTypeGroups");
	}

}
