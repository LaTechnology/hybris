package com.hybris.productcomparison.controllers.cms;

import com.hybris.productcomparison.cms.model.ProductComparisonActionModel;
import com.hybris.productcomparison.facades.ProductComparisonFacade;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
@Controller("ProductComparisonActionController")
@RequestMapping("/view/ProductComparisonActionController")
public class ProductComparisonActionController extends AbstractCMSAddOnComponentController<ProductComparisonActionModel>
{

	@Autowired
	ProductComparisonFacade productComparisonFacade;

	@Override
	protected void fillModel(HttpServletRequest request, Model model, ProductComparisonActionModel component)
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
