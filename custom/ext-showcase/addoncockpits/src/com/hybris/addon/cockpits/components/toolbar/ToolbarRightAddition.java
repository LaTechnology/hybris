/**
 * 
 */
package com.hybris.addon.cockpits.components.toolbar;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractMultiViewToolbarBrowserComponent;
import de.hybris.platform.cockpit.session.SearchBrowserModel;

import org.zkoss.zk.ui.Component;


/**
 * It should be state less.
 * 
 * @author miroslaw.szot
 */
public interface ToolbarRightAddition
{
	Component getContent(SearchBrowserModel model, AbstractMultiViewToolbarBrowserComponent toolbarBrowserComponent);
}
