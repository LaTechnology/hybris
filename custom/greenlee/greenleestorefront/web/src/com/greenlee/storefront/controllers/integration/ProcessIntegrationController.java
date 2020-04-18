/**
 *
 */
package com.greenlee.storefront.controllers.integration;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenlee.orderhistory.data.PIHeaderDetails;
import com.greenlee.orderhistory.data.PIOrderItem;
import com.greenlee.pi.data.PICustomerData;
import com.greenlee.pi.service.GreenleePICustomerService;
import com.greenlee.pi.service.GreenleePIOrderHeaderService;
import com.greenlee.pi.service.GreenleePIOrderItemService;


/**
 * @author peter.asirvatham
 * 
 */
@Controller
public class ProcessIntegrationController extends AbstractController
{

	private static final Logger LOG = Logger.getLogger(ProcessIntegrationController.class);
	private static final String DATE_FORMAT = "yyyyMMdd";

	@Resource(name = "greenleePIOrderItemService")
	private GreenleePIOrderItemService greenleePIOrderItemService;

	@Resource(name = "greenleePIOrderHeaderService")
	private GreenleePIOrderHeaderService greenleePIOrderHeaderService;

	@Resource(name = "greenleePICustomerService")
	private GreenleePICustomerService greenleePICustomerService;


	@RequestMapping(value = "/getPIOrderDetails", method = RequestMethod.GET)
	public PIOrderItem getOrderDetails(@RequestParam(value = "orderid", required = true) final String orderid,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		LOG.info("ProcessIntegrationController -> getPIOrderDetails ");
		return greenleePIOrderItemService.getPIOrderDetails(orderid);
	}

	@RequestMapping(value = "/getPIOrderHeader", method = RequestMethod.GET)
	public List<PIHeaderDetails> getOrderHeader(@RequestParam(value = "customerId", required = true) final String sapCustomerId,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		LOG.info("ProcessIntegrationController -> getPIOrderHeader ");
		final CustomerData customerData = new CustomerData();
		customerData.setSapConsumerID(sapCustomerId);
		return greenleePIOrderHeaderService.getPIOrderHeaderDetails(getCustomerDataForOrderHistory(-12, sapCustomerId));
	}

	@RequestMapping(value = "/createCustomer", method = RequestMethod.GET)
	public PICustomerData createCustomer(final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		LOG.info("ProcessIntegrationController -> createCustomer ");
		return greenleePICustomerService.piCreateCustomer(createPICustomerTestData());
	}

	public CustomerData getCustomerDataForOrderHistory(final int startingMonth, final String sapCustomerId)
	{
		final CustomerData customerData = new CustomerData();
		final Calendar prevYear = Calendar.getInstance();
		prevYear.add(Calendar.MONTH, startingMonth);
		final Date date = Calendar.getInstance().getTime();
		final DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		customerData.setSapFromDate(formatter.format(prevYear.getTime()));
		customerData.setSapToDate(formatter.format(date));
		customerData.setSapConsumerID(sapCustomerId);
		return customerData;
	}

	public PICustomerData createPICustomerTestData()
	{
		final PICustomerData customerData = new PICustomerData();
		customerData.setCustomerName("Peter");
		customerData.setBillToCustomerNo("12345");
		customerData.setEmailId("peter@123.com");
		customerData.setBaseReference("100050");
		return customerData;
	}
}
