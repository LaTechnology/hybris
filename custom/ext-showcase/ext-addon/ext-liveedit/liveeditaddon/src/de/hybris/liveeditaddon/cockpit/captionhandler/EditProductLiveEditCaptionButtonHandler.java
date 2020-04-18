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
package de.hybris.liveeditaddon.cockpit.captionhandler;

import de.hybris.platform.cmscockpit.cms.strategies.CounterpartProductCatalogVersionsStrategy;
import de.hybris.platform.cmscockpit.components.liveedit.AbstractLiveEditCaptionButtonHandler;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserModel;
import de.hybris.platform.cmscockpit.session.impl.LiveEditContentBrowser;
import de.hybris.platform.cmscockpit.url.impl.FrontendUrlDecoder;
import de.hybris.liveeditaddon.cockpit.session.impl.LiveeditaddonPerspective;
import de.hybris.liveeditaddon.cockpit.session.impl.LiveeditaddonPerspective.OnEventCallback;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserArea;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hbox;

import javax.annotation.Resource;


/**
 * 
 */
public class EditProductLiveEditCaptionButtonHandler extends AbstractLiveEditCaptionButtonHandler
{

	@Resource(name = "cockpitTypeService")
	private TypeService cockpitTypeService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;

	@Resource(name = "counterpartProductCatalogVersionsStrategy")
	private CounterpartProductCatalogVersionsStrategy counterpartProductCatalogVersionsStrategy;

	private FrontendUrlDecoder<ProductModel> productFrontendUrlDecoder;


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cmscockpit.components.liveedit.LiveEditCaptionButtonHandler#createButton(de.hybris.platform.
	 * de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserModel,
	 * de.hybris.platform.cmscockpit.session.impl.LiveEditContentBrowser, org.zkoss.zul.Hbox)
	 */
	@Override
	public void createButton(final LiveEditBrowserArea area, final LiveEditBrowserModel browserModel,
			final LiveEditContentBrowser contentBrowser, final Hbox buttonContainer)
	{
		final ProductModel product = getProductForPreviewCatalogVersions(browserModel.getCurrentUrl());

		if (product != null)
		{
			createRightCaptionButton(Labels.getLabel("browser.editProduct"), "btnliveeditcontent_editproduct", buttonContainer,
					new org.zkoss.zk.ui.event.EventListener()
					{
						@Override
						public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
						{
							final TypedObject wrappedItem = getCockpitTypeService().wrapItem(product);

							((LiveeditaddonPerspective) UISessionUtils.getCurrentSession().getCurrentPerspective())
									.activateItemInLiveEditPopup(wrappedItem, createPopupWindow(contentBrowser.getParent()),
											new OnEventCallback()
											{


												@Override
												public void onEvent(final Event event)
												{
													if (event.getName().equals(Events.ON_CLOSE))
													{
														contentBrowser.update();
													}

												}
											}, null);
						}
					});
		}

	}


	protected ProductModel getProductForPreviewCatalogVersions(final String url)
	{
		return ((ProductModel) getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				getCatalogVersionService().setSessionCatalogVersions(
						getCounterpartProductCatalogVersionsStrategy().getCounterpartProductCatalogVersions());
				return getProductFrontendUrlDecoder().decode(url);

			}
		}));
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

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
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

	public FrontendUrlDecoder<ProductModel> getProductFrontendUrlDecoder()
	{
		return productFrontendUrlDecoder;
	}

	@Required
	public void setProductFrontendUrlDecoder(final FrontendUrlDecoder<ProductModel> productFrontendUrlDecoder)
	{
		this.productFrontendUrlDecoder = productFrontendUrlDecoder;
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

}
