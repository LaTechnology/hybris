/**
 *
 */
package com.greenlee.storefront.controllers.integration;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.greenlee.storefront.controllers.ControllerConstants;


/**
 * @author peter.asirvatham
 * 
 */
@Controller
public class SalesforceWebToLeadController extends AbstractController
{

	@RequestMapping(value = "/salesforwebtolead", method = RequestMethod.GET)
	public String getView(final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		return ControllerConstants.Views.Integration.SALESFORCEWEBTOLEAD;
	}
}
