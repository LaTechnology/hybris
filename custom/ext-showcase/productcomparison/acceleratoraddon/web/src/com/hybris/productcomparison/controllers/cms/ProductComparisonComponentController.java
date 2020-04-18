package com.hybris.productcomparison.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hybris.productcomparison.cms.model.ProductComparisonComponentModel;
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

/**
 *
 */
@Controller("ProductComparisonComponentController")
@RequestMapping("/view/ProductComparisonComponentController")
public class ProductComparisonComponentController extends AbstractCMSAddOnComponentController<ProductComparisonComponentModel>
{

	@Autowired
	ProductComparisonFacade productComparisonFacade;

	@Override
	protected void fillModel(HttpServletRequest request, Model model, ProductComparisonComponentModel component)
	{
		List<String> codes = productComparisonFacade.getProductComparisonCodes();
		Map<String, String> map = new HashMap<String, String>();
		for (String code : codes)
		{
			map.put(code, code);
		}
		
		model.addAttribute("pcCodes", map);
	}

}
