/**
 * 
 */
package com.hybris.platform.mediaperspective.session;

import org.zkoss.zk.ui.Component;

import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.SearchBrowserModel;

/**
 * Interface for wrapping cockpit perspective's additional components used for filtering.
 * Created based on promotion cockpits similar interface, consider extracting both these extensions
 * into an extension that is shared across addons. 
 * 
 * @author mkostic
 *
 */
public interface PerspectiveToolbarAddition {
	
	/**
	 * Should return component instance for current object.
	 * 
	 * @return component that will be displayed on GUI.
	 */
	Component getContent(SearchBrowserModel searchBrowserModel);

	/**
	 * @return Value that is selected in component.
	 */
	BrowserFilter getSelectedFilter();

}
