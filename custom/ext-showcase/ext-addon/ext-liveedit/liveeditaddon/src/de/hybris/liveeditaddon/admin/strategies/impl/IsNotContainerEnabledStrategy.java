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
package de.hybris.liveeditaddon.admin.strategies.impl;

import de.hybris.liveeditaddon.admin.ComponentActionMenuRequestData;

/**
 */
public class IsNotContainerEnabledStrategy extends IsContainerEnabledStrategy {

    @Override
    public boolean isEnabled(final ComponentActionMenuRequestData request)
    {
        return !isContainer(request);
    }

}
