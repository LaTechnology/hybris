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

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.liveeditaddon.cockpit.service.MediaFormatService;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.ProductImageWizard;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;

import de.hybris.platform.cmscockpit.url.impl.ProductCatalogVersionFrontendUrlDecoder;





/**
 */
public class ManageProductImageCallbackEventHandler extends AbstractLiveEditCallbackEventHandler<LiveEditView>
{
	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "mediaFormatService")
	private MediaFormatService mediaFormatService;

	@Override
	public String getEventId()
	{
		return "manageProductImage";
	}

	private ProductCatalogVersionFrontendUrlDecoder productFrontendUrlDecoder;
	protected static final String PRODUCT_URL_DECODER = "defaultProductCatalogVersionFrontendRegexUrlDecoder";

	protected <T extends Object> T executeInSessionLocalViewWithProductCatalogRestrictions(final SessionExecutionBody exec)
	{
		return getSessionService().executeInLocalView(new SessionExecutionBody()
		{

			@Override
			public Object execute()
			{
				final Collection<CatalogVersionModel> productCVs = getCounterpartProductCatalogVersionsStrategy()
						.getCounterpartProductCatalogVersions();
				getCatalogVersionService().setSessionCatalogVersions(
						Collections.singletonList(getCatalogVersionService().getCatalogVersion(
								productCVs.iterator().next().getCatalog().getId(), "Staged")));

				return exec.execute();
			}
		});
	}

    @Override
    public void onCallbackEvent(final LiveEditView view, Map<String, Object> attributeMap) throws Exception
	{
		final SessionExecutionBody execBody = new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				return getProductFrontendUrlDecoder().decode(view.getModel().getCurrentUrl());
			}
		};
		ProductModel previewProduct = executeInSessionLocalViewWithProductCatalogRestrictions(execBody);

		final String serverPath = (String) attributeMap.get("serverPath");
		if (previewProduct.getPicture() == null && previewProduct instanceof VariantProductModel)
		{
			previewProduct = ((VariantProductModel) previewProduct).getBaseProduct();
		}
		final String siteUid = view.getModel().getSite().getUid();
		new ProductImageWizard(previewProduct, serverPath, mediaFormatService.getMediaFormatsForCurrentSite(siteUid), siteUid)
				.show(view);
		view.update();

	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18nService)
	{
		this.commonI18NService = commonI18nService;
	}

	public ProductCatalogVersionFrontendUrlDecoder getProductFrontendUrlDecoder()
	{
		if (productFrontendUrlDecoder == null)
		{
			productFrontendUrlDecoder = (ProductCatalogVersionFrontendUrlDecoder) SpringUtil.getBean(PRODUCT_URL_DECODER);
		}
		return productFrontendUrlDecoder;
	}

}
