package com.hybris.productcomparison.controllers.pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.ProductData;

import com.hybris.productcomparison.data.WrapperMapVariantAttributes;
import com.hybris.productcomparison.facades.ProductComparisonFacade;

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */

@Controller
@RequestMapping("/compare")
public class ProductComparisonController
{
	@Autowired
	ProductComparisonFacade productComparisonFacade;

	private static final String COMPARE_LIST = "addon:/productcomparison/fragments/compareList";
	
	@ResponseBody
	@RequestMapping(value= "/add", method = RequestMethod.POST)
	public Integer add(@RequestParam("code") final String productCode)
	{
		productComparisonFacade.add(productCode);
		return productComparisonFacade.size();
	}
	
	@ResponseBody
	@RequestMapping(value= "/cbremove", method = RequestMethod.POST)
	public Integer remove(@RequestParam("code") final String productCode)
	{
		productComparisonFacade.remove(productCode);
		return productComparisonFacade.size();
	}
	
	@RequestMapping(value= "/remove", method = RequestMethod.GET)
	public String removetest(@RequestParam("code") final String productCode)
	{
		productComparisonFacade.remove(productCode);
		return "redirect:/comparison";
	}
	
	@RequestMapping(value= "/clear", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void clear()
	{
		productComparisonFacade.clear();
	}
	
	@RequestMapping(value= "/list", method = RequestMethod.GET)
	public String list(final HttpServletRequest request, final Model model)
	{
		String popup = request.getParameter("popup");
		if (popup != null) {
			model.addAttribute("pcPopup", true);
		}
		String close = request.getParameter("close");
		if (close != null) {
			model.addAttribute("pcPopupClose", true);
		}
		List<ProductData> list = productComparisonFacade.getProductComparisonList();
		model.addAttribute("productList", list);
     	model.addAttribute("classifications", productComparisonFacade.aggregateAllClassificationAttributes(list));
		return COMPARE_LIST;
	}
}
