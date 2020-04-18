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
package de.hybris.liveeditaddon.cockpit.navigationeditor.composers;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.liveeditaddon.cockpit.media.MediaHelper;
import de.hybris.platform.core.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleTreeModel;
import org.zkoss.zul.SimpleTreeNode;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import de.hybris.liveeditaddon.enums.CMSMenuItemType;
import de.hybris.liveeditaddon.cockpit.multitexteditor.MultiLanguageTextBox;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationNodeTabViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.wizard.NavigationEditorWizard;
import de.hybris.liveeditaddon.cockpit.restrictioneditor.RestrictionEditor;
import de.hybris.liveeditaddon.cockpit.services.impl.DefaultNavigationStateViewModel;
import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;


/**
 * 
 */
public class NavigationItemEditorComposer extends GenericForwardComposer
{
	public static final String WIZARD_ARG = "wizardModel";

	private NavigationNodeTabViewModel model;
	private Listbox menuItemTypeSelector;
	private Listbox bannerSizeSelector;
	private MultiLanguageTextBox menuTitle;
	private Textbox linkURL;
	private Div bannerSection;
	private Div columnDesigner;
	private Div leftContentContainer;

	private Textbox styleClass;
	private Decimalbox wrapAfter;

	private RestrictionEditor navBarRestrictions;

	private static final Logger LOG = Logger.getLogger(NavigationItemEditorComposer.class);

	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		model = (NavigationNodeTabViewModel) args.get(NavigationEditorWizard.WIZARD_ARG);

		handleSelectionOfItemType();

		model.setEnumeration(menuItemTypeSelector, "CMSMenuItemType");
		model.setEnumeration(bannerSizeSelector, "MenuBannerSize");

		model.setTitleAndURL(menuTitle, linkURL);

		model.handleAdvancedConfiguration(styleClass, wrapAfter);
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put(WIZARD_ARG, model);

		Collection columns = model.getNavigationNode().getNavigationNodeColumns();
		if (columns == null)
		{
			columns = Collections.EMPTY_LIST;
		}
		map.put(NavigationEditorComposer.NAVIGATION_COLUMNS, columns);

		model.handleRestrictions(navBarRestrictions);

