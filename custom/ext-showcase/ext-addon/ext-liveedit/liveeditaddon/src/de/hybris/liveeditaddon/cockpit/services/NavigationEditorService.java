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
package de.hybris.liveeditaddon.cockpit.services;

import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.NavigationParentElement;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.enumeration.EnumerationService;

import java.util.Collection;
import java.util.List;

import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationEditorViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationNodeTabViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationNodeViewModel;



public interface NavigationEditorService
{

	public Collection<NavigationNodeTabViewModel> getNodesForCurrentVersion(final NavigationEditorViewModel model,
			final NavigationParentElement parentElement);

	public CatalogVersionModel getCurrentContentCatalogVersion();

	public Collection<TypedObject> saveModels(final Collection<NavigationNodeTabViewModel> navigations, NavigationParentElement parentElement);

	void removeNavigation(NavigationNodeTabViewModel model);

	public Collection<NavigationNodeTabViewModel> insertBefore(NavigationNodeTabViewModel draggedModel,
			NavigationNodeTabViewModel targetModel, NavigationParentElement parentElement, Collection<NavigationNodeTabViewModel> navigations);

	public boolean isSynchronizePossible(Collection<TypedObject> itemsToSynchronize);

	public void performSynchronization(Collection<TypedObject> itemsToSynchronize);

	public MediaModel createMedia(NavigationNodeViewModel navigationNode, byte[] bytes);

	public Collection<CategoryModel> getRootCategories();

	public EnumerationService getEnumerationService();

	public List<NavigationLinkViewModel> getNavigationLinksForContetPagesFromCurrentCatalog(final LiveEditViewModel model);

	public ContentSlotModel getContentSlotForPreviewCatalogVersions(final String slotUid, final LiveEditViewModel model);

	public ContentSlotModel getNavigationBarContentSlot(final String uid, final LiveEditViewModel model);

	public AbstractCMSComponentModel getComponentForPreviewCatalogVersions(final String componentUid, final LiveEditViewModel model);

}
