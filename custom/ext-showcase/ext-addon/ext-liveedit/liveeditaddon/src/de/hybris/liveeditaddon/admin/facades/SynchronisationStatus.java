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
package de.hybris.liveeditaddon.admin.facades;

import de.hybris.platform.cockpit.services.sync.SynchronizationService;

public enum SynchronisationStatus {

    SYNCHRONIZATION_OK(SynchronizationService.SYNCHRONIZATION_OK), //green status
    SYNCHRONIZATION_NOT_AVAILABLE(SynchronizationService.SYNCHRONIZATION_NOT_AVAILABLE), //without image
    SYNCHRONIZATION_NOT_OK(SynchronizationService.SYNCHRONIZATION_NOT_OK), //red status
    INITIAL_SYNC_IS_NEEDED(SynchronizationService.INITIAL_SYNC_IS_NEEDED); //gray status

    private int statusValue;

    public int getStatusValue() {
        return statusValue;
    }

    private SynchronisationStatus(int statusValue) {
        this.statusValue = statusValue;
    }

    public static SynchronisationStatus getStatus(int statusValue) {
        for (SynchronisationStatus ss : SynchronisationStatus.values()) {
            if (ss.statusValue == statusValue) {
                return ss;
            }
        }
        return null;
    }

}
