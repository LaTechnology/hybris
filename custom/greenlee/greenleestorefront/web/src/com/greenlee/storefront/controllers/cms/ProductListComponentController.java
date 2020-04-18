/**
 *
 */
package com.greenlee.storefront.controllers.cms;

import de.hybris.platform.cms2lib.model.components.ProductListComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.greenlee.storefront.controllers.ControllerConstants;


/**
 * @author raja.santhanam
 *
 */
@Controller("ProductListComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.ProductListComponent)
public class ProductListComponentController extends AbstractCMSComponentController<ProductListComponentModel>
{
	private static final Logger LOG = Logger.getLogger(ProductListComponentController.class);

	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.IMAGES, ProductOption.SUMMARY);

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greenlee.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.
	 * HttpServletRequest, org.springframework.ui.Model,
	 * de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final ProductListComponentModel component)
	{

		final List<ProductData> products = new ArrayList<>();

		products.addAll(collectLinkedProducts(component));

		model.addAttribute("productData", products);
	}

	protected List<ProductData> collectLinkedProducts(final ProductListComponentModel component)
	{
		final List<ProductData> products = new ArrayList<>();

		if (component.getProducts() != null)
		{
			for (final ProductModel productModel : component.getProducts())
			{
				LOG.error("Product Added [ " + productModel.getCode() + " ]");
				products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
			}
		}

		/*
		 * final CategoryModel categoryModel = component.getCategory(); if (categoryModel != null) { for (final
		 * ProductModel productModel : categoryModel.getProducts()) {
		 * products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS)); } }
		 */

		return products;
	}

}
