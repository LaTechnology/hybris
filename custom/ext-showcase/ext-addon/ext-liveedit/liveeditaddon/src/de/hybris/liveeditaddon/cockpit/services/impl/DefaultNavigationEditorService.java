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
package de.hybris.liveeditaddon.cockpit.services.impl;

import de.hybris.platform.cmscockpit.cms.strategies.CounterpartProductCatalogVersionsStrategy;
import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.NavigationParentElement;
import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.impl.ComponentParentElement;
import de.hybris.liveeditaddon.cockpit.navigationeditor.elements.impl.SlotParentElement;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.*;
import de.hybris.liveeditaddon.cockpit.services.NavigationEditorService;
import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;
import de.hybris.liveeditaddon.enums.CMSMenuItemType;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSPageService;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.services.sync.impl.SynchronizationServiceImpl;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;


public class DefaultNavigationEditorService implements NavigationEditorService
{
	private CMSAdminSiteService cmsAdminSiteService;
	private BaseSiteService baseSiteService;
	private CMSComponentService cmsComponentService;
	private CMSContentSlotService cmsContentSlotService;
	private CatalogVersionService catalogService;
	private CMSNavigationService cmsNavigationService;
	private ModelService modelService;
	private UserService userService;
	private SessionService sessionService;
	private SynchronizationServiceImpl synchronizationService;
	private MediaService mediaService;
	private EnumerationService enumerationService;
	private CategoryService categoryService;
	private DefaultCMSPageService pageService;
	private CounterpartProductCatalogVersionsStrategy counterpartProductCatalogVersionsStrategy;
	private CommonI18NService commonI18NService;


	private final static Logger LOG = Logger.getLogger(DefaultNavigationEditorService.class);

	@Override
	public CatalogVersionModel getCurrentContentCatalogVersion()
	{
		return cmsAdminSiteService.getActiveCatalogVersion();
	}


	@Override
	public Collection<TypedObject> saveModels(final Collection<NavigationNodeTabViewModel> navigationModels, NavigationParentElement parentElement)
	{
		final Collection<TypedObject> typedObjects = new ArrayList<TypedObject>();
		for (final NavigationNodeTabViewModel tab : navigationModels)
		{
			final NavigationNodeViewModel node = tab.getNavigationNode();
			if (StringUtils.isNotBlank(node.getUid()))
			{
				editExistingNode(typedObjects, node);
			}
			else
			{
				createNewNode(typedObjects, node, parentElement);
			}
		}
		return typedObjects;
	}

