/**
 * 
 */
package com.hybris.addon.cockpits.session;

import de.hybris.platform.cockpit.session.BrowserFilter;


/**
 * @author miroslaw.szot
 * 
 */
public interface MultipleBrowserFilterEnabled
{
	void enableFilter(BrowserFilter filter);

	void disableFilter(BrowserFilter filter);
}
