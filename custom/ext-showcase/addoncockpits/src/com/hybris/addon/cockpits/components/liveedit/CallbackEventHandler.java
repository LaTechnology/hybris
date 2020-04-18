/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 * 
 */
package com.hybris.addon.cockpits.components.liveedit;

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;


/**
 * @author rmcotton
 * 
 */
public interface CallbackEventHandler<V extends LiveEditView>
{
	String getEventId();

	void onCallbackEvent(final V view, final String passedAttributes[]) throws Exception;
}
