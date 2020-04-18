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
import de.hybris.platform.acceleratorcms.model.components.ProductFeatureComponentModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.liveeditaddon.cockpit.service.MediaFormatService;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.ProductImageWizard;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.cmscockpit.url.impl.ProductCatalogVersionFrontendUrlDecoder;

import java.util.Map;


/**
 * 
 */
public class ManageProductFeatureImageCallbackEventHandler extends AbstractLiveEditCallbackEventHandler<LiveEditView>
{

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "mediaFormatService")
	private MediaFormatService mediaFormatService;

	@Override
	public String getEventId()
	{
		return "manageProductFeatureImage";
	}

	private ProductCatalogVersionFrontendUrlDecoder productFrontendUrlDecoder;
	protected static final String PRODUCT_URL_DECODER = "defaultProductCatalogVersionFrontendRegexUrlDecoder";

    @Override
    public void onCallbackEvent(LiveEditView view, Map<String, Object> attributeMap) throws Exception {
		ProductModel previewProduct = null;

		final String componentId = (String)attributeMap.get("item_uid");
		final AbstractCMSComponentModel component = getComponentForUid(componentId, view);
		if (component instanceof ProductFeatureComponentModel)
		{
			final ProductFeatureComponentModel productFeatureComponent = (ProductFeatureComponentModel) component;


			previewProduct = productFeatureComponent.getProduct();
		}

		final String serverPath = (String)attributeMap.get("serverPath");
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


}
