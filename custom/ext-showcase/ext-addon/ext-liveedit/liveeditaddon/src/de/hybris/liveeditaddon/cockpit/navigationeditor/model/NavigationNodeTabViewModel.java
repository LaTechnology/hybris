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
package de.hybris.liveeditaddon.cockpit.navigationeditor.model;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditViewModel;
import de.hybris.platform.cockpit.constants.ImageUrls;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.media.MediaModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import de.hybris.liveeditaddon.enums.CMSMenuItemType;
import de.hybris.liveeditaddon.cockpit.multitexteditor.MultiLanguageTextBox;
import de.hybris.liveeditaddon.cockpit.restrictioneditor.RestrictionEditor;
import de.hybris.liveeditaddon.cockpit.services.NavigationEditorService;
import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;



/**
 * 
 */
public class NavigationNodeTabViewModel
{
	private String serverPath;

	private NavigationNodeViewModel navigationNode;
	private NavigationEditorService navigationService;

	public NavigationNodeTabViewModel(final NavigationNodeViewModel navigationNode)
	{
		this.setNavigationNode(navigationNode);
	}

	public MediaModel createMedia(final byte[] bytes)
	{
		return getNavigationService().createMedia(navigationNode, bytes);
	}

	public void setTitleAndURL(final MultiLanguageTextBox menuTitleContainer, final Textbox linkURL)
	{
		if (navigationNode.getNavBarLink() != null)
		{
			if (StringUtils.isBlank(navigationNode.getNavBarLink().getURL()))
			{
				linkURL.setText(NavigationPackHelper.showAlternativeUrl(navigationNode.getNavBarLink()));
			}
			else
			{
				linkURL.setText(navigationNode.getNavBarLink().getURL());
			}
		}
		linkURL.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				//for arbitrary link only
				navigationNode.getNavBarLink().setURL(((InputEvent) arg0).getValue());
			}
		});

		menuTitleContainer.setNames(navigationNode.getNames(), getCurrentLanguage());

        menuTitleContainer.addEventListener(Events.ON_CHANGE, new EventListener()
        {
            @Override
            public void onEvent(final Event arg0) throws Exception
            {
                final MultiLanguageTextBox box = (MultiLanguageTextBox) arg0.getTarget();
                navigationNode.setName(box.getName());
            }
        });

		menuTitleContainer.addEventListener(Events.ON_DROP, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final DropEvent de = (DropEvent) arg0;

				NavigationLinkViewModel link = null;
				if (de.getDragged() instanceof Treerow)
				{
					final Treerow tr = (Treerow) de.getDragged();
					final Treeitem item = (Treeitem) tr.getParent();
					link = (NavigationLinkViewModel) item.getValue();
				}
				if (de.getDragged() instanceof Listitem)
				{
					final Listitem item = (Listitem) de.getDragged();
					link = (NavigationLinkViewModel) item.getValue();
				}
				if (de.getDragged().getAttribute("link_value") != null)
				{
					final Object attribute = de.getDragged().getAttribute("link_value");
					if (attribute instanceof NavigationLinkViewModel)
					{
						link = (NavigationLinkViewModel) attribute;
						NavigationPackHelper.cleanFacetValueName(link);
					}
				}

				if (link == null)
				{
					return;
				}

				menuTitleContainer.setNames(link.getNames(), getCurrentLanguage());
				setMenuNames(link.getNames());
				getNavigationNode().setNavBarLink(link);

				if (StringUtils.isBlank(link.getURL()))
				{
					linkURL.setText(NavigationPackHelper.showAlternativeUrl(navigationNode.getNavBarLink()));
				}
				else
				{
					linkURL.setText(link.getURL());
				}


				if (link.getMenuItemType() == CMSMenuItemType.ARBITRARY_LINK)
				{
					linkURL.setDisabled(false);
				}
			}
		});
	}

	private String getCurrentLanguage()
	{
		return navigationNode.getLiveEditView().getCurrentPreviewData().getLanguage().getIsocode();
	}

	public void setMainGroupBoxName(final Groupbox box, final String navigationTitle)
	{
		box.getCaption().getChildren().clear();
		box.getCaption().appendChild(new Label("Menu for (" + navigationTitle + " )"));
	}

	public void renderUploadedImage(final Div bannerSection, final MediaModel media)
	{
		renderImageWithRemoveAction(bannerSection, media);
		renderBannerURL(bannerSection);
	}

	private void renderImageWithRemoveAction(final Div bannerSection, final MediaModel media)
	{
		final List originalSectionElements = new ArrayList(bannerSection.getChildren());
		bannerSection.getChildren().clear();

		final Div imgContainer = new Div();
		imgContainer.setSclass("productImageGallery");
		imgContainer.setParent(bannerSection);
		final Image img = new Image(getServerPath() + media.getURL());
		renderRemoveGalleryButton(imgContainer, originalSectionElements);
		img.setParent(imgContainer);
		attachHoverAction(imgContainer);

		img.setClass("uploadedMedia");
		bannerSection.appendChild(new Br());
		final Label linkLabel = new Label("Banner Link URL");
		bannerSection.appendChild(linkLabel);
	}

	private void renderBannerURL(final Div bannerSection)
	{
		final Textbox bannerLinkURL = new Textbox();
		bannerSection.appendChild(bannerLinkURL);
		bannerLinkURL.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				setBannerLinkURL(((InputEvent) arg0).getValue());
			}
		});
	}

	private void attachHoverAction(final Div imgContainer)
	{
		imgContainer.setAction("onmouseover:var actionDiv=this.getElementsByClassName('actionContainer')[0];"
				+ "actionDiv.style.visibility='visible';"
				+ "onmouseout:var actionDiv=this.getElementsByClassName('actionContainer')[0];"
				+ "actionDiv.style.visibility='hidden';");
	}

	public void renderRemoveGalleryButton(final Div imgContainer, final List originalSectionElements)
	{
		final Div actionContainer = new Div();
		actionContainer.setParent(imgContainer);
		actionContainer.setSclass("actionContainer");
		final Image removeImage = new Image(ImageUrls.REMOVE_BUTTON_IMAGE);
		removeImage.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception //NOPMD: ZK specific
			{
				actionContainer.detach();
				getNavigationNode().setBanner(null);
				imgContainer.getParent().getChildren().addAll(0, originalSectionElements);

				imgContainer.detach();
			}
		});
		removeImage.setParent(actionContainer);
	}

	public void setEnumeration(final Listbox selector, final String enumerationType)
	{
		final List<HybrisEnumValue> enumerationValues = getNavigationService().getEnumerationService().getEnumerationValues(
				enumerationType);

		for (final HybrisEnumValue val : enumerationValues)
		{
			selector.appendItem(getNavigationService().getEnumerationService().getEnumerationName(val), val.getCode());
		}
		if (selector.getItemCount() > 0)
		{
			selector.selectItem(selector.getItemAtIndex(0));
			Events.sendEvent(selector, new Event(Events.ON_SELECT, selector, selector.getItemAtIndex(0)));
		}
	}

	public List<NavigationLinkViewModel> getNavigationLinksForContentPageFromCurrentCatalog(final LiveEditViewModel model)
	{
		return getNavigationService().getNavigationLinksForContetPagesFromCurrentCatalog(model);
	}

	public Collection<CategoryModel> getCategoriesForCurrentCatalog()
	{
		return getNavigationService().getRootCategories();
	}

	public NavigationNodeViewModel getNavigationNode()
	{
		return navigationNode;
	}

	public void setNavigationNode(final NavigationNodeViewModel navigationNode)
	{
		this.navigationNode = navigationNode;
	}

	public void setMenuNames(final Map<String, String> menuTitle)
	{
		getNavigationNode().setNames(menuTitle);
	}

	public String getServerPath()
	{
		return serverPath;
	}

	public void setServerPath(final String serverPath)
	{
		this.serverPath = serverPath;
	}

	public Collection<HybrisEnumValue> getNavigationTypes()
	{
		return getNavigationService().getEnumerationService().getEnumerationValues("CMSMenuItemType");
	}

	public void setBannerLinkURL(final String bannerLinkURL)
	{
		this.getNavigationNode().setBannerLinkURL(bannerLinkURL);
	}

	public void handleAdvancedConfiguration(final Textbox styleClass, final Decimalbox wrapAfter)
	{
		styleClass.setText(getNavigationNode().getStyleClass());
		styleClass.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				getNavigationNode().setStyleClass(((InputEvent) arg0).getValue());
			}
		});

		if (getNavigationNode().getWrapAfter() != null)
		{
			wrapAfter.setValue(BigDecimal.valueOf(getNavigationNode().getWrapAfter().intValue()));
		}
		else
		{
			wrapAfter.setValue(BigDecimal.TEN);
			getNavigationNode().setWrapAfter(Integer.valueOf(10));
		}
		wrapAfter.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final Decimalbox box = (Decimalbox) ((InputEvent) arg0).getTarget();
				getNavigationNode().setWrapAfter(Integer.valueOf(box.getValue().intValue()));
			}
		});

	}

	public NavigationEditorService getNavigationService()
	{
		return navigationService;
	}

	public void setNavigationService(final NavigationEditorService navigationService)
	{
		this.navigationService = navigationService;
	}

	public void handleRestrictions(final RestrictionEditor userGroupEditorArea)
	{
		userGroupEditorArea.setRestrictions(navigationNode.getRestrictions());
		userGroupEditorArea.addEventListener(Events.ON_CHANGE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				navigationNode.setRestrictions(userGroupEditorArea.getRestrictions());
			}
		});
	}
}
