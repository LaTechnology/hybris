/**
 * 
 */
package com.hybris.addon.cockpits.components.liveedit;

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;


/**
 * 
 */
public interface LockAwareEventHandler<V extends LiveEditView>
{

	void onLockCallbackEvent(final V view, final String passedAttributes[]) throws Exception;

}