	private void editExistingNode(final Collection<TypedObject> typedObjects, final NavigationNodeViewModel node)
	{


		final AbstractCMSComponentModel simpleCMSComponent = getComponentForPreviewCatalogVersions(node.getUid(),
				node.getLiveEditView());
		if (simpleCMSComponent instanceof NavigationBarComponentModel)
		{
			final NavigationBarComponentModel navBar = (NavigationBarComponentModel) simpleCMSComponent;
			//nav bar
			populateCMSNavBarFromModel(node, navBar, typedObjects);
			populateCMSLinkFromModelLink(navBar.getLink(), node, typedObjects);
			getModelService().save(navBar.getLink());


			final CMSNavigationNodeModel mainNavNode = navBar.getNavigationNode();

			if (mainNavNode != null)
			{
				//main node values
				mainNavNode.setName(node.getName());
				for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
				{
					mainNavNode.setTitle(node.getNames().get(iso), Locale.forLanguageTag(iso));
				}

				//columns
				final Iterator<NavigationColumnViewModel> iterator = node.getNavigationNodeColumns().iterator();
				while (iterator.hasNext())
				{
					final NavigationColumnViewModel column = iterator.next();
					if (column.isDeleted())
					{
						iterator.remove();
					}
					//new column
					if (StringUtils.isBlank(column.getNavNodeUID()))
					{
						final CMSNavigationNodeModel cmsNavigationNodeFromColumnViewModel = createNewCMSNavigationNodeFromColumnViewModel(
								column, typedObjects);
						column.setNavNodeUID(cmsNavigationNodeFromColumnViewModel.getUid());

						//add children
						final ArrayList<CMSNavigationNodeModel> navs = new ArrayList<CMSNavigationNodeModel>(mainNavNode.getChildren());
						navs.add(cmsNavigationNodeFromColumnViewModel);
						mainNavNode.setChildren(navs);

						getModelService().save(cmsNavigationNodeFromColumnViewModel);
						typedObjects.add(UISessionUtils.getCurrentSession().getTypeService()
								.wrapItem(cmsNavigationNodeFromColumnViewModel));
					}
					else
					{
						//edit existing nodes
						final Iterator<CMSNavigationNodeModel> cmsIterator = mainNavNode.getChildren().iterator();
						while (cmsIterator.hasNext())
						{
							final CMSNavigationNodeModel childNode = cmsIterator.next();
							if (childNode.getUid().equals(column.getNavNodeUID()))
							{
								if (column.isDeleted())
								{
									final List<CMSNavigationNodeModel> children = new ArrayList<CMSNavigationNodeModel>(
											mainNavNode.getChildren());
									children.remove(childNode);
									mainNavNode.setChildren(children);
								}

								populateCMSNavigationNode(column, childNode, typedObjects);
							}
						}
					}
				}

				getModelService().save(mainNavNode);
				typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(mainNavNode));
			}
			//needed to remove elements
			getModelService().save(node.getContentSlot());
			typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(node.getContentSlot()));
			getModelService().save(navBar);
			typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(navBar));

		}

	}


	private String getCurrentLanguageFromModel(final NavigationNodeViewModel node)
	{
		return node.getLiveEditView().getCurrentPreviewData().getLanguage().getIsocode();
	}

	protected void populateCMSNavBarFromModel(final NavigationNodeViewModel node, final NavigationBarComponentModel navBar,
			final Collection<TypedObject> typedObjects)
	{
		navBar.setName(node.getName());
		navBar.setWrapAfter(node.getWrapAfter());
		navBar.setStyleClass(node.getStyleClass());
		navBar.setRestrictions((List<AbstractRestrictionModel>) node.getRestrictions());
		typedObjects.addAll(UISessionUtils.getCurrentSession().getTypeService().wrapItems(node.getRestrictions()));
	}

	private void createNewNode(final Collection<TypedObject> typedObjects, final NavigationNodeViewModel node, NavigationParentElement parentElement)
	{
		if (StringUtils.isNotBlank(node.getName()))
		{
			final NavigationBarComponentModel newNavBar = createNavigationBar(node, typedObjects, parentElement);
			final CMSNavigationNodeModel newNavNode = newNavNodeForNavigationBar(node, typedObjects);

			newNavBar.setNavigationNode(newNavNode);

			final CMSLinkComponentModel newNavLink = createNavigationLinkForNavigationBar(node, typedObjects);
			newNavBar.setLink(newNavLink);
			newNavNode.setLinks(Collections.singletonList(newNavLink));


			final CMSNavigationNodeModel navigationNodeForId = appendNavNodeToParent(newNavNode, node.getLiveEditView());

			saveAllAndAddToSynchronizedCollection(node, typedObjects, newNavBar, newNavNode, newNavLink, navigationNodeForId, parentElement);

		}
	}

	private void saveAllAndAddToSynchronizedCollection(final NavigationNodeViewModel node,
			final Collection<TypedObject> typedObjects, final NavigationBarComponentModel newNavBar,
			final CMSNavigationNodeModel newNavNode, final CMSLinkComponentModel newNavLink,
			final CMSNavigationNodeModel navigationNodeForId, NavigationParentElement parentElement)
	{
		getModelService().save(newNavNode);
		typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(newNavNode));
		getModelService().save(navigationNodeForId);
		typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(navigationNodeForId));
		getModelService().save(parentElement.getElement());
		typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(parentElement.getElement()));

		getModelService().save(newNavLink);
		typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(newNavLink));
		getModelService().save(newNavBar);
        node.setUid(newNavBar.getUid());
		typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(newNavBar));


	}

	private CMSNavigationNodeModel appendNavNodeToParent(final CMSNavigationNodeModel newNavNode,
			final LiveEditViewModel liveEditView)
	{
		final CMSNavigationNodeModel navigationNodeForId = getNavigationNodeForPreviewCatalogVersions("SiteRootNode", liveEditView);
		final List<CMSNavigationNodeModel> navv = new ArrayList<CMSNavigationNodeModel>(navigationNodeForId.getChildren());
		navv.add(newNavNode);
		navigationNodeForId.setChildren(navv);
		return navigationNodeForId;
	}

	private CMSLinkComponentModel createNavigationLinkForNavigationBar(final NavigationNodeViewModel node,
			final Collection<TypedObject> typedObjects)
	{
		final CMSLinkComponentModel newNavLink = getModelService().create("CMSLinkComponent");
		newNavLink.setCatalogVersion(getCurrentContentCatalogVersion());
		newNavLink.setUid(node.getName() + "NAV_LINK_UID" + System.nanoTime());
		populateCMSLinkFromModelLink(newNavLink, node, typedObjects);
		return newNavLink;
	}

	private void populateCMSLinkFromModelLink(final CMSLinkComponentModel cmsLinkModel, final NavigationNodeViewModel node,
			final Collection<TypedObject> typedObjects)
	{
		cmsLinkModel.setName(node.getName());
		final NavigationLinkViewModel navBarLink = node.getNavBarLink();
		if (navBarLink != null)
		{
			cmsLinkModel.setUrl(navBarLink.getURL());
			cmsLinkModel.setCategory(navBarLink.getCategory());
			cmsLinkModel.setProduct(navBarLink.getProduct());
			cmsLinkModel.setContentPage(navBarLink.getPage());

			//setting fake url if nav bar was not created completely
			if (navBarLink.getCategory() == null && navBarLink.getPage() == null && navBarLink.getProduct() == null
					&& StringUtils.isBlank(navBarLink.getURL()))
			{
				cmsLinkModel.setUrl("#");
			}
		}
		else
		{
			cmsLinkModel.setUrl("#"); //setting fake url if nav bar was not created completely
		}
		for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
		{
			cmsLinkModel.setLinkName(node.getNames().get(iso), Locale.forLanguageTag(iso));
		}
		cmsLinkModel.setRestrictions((List<AbstractRestrictionModel>) node.getRestrictions());
		typedObjects.addAll(UISessionUtils.getCurrentSession().getTypeService().wrapItems(node.getRestrictions()));
		typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(cmsLinkModel));
	}

	private CMSNavigationNodeModel newNavNodeForNavigationBar(final NavigationNodeViewModel node,
			final Collection<TypedObject> typedObjects)
	{
		final CMSNavigationNodeModel newNavNode = getModelService().create("CMSNavigationNode");
		newNavNode.setUid(node.getName() + "NAV_NODE_UID" + System.nanoTime());
		newNavNode.setCatalogVersion(getCurrentContentCatalogVersion());
		newNavNode.setName(node.getName());
		for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
		{
			newNavNode.setTitle(node.getNames().get(iso), Locale.forLanguageTag(iso));
		}

		createChildColumns(node, newNavNode, typedObjects);

		return newNavNode;
	}

	private void createChildColumns(final NavigationNodeViewModel node, final CMSNavigationNodeModel newNavNode,
			final Collection<TypedObject> typedObjects)
	{
		final List<CMSNavigationNodeModel> children = new ArrayList<CMSNavigationNodeModel>();
		for (final NavigationColumnViewModel column : node.getNavigationNodeColumns())
		{
			final CMSNavigationNodeModel newColumnNavNode = createNewCMSNavigationNodeFromColumnViewModel(column, typedObjects);
			children.add(newColumnNavNode);
		}
		newNavNode.setChildren(children);
	}

	private CMSNavigationNodeModel createNewCMSNavigationNodeFromColumnViewModel(final NavigationColumnViewModel column,
			final Collection<TypedObject> typedObjects)
	{
		final CMSNavigationNodeModel newColumnNavNode = getModelService().create("CMSNavigationNode");
		newColumnNavNode.setUid(column.getName() + "COLUMN_NAV_NODE_UID" + System.nanoTime());
		newColumnNavNode.setCatalogVersion(getCurrentContentCatalogVersion());
		populateCMSNavigationNode(column, newColumnNavNode, typedObjects);
		return newColumnNavNode;
	}

	private void populateCMSNavigationNode(final NavigationColumnViewModel column, final CMSNavigationNodeModel cmsColumnNavNode,
			final Collection<TypedObject> typedObjects)
	{
		cmsColumnNavNode.setName(column.getName());
		for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
		{
			cmsColumnNavNode.setTitle(column.getNames().get(iso), Locale.forLanguageTag(iso));
		}

		//links
		Collection<CMSLinkComponentModel> links = Collections.EMPTY_LIST;
		if (cmsColumnNavNode.getLinks() != null)
		{
			links = cmsColumnNavNode.getLinks();
		}
		final LinkedList<CMSLinkComponentModel> childLinks = new LinkedList<CMSLinkComponentModel>(links);
		final Iterator<NavigationLinkViewModel> linkIterator = column.getNavigationLinks().iterator();

		while (linkIterator.hasNext())
		{
			final NavigationLinkViewModel link = linkIterator.next();
			if (link.isDeleted())
			{
				if (link.getModel() != null)
				{
					getModelService().remove(link.getModel());
					childLinks.remove(link.getModel());
				}
				linkIterator.remove();
			}
			else
			{
				if (link.getModel() == null)//new model
				{
					final CMSLinkComponentModel newNavLink = createNewCMSLinkFromLinkViewModel(link, cmsColumnNavNode);
					link.setModel(newNavLink);
					childLinks.add(newNavLink);
					typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(newNavLink));
				}
				else
				{
					populateCMSNavigationLinkFromModel(link, link.getModel(), cmsColumnNavNode);
					typedObjects.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(link.getModel()));
				}
			}
		}

		final LinkedList<CMSLinkComponentModel> finalLinkList = new LinkedList<CMSLinkComponentModel>();
		//setting links in right order, now all links should have models:)
		for (final NavigationLinkViewModel link : column.getNavigationLinks())
		{
			finalLinkList.add(link.getModel());
		}

		cmsColumnNavNode.setLinks(finalLinkList);
		getModelService().save(cmsColumnNavNode);
	}

	private CMSLinkComponentModel createNewCMSLinkFromLinkViewModel(final NavigationLinkViewModel link,
			final CMSNavigationNodeModel cmsColumnNavNode)
	{
		final CMSLinkComponentModel newNavLink = getModelService().create("CMSLinkComponent");
		newNavLink.setUid(link.getName() + "NAV_LINK_UID" + System.nanoTime());
		newNavLink.setCatalogVersion(getCurrentContentCatalogVersion());
		populateCMSNavigationLinkFromModel(link, newNavLink, cmsColumnNavNode);
		return newNavLink;
	}

	private void populateCMSNavigationLinkFromModel(final NavigationLinkViewModel link, final CMSLinkComponentModel modelNavLink,
			final CMSNavigationNodeModel cmsColumnNavNode)
	{
		for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
		{
			modelNavLink.setLinkName(link.getNames().get(iso), Locale.forLanguageTag(iso));
		}
		modelNavLink.setProduct(link.getProduct());
		modelNavLink.setContentPage(link.getPage());
		modelNavLink.setCategory(link.getCategory());
		modelNavLink.setUrl(link.getURL());

		if (StringUtils.isNotBlank(link.getURL()) && StringUtils.startsWithIgnoreCase(link.getURL(), "http"))
		{
			modelNavLink.setExternal(true);
		}

		if (modelNavLink.getProduct() == null && modelNavLink.getContentPage() == null && modelNavLink.getCategory() == null
				&& StringUtils.isBlank(modelNavLink.getUrl()))
		{
			modelNavLink.setUrl("#"); // hack to avoid Accelerator bug with null nav node links
		}

		modelNavLink.setNavigationNodes(Collections.singletonList(cmsColumnNavNode));
		modelNavLink.setRestrictions((List<AbstractRestrictionModel>) link.getRestrictions());
		getModelService().save(modelNavLink);
	}

	protected NavigationBarComponentModel createNavigationBar(final NavigationNodeViewModel node,
			final Collection<TypedObject> typedObjects, NavigationParentElement parentElement)
	{
		final NavigationBarComponentModel newNavBar = getModelService().create("NavigationBarComponent");
		newNavBar.setUid(node.getName() + "NAV_BAR_UID" + System.nanoTime());
		newNavBar.setCatalogVersion(getCurrentContentCatalogVersion());
		populateCMSNavBarFromModel(node, newNavBar, typedObjects);

        if (SlotParentElement.class.isAssignableFrom(parentElement.getClass())) {//no NavigationBarCollectionComponent wrapping
            final ContentSlotModel contentSlot = parentElement.getSlot();
            final List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>(contentSlot.getCmsComponents());
            components.add(newNavBar);
            contentSlot.setCmsComponents(components);
        }else if (ComponentParentElement.class.isAssignableFrom(parentElement.getClass())){
            NavigationBarCollectionComponentModel navigationBarCollectionComponentModel = (NavigationBarCollectionComponentModel) parentElement.getElement();
            final List<NavigationBarComponentModel> components = new ArrayList<NavigationBarComponentModel>(navigationBarCollectionComponentModel.getComponents());
            components.add(newNavBar);
            navigationBarCollectionComponentModel.setComponents(components);
        }
		return newNavBar;
	}

	private void getAllNavigationNodesFromClickedPlace(final NavigationEditorViewModel viewModel,
			final Collection<NavigationNodeTabViewModel> models, final NavigationParentElement parentElement)
	{
		for (final AbstractCMSComponentModel model : parentElement.getChildren())
		{
			if (model instanceof NavigationBarComponentModel)
			{
				final NavigationBarComponentModel navBar = (NavigationBarComponentModel) model;

				//navigation node
				final NavigationNodeViewModel mainNavNode = new NavigationNodeViewModel(viewModel.getLiveEditViewModel(),
						parentElement.getSlot());
				final NavigationNodeTabViewModel tab = new NavigationNodeTabViewModel(mainNavNode);
				tab.setNavigationService(this);
				mainNavNode.setRestrictions(navBar.getRestrictions());
				tab.setServerPath(viewModel.getServerPath());

				populateModelNavBar(mainNavNode, model, navBar);

				if (navBar.getNavigationNode() != null)
				{
					//columns
					for (final CMSNavigationNodeModel node : navBar.getNavigationNode().getChildren())
					{
						createModelColumn(mainNavNode, node);
					}
				}

				//we need to set this uid due to simplify saving
				mainNavNode.setUid(model.getUid());
				models.add(tab);
			}

		}
	}

	@Override
	public ContentSlotModel getContentSlotForPreviewCatalogVersions(final String slotUid, final LiveEditViewModel model)
	{
		return ((ContentSlotModel) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				getCatalogVersionService().setSessionCatalogVersions(model.getCurrentPreviewData().getCatalogVersions());
				final ContentSlotModel contentSlot = getCmsContentSlotService().getContentSlotForId(slotUid);
				return contentSlot;
			}
		}));
	}

	protected CMSNavigationNodeModel getNavigationNodeForPreviewCatalogVersions(final String nodeId, final LiveEditViewModel model)
	{
		return ((CMSNavigationNodeModel) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				getCatalogVersionService().setSessionCatalogVersions(model.getCurrentPreviewData().getCatalogVersions());
				try
				{
					return getCmsNavigationService().getNavigationNodeForId(nodeId);
				}
				catch (final CMSItemNotFoundException e)
				{
					return null;
				}
			}
		}));
	}

	/**
	 * @param componentUid
	 * @return
	 */
	@Override
	public AbstractCMSComponentModel getComponentForPreviewCatalogVersions(final String componentUid, final LiveEditViewModel model)
	{
		return ((AbstractCMSComponentModel) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				try
				{
					return getCmsComponentService().getAbstractCMSComponent(componentUid,
							model.getCurrentPreviewData().getCatalogVersions());
				}
				catch (final CMSItemNotFoundException e)
				{
					throw new IllegalArgumentException("Not component found for UID [" + componentUid + "]");
				}
			}
		}));
	}

	@Override
	public ContentSlotModel getNavigationBarContentSlot(final String uid, final LiveEditViewModel model)
	{
		return getContentSlotForPreviewCatalogVersions(uid, model);
	}

	private void createModelColumn(final NavigationNodeViewModel mainNavNode, final CMSNavigationNodeModel node)
	{

		getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				final LanguageModel language = getCommonI18NService().getLanguage(getCurrentLanguageFromModel(mainNavNode));
				getCommonI18NService().setCurrentLanguage(language);

				final NavigationColumnViewModel column = new NavigationColumnViewModel();
				for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
				{
					column.getNames().put(iso, node.getTitle(Locale.forLanguageTag(iso)));
				}
				column.setNavNodeUID(node.getUid());
				column.setParentNavigationNode(mainNavNode);

				//links
				for (final CMSLinkComponentModel linkModel : node.getLinks())
				{
					final NavigationLinkViewModel link = new NavigationLinkViewModel(language.getIsocode());
					populateNavigationLinkViewModel(link, linkModel, column);
					column.getNavigationLinks().add(link);
				}

				mainNavNode.getNavigationNodeColumns().add(column);
			}
		});

	}

	private void populateModelNavBar(final NavigationNodeViewModel mainNavNode,
			final AbstractCMSComponentModel simpleCMSComponent, final NavigationBarComponentModel navBar)
	{

		mainNavNode.setWrapAfter(navBar.getWrapAfter());
		mainNavNode.setStyleClass(navBar.getStyleClass());
		mainNavNode.setCatalogVersion(simpleCMSComponent.getCatalogVersion());


		getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				final LanguageModel language = getCommonI18NService().getLanguage(getCurrentLanguageFromModel(mainNavNode));
				getCommonI18NService().setCurrentLanguage(language);
				mainNavNode.getNames().put(getCurrentLanguageFromModel(mainNavNode), simpleCMSComponent.getName());
			}
		});
		mainNavNode.setNavBarLink(new NavigationLinkViewModel(getCommonI18NService().getLanguage(
				getCurrentLanguageFromModel(mainNavNode)).getIsocode()));
		populateNavigationLinkViewModel(mainNavNode.getNavBarLink(), navBar.getLink(), null);

		for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
		{
			mainNavNode.getNames().put(iso, (navBar.getLink().getLinkName(Locale.forLanguageTag(iso))));
		}
	}

	private void populateNavigationLinkViewModel(final NavigationLinkViewModel link, final CMSLinkComponentModel linkModel,
			final NavigationColumnViewModel column)
	{
		link.setColumn(column);
		link.setCategory(linkModel.getCategory());
		link.setProduct(linkModel.getProduct());
		link.setPage(linkModel.getContentPage());
		link.setURL("#".equals(linkModel.getUrl()) ? null : linkModel.getUrl());

		if (link.getMenuItemType() == null)
		{
			if (link.getCategory() != null)
			{
				link.setMenuItemType(CMSMenuItemType.CATEGORY);
			}
			if (link.getPage() != null)
			{
				link.setMenuItemType(CMSMenuItemType.CONTENT_PAGE);
			}
			if (StringUtils.isNotBlank(link.getURL()))
			{
				link.setMenuItemType(CMSMenuItemType.ARBITRARY_LINK);
			}
		}
		for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
		{
			link.getNames().put(iso, linkModel.getLinkName(Locale.forLanguageTag(iso)));
		}
		link.setModel(linkModel);
		link.setRestrictions(linkModel.getRestrictions());
	}

	@Override
	public Collection<NavigationNodeTabViewModel> getNodesForCurrentVersion(final NavigationEditorViewModel viewModel,
			final NavigationParentElement parentElement)
	{
		final Collection<NavigationNodeTabViewModel> models = new ArrayList<NavigationNodeTabViewModel>();
		getAllNavigationNodesFromClickedPlace(viewModel, models, parentElement);

		return models;
	}


	@Override
	public void removeNavigation(final NavigationNodeTabViewModel model)
	{

		final AbstractCMSComponentModel simpleCMSComponent = getComponentForPreviewCatalogVersions(model.getNavigationNode()
				.getUid(), model.getNavigationNode().getLiveEditView());
		if (simpleCMSComponent instanceof NavigationBarComponentModel)
		{
			final NavigationBarComponentModel nav = (NavigationBarComponentModel) simpleCMSComponent;
			final CMSNavigationNodeModel navigationNode = nav.getNavigationNode();
			if (navigationNode != null)
			{
				getCmsNavigationService().delete(navigationNode);
			}
			getModelService().remove(nav);

			final List<AbstractCMSComponentModel> elements = new ArrayList<AbstractCMSComponentModel>(model.getNavigationNode()
					.getContentSlot().getCmsComponents());
			elements.remove(nav);
			model.getNavigationNode().getContentSlot().setCmsComponents(elements);

		}


	}

	@Override
	public Collection<NavigationNodeTabViewModel> insertBefore(final NavigationNodeTabViewModel draggedModel,
			final NavigationNodeTabViewModel targetModel, final NavigationParentElement parentElement, final Collection<NavigationNodeTabViewModel> navigations)
	{

		if (StringUtils.isBlank(draggedModel.getNavigationNode().getUid()))
		{
			LOG.warn("Drag and Drop beetween not saved tabs");
			return navigations;
		}


		final AbstractCMSComponentModel draggedModelCMSComponent = getComponentForPreviewCatalogVersions(draggedModel
				.getNavigationNode().getUid(), draggedModel.getNavigationNode().getLiveEditView());


		if (draggedModelCMSComponent instanceof NavigationBarComponentModel)
		{
			final NavigationBarComponentModel dragged = (NavigationBarComponentModel) draggedModelCMSComponent;

			final AbstractCMSComponentModel targetModelCMSComponent = getComponentForPreviewCatalogVersions(targetModel
					.getNavigationNode().getUid(), targetModel.getNavigationNode().getLiveEditView());

			if (targetModelCMSComponent instanceof NavigationBarComponentModel) {
                final NavigationBarComponentModel target = (NavigationBarComponentModel) targetModelCMSComponent;

                int indexOfTarget = -1;
                for(NavigationNodeTabViewModel navigationNodeTabViewModel : navigations){
                    indexOfTarget++;
                    if (navigationNodeTabViewModel.getNavigationNode().getUid().equals(target.getUid())){
                        break;
                    }
                }

                if (SlotParentElement.class.isAssignableFrom(parentElement.getClass())) {//no NavigationBarCollectionComponent wrapping

                    final ContentSlotModel contentSlot = parentElement.getSlot();
                    final List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>(contentSlot.getCmsComponents());
                    //insert before
                    if (components.contains(dragged)) {
                        components.remove(dragged);
                    }
                    components.add(indexOfTarget, dragged);

                    final ArrayList<NavigationNodeTabViewModel> navigationsList = new ArrayList<NavigationNodeTabViewModel>(navigations);

                    //insert before
                    if (navigationsList.contains(draggedModel)) {
                        navigationsList.remove(draggedModel);
                    }
                    navigationsList.add(indexOfTarget, draggedModel);

                    contentSlot.setCmsComponents(components);
                    getModelService().save(contentSlot);
                    return navigationsList;

                }else if (ComponentParentElement.class.isAssignableFrom(parentElement.getClass())){
                    NavigationBarCollectionComponentModel navigationBarCollectionComponentModel = (NavigationBarCollectionComponentModel) parentElement.getElement();

                    final List<NavigationBarComponentModel> components = new ArrayList<NavigationBarComponentModel>(navigationBarCollectionComponentModel.getComponents());
                    //insert before
                    if (components.contains(dragged)) {
                        components.remove(dragged);
                    }
                    components.add(indexOfTarget, dragged);

                    final ArrayList<NavigationNodeTabViewModel> navigationsList = new ArrayList<NavigationNodeTabViewModel>(navigations);

                    //insert before
                    if (navigationsList.contains(draggedModel)) {
                        navigationsList.remove(draggedModel);
                    }
                    navigationsList.add(indexOfTarget, draggedModel);

                    navigationBarCollectionComponentModel.setComponents(components);
                    getModelService().save(navigationBarCollectionComponentModel);
                    return navigationsList;

                }
			}
		}
		return navigations;
	}


	@Override
	public boolean isSynchronizePossible(final Collection<TypedObject> itemsToSynchronize)
	{
		if (getCurrentContentCatalogVersion().getSynchronizations().isEmpty())
		{
			return false;
		}
		final Boolean result = getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Boolean execute()
			{
				getUserService().setCurrentUser(getUserService().getAdminUser());

				for (final TypedObject objectToSync : itemsToSynchronize)
				{
					final int status = getSynchronizationService().isObjectSynchronized(objectToSync);
					if (status == SynchronizationService.SYNCHRONIZATION_NOT_OK
							|| status == SynchronizationService.INITIAL_SYNC_IS_NEEDED)
					{
						return Boolean.TRUE;
					}
				}
				return Boolean.FALSE;
			}
		});
		return result.booleanValue();
	}

	@Override
	public void performSynchronization(final Collection<TypedObject> itemsToSynchronize)
	{
		final SyncItemJobModel syncItemJobModel = getCurrentContentCatalogVersion().getSynchronizations().get(0);
		getSynchronizationService().performSynchronization(itemsToSynchronize,
				Collections.singletonList(syncItemJobModel.getPk().toString()), syncItemJobModel.getTargetVersion(), null);
	}

	@Override
	public MediaModel createMedia(final NavigationNodeViewModel navigationNode, final byte[] bytes)
	{
		final MediaModel uploadedMediaModel = getModelService().create("Media");
		uploadedMediaModel.setCode("navigationBanner_" + System.nanoTime());
		uploadedMediaModel.setCatalogVersion(navigationNode.getCatalogVersion());
		getModelService().save(uploadedMediaModel);
		getMediaService().setDataForMedia(uploadedMediaModel, bytes);
		getModelService().save(uploadedMediaModel);
		navigationNode.setBanner(uploadedMediaModel);
		return uploadedMediaModel;
	}

	@Override
	public List<NavigationLinkViewModel> getNavigationLinksForContetPagesFromCurrentCatalog(final LiveEditViewModel model)
	{
		final List<NavigationLinkViewModel> allLinks = new ArrayList<NavigationLinkViewModel>();
		for (final Iterator<ContentPageModel> it = getPageService().getAllContentPages().iterator(); it.hasNext();)
		{
			final ContentPageModel page = it.next();
			if (page.getMasterTemplate().getActive().booleanValue() && StringUtils.isNotBlank(page.getLabelOrId()))
			{
				if (page.getCatalogVersion().equals(getCurrentContentCatalogVersion()))
				{
					final NavigationLinkViewModel link = new NavigationLinkViewModel(model.getCurrentPreviewData().getLanguage()
							.getIsocode());

					for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
					{
						if (page.getTitle(Locale.forLanguageTag(iso)) == null)
						{
							link.getNames().put(iso, page.getName());
						}
						else
						{
							link.getNames().put(iso, page.getTitle(Locale.forLanguageTag(iso)));
						}
					}

					link.setMenuItemType(CMSMenuItemType.CONTENT_PAGE);
					link.setPage(page);
					allLinks.add(link);
				}
			}
		}
		return allLinks;
	}

	@Override
	public Collection<CategoryModel> getRootCategories()
	{
		return getCategoryService().getRootCategoriesForCatalogVersion(
				getCounterpartProductCatalogVersionsStrategy().getCounterpartProductCatalogVersions().iterator().next());
	}

	public CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public CMSComponentService getCmsComponentService()
	{
		return cmsComponentService;
	}

	@Required
	public void setCmsComponentService(final CMSComponentService cmsComponentService)
	{
		this.cmsComponentService = cmsComponentService;
	}

	private CMSContentSlotService getCmsContentSlotService()
	{
		return cmsContentSlotService;
	}

	@Required
	public void setCmsContentSlotService(final CMSContentSlotService cmsContentSlotService)
	{
		this.cmsContentSlotService = cmsContentSlotService;
	}

	private CatalogVersionService getCatalogVersionService()
	{
		return catalogService;
	}

	@Required
	public void setCatalogService(final CatalogVersionService catalogService)
	{
		this.catalogService = catalogService;
	}

	public CMSNavigationService getCmsNavigationService()
	{
		return cmsNavigationService;
	}

	@Required
	public void setCmsNavigationService(final CMSNavigationService cmsNavigationService)
	{
		this.cmsNavigationService = cmsNavigationService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public SynchronizationServiceImpl getSynchronizationService()
	{
		return synchronizationService;
	}

	@Required
	public void setSynchronizationService(final SynchronizationServiceImpl synchronizationService)
	{
		this.synchronizationService = synchronizationService;
	}

	public MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	@Override
	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	public DefaultCMSPageService getPageService()
	{
		return pageService;
	}

	@Required
	public void setPageService(final DefaultCMSPageService pageService)
	{
		this.pageService = pageService;
	}

	public CounterpartProductCatalogVersionsStrategy getCounterpartProductCatalogVersionsStrategy()
	{
		return counterpartProductCatalogVersionsStrategy;
	}

	@Required
	public void setCounterpartProductCatalogVersionsStrategy(
			final CounterpartProductCatalogVersionsStrategy counterpartProductCatalogVersionsStrategy)
	{
		this.counterpartProductCatalogVersionsStrategy = counterpartProductCatalogVersionsStrategy;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public CommonI18NService getCommonI18NService()
	{
		return this.commonI18NService;
	}
}
