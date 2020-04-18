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

import java.util.List;

import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkCollectionViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;


/**
 * Simple factory for NavigationLinkCollectionViewModel type
 * 
 * 
 */
public class NavigationLinkCollectionViewModelFactory
{
	public static NavigationLinkCollectionViewModel create(final String navNodeName, final List<NavigationLinkViewModel> navLinks)
	{
		final NavigationLinkCollectionViewModel navLinkCollectionModel = new NavigationLinkCollectionViewModel();
		navLinkCollectionModel.setName(navNodeName);
		navLinkCollectionModel.setNavLinks(navLinks);
		return navLinkCollectionModel;
	}
}
