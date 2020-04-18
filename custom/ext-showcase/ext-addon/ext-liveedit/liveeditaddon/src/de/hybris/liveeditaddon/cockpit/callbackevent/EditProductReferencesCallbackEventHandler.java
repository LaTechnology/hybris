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
import de.hybris.platform.cmscockpit.url.impl.FrontendUrlDecoder;
import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.*;

import org.springframework.beans.factory.annotation.Required;




/**
 * 
 */
public class EditProductReferencesCallbackEventHandler<V extends LiveEditView> extends AbstractLiveEditCallbackEventHandler<V>
{
	private FrontendUrlDecoder<ProductModel> productFrontendUrlDecoder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#getEventId()
	 */
	@Override
	public String getEventId()
	{
		return "editProductReferences";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#onCallbackEvent(de.hybris.platform.cmscockpit
	 * .components.liveedit.LiveEditView, java.lang.String[])
	 */
    @Override
    public void onCallbackEvent(V view, Map<String, Object> attributeMap) throws Exception
	{
		final ProductModel product = getProductForPreviewCatalogVersions(view, view.getModel().getCurrentUrl());

		if (product != null)
		{
			final ProductReferencesComponentModel component = (ProductReferencesComponentModel) getComponentForUid(
                    (String)attributeMap.get("cmp_id"), view);

			final TypedObject wrappedProduct = getCockpitTypeService().wrapItem(product);
			final List<ProductReferenceModel> references = getAllProductReferencesFromSourceOfType(product,
					component.getProductReferenceTypes());
			final Collection<TypedObject> wrappedReferences = getCockpitTypeService().wrapItems(references);

			UISessionUtils
					.getCurrentSession()
					.getCurrentPerspective()
					.openReferenceCollectionInBrowserContext(wrappedReferences,
							getCockpitTypeService().getObjectTemplate(ProductReferenceModel._TYPECODE), wrappedProduct,
							Collections.<String, Object> emptyMap());
		}

	}



	/**
	 * Get an attribute value from a product. If the attribute value is null and the product is a variant then the same
	 * attribute will be requested from the base product.
	 * 
	 * @param product
	 *           the product
	 * @param attribute
	 *           the name of the attribute to lookup
	 * @return the value of the attribute
	 */
	protected Object getProductAttribute(final ProductModel product, final String attribute)
	{
		final Object value = getModelService().getAttributeValue(product, attribute);
		if (product instanceof VariantProductModel
				&& (value == null || (value instanceof Collection && ((Collection) value).isEmpty())))
		{
			final ProductModel baseProduct = ((VariantProductModel) product).getBaseProduct();
			if (baseProduct != null)
			{
				return getProductAttribute(baseProduct, attribute);
			}
		}
		return value;
	}

	protected Collection<ProductReferenceModel> getProductReferencesForProduct(final ProductModel product)
	{
		return (Collection<ProductReferenceModel>) getProductAttribute(product, ProductModel.PRODUCTREFERENCES);
	}

	protected Set<ProductModel> getAllBaseProducts(final ProductModel productModel)
	{
		final Set<ProductModel> allBaseProducts = new HashSet<ProductModel>();

		ProductModel currentProduct = productModel;
		allBaseProducts.add(currentProduct);

		while (currentProduct instanceof VariantProductModel)
		{
			currentProduct = ((VariantProductModel) currentProduct).getBaseProduct();

			if (currentProduct == null)
			{
				break;
			}
			else
			{
				allBaseProducts.add(currentProduct);
			}
		}
		return allBaseProducts;
	}

	protected List<ProductReferenceModel> getAllProductReferencesFromSourceOfType(final ProductModel product,
			final Collection<ProductReferenceTypeEnum> referenceTypes)
	{
		final Collection<ProductReferenceModel> allReferences = getProductReferencesForProduct(product);
		if (allReferences != null && !allReferences.isEmpty())
		{
			final Set<ProductModel> allSourceProducts = getAllBaseProducts(product);
			final List<ProductReferenceModel> matchingReferences = new ArrayList<ProductReferenceModel>();

			for (final ProductReferenceModel reference : allReferences)
			{
				if (reference != null && referenceTypes.contains(reference.getReferenceType())
						&& allSourceProducts.contains(reference.getSource()))
				{
					matchingReferences.add(reference);
				}
			}

			return matchingReferences;
		}
		return null;
	}

	protected ProductModel getProductForPreviewCatalogVersions(final V view, final String url)
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

	public FrontendUrlDecoder<ProductModel> getProductFrontendUrlDecoder()
	{
		return productFrontendUrlDecoder;
	}

	@Required
	public void setProductFrontendUrlDecoder(final FrontendUrlDecoder<ProductModel> productFrontendUrlDecoder)
	{
		this.productFrontendUrlDecoder = productFrontendUrlDecoder;
	}
}
