/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.liveeditaddon.cockpit.service;

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;

/**
 * Created by fcanteloup on 25/09/14.
 */
public interface CMSLockingService {


    boolean isSectionLocked(LiveEditView view, String slotId) throws InterruptedException;
}
