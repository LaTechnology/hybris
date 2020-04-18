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
package de.hybris.liveeditaddon.cockpit.callbackevent;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.session.impl.CmsListBrowserSectionModel;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.liveeditaddon.service.CmsObjectService;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Window;

import de.hybris.platform.cmscockpit.cms.strategies.CounterpartProductCatalogVersionsStrategy;
import de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler;
import de.hybris.platform.core.Registry;
import org.springframework.context.ApplicationContext;
/**
 * 
 */
public abstract class AbstractLiveEditCallbackEventHandler<V extends LiveEditView> implements CallbackEventHandler<V>
{
	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "cockpitTypeService")
	private TypeService cockpitTypeService;

	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	@Resource(name = "cmsContentSlotService")
	private CMSContentSlotService cmsContentSlotService;

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	@Resource(name = "cmsObjectService")
	private CmsObjectService cmsObjectService;

	@Resource(name = "counterpartProductCatalogVersionsStrategy")
	private CounterpartProductCatalogVersionsStrategy counterpartProductCatalogVersionsStrategy;

    protected ApplicationContext getApplicationContext(){
        return Registry.getApplicationContext();
    }
	/**
	 * @param componentUid
	 * @return
	 */
	protected AbstractCMSComponentModel getComponentForUid(final String componentUid, final LiveEditView view)
	{
		AbstractCMSComponentModel component = null;
		try
		{
			component = getCmsComponentService().getAbstractCMSComponent(componentUid,
					view.getModel().getCurrentPreviewData().getCatalogVersions());
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new IllegalArgumentException("Not component found for UID [" + componentUid + "]");
		}
		return component;
	}

	protected <C extends ItemModel> C getCMSObjectForUid(final String uid, final LiveEditView view)
	{
		C object = null;
		try
		{
			object = getCmsObjectService().getItemOrRelation(uid, view.getModel().getCurrentPreviewData().getCatalogVersions());
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new IllegalArgumentException("Not component found for UID [" + uid + "]");
		}
		return object;
	}



	protected ContentSlotModel getContentSlotForPreviewCatalogVersions(final String slotUid, final LiveEditView view)
	{
		return ((ContentSlotModel) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				getCatalogVersionService().setSessionCatalogVersions(view.getModel().getCurrentPreviewData().getCatalogVersions());
				final ContentSlotModel contentSlot = getCmsContentSlotService().getContentSlotForId(slotUid);
				return contentSlot;
			}
		}));
	}

	protected AbstractPageModel getPageForPreviewCatalogVersions(final String pageUid, final LiveEditView view)
	{
		return ((AbstractPageModel) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				getCatalogVersionService().setSessionCatalogVersions(view.getModel().getCurrentPreviewData().getCatalogVersions());
				AbstractPageModel page;
				try
				{
					page = getCmsPageService().getPageForId(pageUid);
				}
				catch (final CMSItemNotFoundException e)
				{
					return null;
				}
				return page;
			}
		}));
	}

	protected ContentSlotNameModel getContentSlotName(final String position, final AbstractPageModel page)
	{
		for (final ContentSlotNameModel name : page.getMasterTemplate().getAvailableContentSlots())
		{
			if (name.getName().equalsIgnoreCase(position))
			{
				return name;
			}
		}
		return null;
	}

	protected Set<CMSComponentTypeModel> getValidComponentTypes(final String position, final AbstractPageModel page)
	{
		final ContentSlotNameModel slotName = getContentSlotName(position, page);
		final Set<CMSComponentTypeModel> validComponentTypes = new HashSet<CMSComponentTypeModel>();
		validComponentTypes.addAll(slotName.getValidComponentTypes());
		if (slotName.getCompTypeGroup() != null)
		{
			validComponentTypes.addAll(slotName.getCompTypeGroup().getCmsComponentTypes());
		}
		return validComponentTypes;
	}

	protected boolean isValidForContentSlot(final ComposedTypeModel type, final String position, final AbstractPageModel page)
	{
		return getValidComponentTypes(position, page).contains(type);
	}

	protected BrowserSectionModel getBrowserSectionForPosition(final String position, final CmsPageBrowserModel pageModel)
	{
		for (final BrowserSectionModel sectionModel : pageModel.getBrowserSectionModels())
		{
			if (sectionModel instanceof CmsListBrowserSectionModel)
			{
				if (((CmsListBrowserSectionModel) sectionModel).getPosition().equalsIgnoreCase(position))
				{
					return sectionModel;
				}
			}
		}
		return null;
	}

	protected CmsPageBrowserModel getPageBrowserModel(final LiveEditView view, final String pageUid)
	{
		final AbstractPageModel page = getPageForPreviewCatalogVersions(pageUid, view);
		final CmsPageBrowserModel pageModel = getApplicationContext().getBean("cmsPageBrowserModel", CmsPageBrowserModel.class);
		final TypedObject pageObject = getCockpitTypeService().wrapItem(page);
		final UIBrowserArea area = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();

		pageModel.setCurrentPageObject(pageObject);
		pageModel.setArea(area);
		pageModel.initialize();

		return pageModel;
	}

	protected Window createPopupWindow(final Component parent)
	{
		//popup window
		final Window popupwindow = new Window();
		popupwindow.setParent(parent);
		popupwindow.setSclass("popupwindow");
		popupwindow.setVisible(false);
		popupwindow.setWidth("800px");
		popupwindow.setHeight("800px");
		popupwindow.setBorder("none");
		popupwindow.setClosable(true);
		popupwindow.setMaximizable(false);
		popupwindow.setSizable(false);
		popupwindow.setShadow(false);
		popupwindow.setAction("onhide: anima.fade(#{self}); onshow: anima.appear(#{self});");

		final Caption caption = new Caption();
		caption.setParent(popupwindow);

		return popupwindow;
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

	@Required
	public void setCockpitTypeService(final TypeService typeService)
	{
		this.cockpitTypeService = typeService;
	}

	public TypeService getCockpitTypeService()
	{
		return this.cockpitTypeService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public SessionService getSessionService()
	{
		return this.sessionService;
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

	public CMSContentSlotService getCmsContentSlotService()
	{
		return cmsContentSlotService;
	}

	@Required
	public void setCmsContentSlotService(final CMSContentSlotService cmsContentSlotService)
	{
		this.cmsContentSlotService = cmsContentSlotService;
	}

	public CMSPageService getCmsPageService()
	{
		return cmsPageService;
	}

	@Required
	public void setCmsPageService(final CMSPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}

	public CmsObjectService getCmsObjectService()
	{
		return cmsObjectService;
	}

	@Required
	public void setCmsObjectService(final CmsObjectService cmsObjectService)
	{
		this.cmsObjectService = cmsObjectService;
	}

	public CounterpartProductCatalogVersionsStrategy getCounterpartProductCatalogVersionsStrategy()
	{
		return counterpartProductCatalogVersionsStrategy;
	}

	/**
	 * @param counterpartProductCatalogVersionsStrategy
	 *           the counterpartProductCatalogVersionsStrategy to set
	 */
	@Required
	public void setCounterpartProductCatalogVersionsStrategy(
			final CounterpartProductCatalogVersionsStrategy counterpartProductCatalogVersionsStrategy)
	{
		this.counterpartProductCatalogVersionsStrategy = counterpartProductCatalogVersionsStrategy;
	}
}
