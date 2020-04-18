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
package de.hybris.liveeditaddon.cockpit.navigationbargenerator;

import de.hybris.liveeditaddon.cockpit.navigationbargenerator.impl.OutputResult;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.util.Collection;
import java.util.Set;


/**
 * Created with IntelliJ IDEA. User: jacek.hrominski Date: 18.08.13 Time: 08:57 To change this template use File |
 * Settings | File Templates.
 */
public interface NavigationBarGenerator
{
	OutputResult runGeneratorForCatalog(String productCatalogId, String productCatalogVersionName, String contentCatalogId,
                                        String contentCatalogVersionName, Integer level, String[] rootCategories, boolean shouldHaveProducts,
                                        Set<String> localizations);

	OutputResult runGeneratorForCatalog(String productCatalogId, String productCatalogVersionName, String contentCatalogId,
                                        String contentCatalogVersionName, Integer level, boolean shouldHaveProducts, Set<String> localizations);

	Set<String> generateInnerJoinOfLanguages(Collection<LanguageModel> firstLanguagesCollection,
                                             Collection<LanguageModel> secondLanguagesCollection);
}
