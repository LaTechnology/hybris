/**
 * 
 */
package com.hybris.platform.mediaperspective.session.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.hybris.platform.mediaperspective.session.PerspectiveToolbarAddition;

import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * Parent class for different list-like toolbar additions. It uses a combobox component.
 * 
 * @author mkostic
 *
 */
public abstract class AbstractMediaPerspectiveToolbarAddition implements
		PerspectiveToolbarAddition {
	
	private Combobox component;
	private List<BrowserFilter> filters;

	@Override
	public Component getContent(final SearchBrowserModel searchBrowserModel) {
		component = new Combobox();
		component.setSclass(getClass().getSimpleName() + "_combobox");
		component.setTooltiptext(getTooltipText());
		component.setReadonly(true);

		if (getFilters() != null && !getFilters().isEmpty())
		{
			for (final BrowserFilter value : filters)
			{
				component.appendChild(new Comboitem(value.getLabel()));
			}
			component.setSelectedIndex(0);
		}

		UITools.addBusyListener(component, "onChange", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				final Comboitem selectedItem = ((Combobox) event.getTarget()).getSelectedItem();
				final BrowserFilter value = selectedItem != null ? (BrowserFilter) selectedItem.getValue() : null;
				searchBrowserModel.setBrowserFilter(value);
				searchBrowserModel.updateItems(0);
			}
		}, null, "general.updating.busy");
		return component;
	}

	protected String getTooltipText() {
		return null;
	}

	private List<BrowserFilter> getFilters() {
		if (filters == null)
		{
			filters = new ArrayList<BrowserFilter>();
		}
		else
		{
			filters.clear();
		}
		filters.addAll(addFilters());
		return filters;
	}

	protected abstract Collection<? extends BrowserFilter> addFilters();

	@Override
	public BrowserFilter getSelectedFilter() {
		if (component != null && component.getSelectedIndex() >= 0)
		{
			return filters.get(component.getSelectedIndex());
		}
		else if (filters != null && !filters.isEmpty())
		{
			return filters.get(0);
		}
		return null;
	}
	
	protected FlexibleSearchService getFlexibleSearchService() {
		return (FlexibleSearchService) SpringUtil.getBean("flexibleSearchService");
	}


}