		Executions.getCurrent().createComponents("/cmscockpit/ColumnCollectionDesigner.zul", columnDesigner, map);
	}

	private void handleSelectionOfItemType()
	{
		menuItemTypeSelector.addEventListener(Events.ON_SELECT, new EventListener()
		{

			@Override
			public void onEvent(final Event event) throws Exception
			{
				final CMSMenuItemType enumerationValue = getMenuItemType(event);

				if (enumerationValue.equals(CMSMenuItemType.ARBITRARY_LINK))
				{
					Executions.getCurrent().createComponents("/cmscockpit/ArbitraryLinkEditorDesigner.zul", getWrapper(),
							Collections.singletonMap(MultiLanguageTextBox.DEF_LANG, getCurrentLanguageIsoCode()));
				}
				if (enumerationValue.equals(CMSMenuItemType.NAVIGATION_STATE))
				{
					renderNavigationState();
				}
				if (enumerationValue.equals(CMSMenuItemType.CONTENT_PAGE))
				{
					renderContentPages();
				}
				if (enumerationValue.equals(CMSMenuItemType.CATEGORY))
				{
					renderCatalogs();
				}
			}

			private String getCurrentLanguageIsoCode()
			{
				return model.getNavigationNode().getLiveEditView().getCurrentPreviewData().getLanguage().getIsocode();
			}

			private CMSMenuItemType getMenuItemType(final Event event)
			{
				final Listitem next;
				if (event instanceof SelectEvent)//changed by user
				{
					final SelectEvent se = (SelectEvent) event;
					next = (Listitem) se.getSelectedItems().iterator().next();
				}
				else
				//set by setEnumeration method
				{
					next = (Listitem) event.getData();
				}
				final CMSMenuItemType enumerationValue = model.getNavigationService().getEnumerationService()
						.getEnumerationValue("CMSMenuItemType", next.getValue().toString());

				columnDesigner.setVisible(true);
				leftContentContainer.getChildren().clear();
				return enumerationValue;
			}

			private void renderNavigationState()
			{
				final Map<String, Object> map = new HashMap<String, Object>();
                DefaultNavigationStateViewModel defaultNavigationStateViewModel = getNewDefaultNavigationStateViewModel();
                defaultNavigationStateViewModel.setLiveEditView(model.getNavigationNode().getLiveEditView());
				map.put(WIZARD_ARG, defaultNavigationStateViewModel);
				Executions.getCurrent().createComponents("/cmscockpit/navigationStateComposer.zul", getWrapper(), map);
			}


            protected DefaultNavigationStateViewModel getNewDefaultNavigationStateViewModel() {
                return (DefaultNavigationStateViewModel) Registry.getApplicationContext().getBean("navigationStateViewModel");
            }
			private void renderContentPages()
			{
				final Listbox contentPageBox = new Listbox();
				contentPageBox.setParent(getWrapper());

				final List<NavigationLinkViewModel> navigationLinks = model.getNavigationLinksForContentPageFromCurrentCatalog(model
						.getNavigationNode().getLiveEditView());
				Collections.sort(navigationLinks, new Comparator<NavigationLinkViewModel>()
				{
					final String currentLanguage = getCurrentLanguageIsoCode();

					@Override
					public int compare(final NavigationLinkViewModel o1, final NavigationLinkViewModel o2)
					{

						return o1.getNames().get(currentLanguage).compareTo(o2.getNames().get(currentLanguage));
					}

				});
				for (final NavigationLinkViewModel link : navigationLinks)
				{
					final Listitem item = new Listitem(link.getNames().get(getCurrentLanguageIsoCode()), link);
					item.setDraggable("link");
					item.setDroppable("link");
					contentPageBox.appendChild(item);
				}
			}

			private Div getWrapper()
			{
				final Div wrapper = new Div();
				wrapper.setParent(leftContentContainer);
				wrapper.setSclass("listContainer");
				return wrapper;
			}

			private void renderCatalogs()
			{
				final Tree tree = new Tree();
				tree.setZclass("z-dottree");
				tree.setParent(leftContentContainer);
				tree.setTreeitemRenderer(new TreeitemRenderer()
				{

					@Override
					public void render(final Treeitem item, final Object arg1) throws Exception
					{
						renderEntry(item, (SimpleTreeNode) arg1);
					}

					private void renderEntry(final Treeitem item, final SimpleTreeNode node)
					{
						final NavigationLinkViewModel link = (NavigationLinkViewModel) node.getData();
						item.setOpen(true);
						item.setValue(link);
						Treerow treeRow = item.getTreerow();
						if (treeRow == null)
						{
							(treeRow = new Treerow()).setParent(item);
						}
						final Treecell cell = new Treecell(link.getNames().get(getCurrentLanguageIsoCode()));
						cell.setParent(treeRow);
						treeRow.setDraggable("link");
						treeRow.setDroppable("link");
					}
				});

				final List test = new ArrayList();
				render(test, model.getCategoriesForCurrentCatalog());

				final SimpleTreeModel stm = new SimpleTreeModel(new SimpleTreeNode("ROOT", test));
				tree.setModel(stm);
			}

			private void render(final List test, final Collection<CategoryModel> collection)
			{
				for (final CategoryModel category : collection)
				{
					if (StringUtils.isNotBlank(category.getName()))//yes we have this situation in acc data
					{
						final NavigationLinkViewModel createLinkModel = createLinkModel(category);
						if (category.getAllSubcategories().isEmpty())
						{
							test.add(new SimpleTreeNode(createLinkModel, new ArrayList()));
						}
						else
						{
							final List childList = new ArrayList();
							render(childList, category.getAllSubcategories());
							test.add(new SimpleTreeNode(createLinkModel, childList));
						}
					}
				}
			}

			private NavigationLinkViewModel createLinkModel(final CategoryModel category)
			{
				final NavigationLinkViewModel link = new NavigationLinkViewModel(model.getNavigationNode().getLiveEditView()
						.getCurrentPreviewData().getLanguage().getIsocode());

				for (final String iso : NavigationPackHelper.getLanguageIsoCodes())
				{
					link.getNames().put(iso, category.getName(Locale.forLanguageTag(iso)));
				}
				link.setMenuItemType(CMSMenuItemType.CATEGORY);
				link.setCategory(category);
				return link;
			}
		});
	}

	@SuppressWarnings("PMD")
	public void onClick$btnUpload(final ForwardEvent event)
	{
		try
		{
			final org.zkoss.util.media.Media uploadedMedia = Fileupload.get(Labels.getLabel("uploadmediawizard.fileupload.tips"),
					null);
			final byte[] bytes = MediaHelper.extractMediaBytesFromMedia(LOG, uploadedMedia);
			if (bytes != null)
			{
				final MediaModel media = model.createMedia(bytes);
				model.renderUploadedImage(bannerSection, media);
			}
		}
		catch (final InterruptedException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
