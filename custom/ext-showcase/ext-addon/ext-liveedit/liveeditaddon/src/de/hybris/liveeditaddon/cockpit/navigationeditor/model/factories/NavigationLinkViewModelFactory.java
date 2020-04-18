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
package de.hybris.liveeditaddon.cockpit.navigationeditor.model.factories;

import de.hybris.liveeditaddon.enums.CMSMenuItemType;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;


/**
 * 
 */
public class NavigationLinkViewModelFactory
{
	public static NavigationLinkViewModel create(final String navLinkName, final String langIso, final String url,
			final CMSMenuItemType menuItemType)
	{
		final NavigationLinkViewModel navLink = new NavigationLinkViewModel(langIso);
		navLink.setMenuItemType(menuItemType);
		navLink.setName(langIso, navLinkName);
		navLink.setURL(url);
		return navLink;
	}
}
