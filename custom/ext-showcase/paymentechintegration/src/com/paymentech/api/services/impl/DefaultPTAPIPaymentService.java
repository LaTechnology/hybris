/**
 *
 */
package com.paymentech.api.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;

import com.paymentech.api.services.PTAPIPaymentService;
import com.paymentech.constants.PaymentechintegrationConstants;
import com.paymentech.jaxb.request.MarkForCaptureType;
import com.paymentech.jaxb.request.NewOrderType;
import com.paymentech.jaxb.request.ObjectFactory;
import com.paymentech.jaxb.request.Request;
import com.paymentech.jaxb.request.ValidIndustryTypes;
import com.paymentech.jaxb.request.ValidTransTypes;
import com.paymentech.jaxb.response.Response;
import com.paymentech.payment.data.MarkForCaptureData;
import com.paymentech.payment.data.NewOrderData;
import com.paymentech.rest.client.PTSecurePaymentClient;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author dipankan
 *
 */
public class DefaultPTAPIPaymentService implements PTAPIPaymentService
{
	private static final Logger LOG = Logger.getLogger(DefaultPTAPIPaymentService.class.getName());
	private ConfigurationService configService;
	private PTSecurePaymentClient ptSecurePaymentClient;
	private ModelService modelService;
	private CartService cartService;
	private CommerceCheckoutService commerceCheckoutService;
	private static final String XML_NAMESPACE_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	private CustomerAccountService customerAccountService;
	private String approvalStatus=null;

	@Override
	public String getApprovalStatus() {
		// TODO Auto-generated method stub
		return approvalStatus;
	}

	@Override
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus=approvalStatus;
		
	}

	@Override
	public PaymentTransactionEntryModel savePaymentDetails(final Response reponse, final AbstractOrderModel orderModel,
			final PaymentTransactionType paymentTransactionType, final OrderStatus orderStatus, final Request request, 
			final String transCode, String paymentInfoCode, Double amount)
	{
		LOG.error("Calling savePaymentDetails...for the card #: ");
		validateParameterNotNull(reponse, "Response cannot be null");
		validateParameterNotNull(orderModel, "orderModel cannot be null");
		List<PaymentTransactionModel> paymentTransactionModels = null;
		final CustomerModel customerModel = (CustomerModel) orderModel.getUser();
		CreditCardPaymentInfoModel ccPaymentInfoModel=null;
		
		if(paymentTransactionType.equals(PaymentTransactionType.CARD_VERIFICATION))
		{
			ccPaymentInfoModel = getCustomerAccountService().getCreditCardPaymentInfoForCode(
					customerModel, paymentInfoCode);
		}else {
			ccPaymentInfoModel = (CreditCardPaymentInfoModel)orderModel.getPaymentInfo();
		}
		
		validateParameterNotNull(ccPaymentInfoModel, "ccPaymentInfoModel cannot be null");

		final PaymentTransactionModel transaction = getModelService().create(PaymentTransactionModel.class);
		//set consignment id
		transaction.setCode(transCode);
		validateParameterNotNull(request.getNewOrder().getCustomerRefNum(), "customerRefNum cannot be null");
		transaction.setRequestId(request.getNewOrder().getCustomerRefNum());

		transaction.setInfo(ccPaymentInfoModel);
		transaction.setPaymentProvider(getCardBrand(ccPaymentInfoModel.getType()));		
		
		transaction.setOwner(customerModel);
		transaction.setOrder(orderModel);
		getModelService().save(transaction);
		final PaymentTransactionEntryModel entry = getModelService().create(PaymentTransactionEntryModel.class);
		entry.setType(paymentTransactionType);
		if(reponse.getNewOrderResp() != null)
		{
			entry.setRequestId(reponse.getNewOrderResp().getTxRefNum());
			entry.setRequestToken(reponse.getNewOrderResp().getTxRefNum());
			entry.setSubscriptionID(reponse.getNewOrderResp().getTxRefNum());
		}
		final Date date = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT")).getTime();
		entry.setTime(date);
		entry.setPaymentTransaction(transaction);
		
		// Set Transaction status as per PT Response
		addTransactionStatus(entry, reponse);
		
		entry.setCurrency(orderModel.getCurrency());
		entry.setOwner(transaction);
		entry.setAmount(new BigDecimal(amount.doubleValue()));
		//		addAuthorizedAmount(entry, parameters, cartModel);
		

		entry.setCode(request.getNewOrder().getOrderID());
		getModelService().save(entry);
		getModelService().refresh(transaction);
		if (orderModel.getPaymentTransactions() != null)
		{
			paymentTransactionModels = new ArrayList<PaymentTransactionModel>(orderModel.getPaymentTransactions());
		}
		else
		{
			paymentTransactionModels = new ArrayList<PaymentTransactionModel>();
		}

		paymentTransactionModels.add(transaction);
		orderModel.setPaymentTransactions(paymentTransactionModels);
		orderModel.setStatus(orderStatus);
		
		getModelService().saveAll(orderModel);
		return entry;
	}

	@Override
	public PaymentTransactionEntryModel updateSettlement(final Response reponse, final PaymentTransactionModel transaction,
			final PaymentTransactionEntryModel transactionEntry, final PaymentTransactionType paymentTransactionType,
			final OrderStatus orderStatus)
	{
		validateParameterNotNull(reponse, "Response cannot be null");

		final PaymentTransactionEntryModel entry = getModelService().create(PaymentTransactionEntryModel.class);
		entry.setType(paymentTransactionType);
		if(reponse.getMarkForCaptureResp() != null)
		{
			entry.setRequestId(reponse.getMarkForCaptureResp().getTxRefNum());
			entry.setRequestToken(reponse.getMarkForCaptureResp().getTxRefNum());
			entry.setSubscriptionID(reponse.getMarkForCaptureResp().getTxRefNum());
		}
		final Date date = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT")).getTime();
		entry.setTime(date);
		entry.setPaymentTransaction(transaction);
		addTransactionStatus(entry, reponse);
		entry.setCurrency(transactionEntry.getCurrency());
		entry.setOwner(transaction);
		entry.setAmount(transactionEntry.getAmount());
		//		addAuthorizedAmount(entry, parameters, cartModel);
		

		entry.setCode(transactionEntry.getCode());

		getModelService().save(entry);
		getModelService().refresh(transaction);
		return entry;
	}

	private String getCardBrand(final CreditCardType cardType)
	{
		if (cardType != null)
		{
			if (cardType.getCode().equalsIgnoreCase(PaymentechintegrationConstants.STR_MASTERCARD))
			{
				return PaymentechintegrationConstants.PAYMENT_PROVIDER_MC;
			}else if (cardType.getCode().equalsIgnoreCase(PaymentechintegrationConstants.STR_AMEX))
			{
				return PaymentechintegrationConstants.PAYMENT_PROVIDER_AMEX;
			}
			else if (cardType.getCode().equalsIgnoreCase(PaymentechintegrationConstants.STR_VISA))
			{
				return PaymentechintegrationConstants.PAYMENT_PROVIDER_VISA;
			}
			else if (cardType.getCode().equalsIgnoreCase(PaymentechintegrationConstants.STR_DISCOVER_LOWERCASE))
			{
				return PaymentechintegrationConstants.PAYMENT_PROVIDER_DISCOVER;
			}
		}

		return PaymentechintegrationConstants.PAYMENT_PROVIDER;
	}
	
	private void addTransactionStatus(final PaymentTransactionEntryModel entry, final Response reponse)
	{
		TransactionStatus status = TransactionStatus.ERROR;
		TransactionStatusDetails details = TransactionStatusDetails.COMMUNICATION_PROBLEM;
		if (reponse.getQuickResp() != null)
		{
			status = TransactionStatus.REJECTED;
			details = TransactionStatusDetails.REVIEW_NEEDED;
		}
		// set the Transaction status according to the PT Responses.
		if (reponse.getNewOrderResp() != null)
		{
			//accountVerify & authorizePayment
			//4.2 New Order Response Elements
			String approvalStatus=reponse.getNewOrderResp().getApprovalStatus();
			if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("0")){
				//approvalStatus="DECLINED";
				status = TransactionStatus.REJECTED;
				details = TransactionStatusDetails.BANK_DECLINE;
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("1")){
				//approvalStatus="APPROVED";
				status = TransactionStatus.ACCEPTED;
				details = TransactionStatusDetails.SUCCESFULL;
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("2")){
				//approvalStatus="Message/System Error";
				status = TransactionStatus.ERROR;
				details = TransactionStatusDetails.COMMUNICATION_PROBLEM;
			}
		}
		 
		if (reponse.getMarkForCaptureResp() != null)
		{
			//capturePayment
			//4.4 Mark for Capture Response Elements
			String approvalStatus=reponse.getMarkForCaptureResp().getApprovalStatus();
			if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("0")){
				//approvalStatus="DECLINED";
				status = TransactionStatus.REJECTED;
				details = TransactionStatusDetails.BANK_DECLINE;
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("1")){
				//approvalStatus="APPROVED";
				status = TransactionStatus.ACCEPTED;
				details = TransactionStatusDetails.SUCCESFULL;
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("2")){
				//approvalStatus="Message/System Error";
				status = TransactionStatus.ERROR;
				details = TransactionStatusDetails.COMMUNICATION_PROBLEM;
			}
		}
		if (reponse.getQuickResp() != null)
		{
			//capturePayment
			//4.4 Mark for Capture Response Elements
			String approvalStatus=reponse.getQuickResp().getApprovalStatus();
			if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("0")){
				//approvalStatus="DECLINED";
				status = TransactionStatus.REJECTED;
				details = TransactionStatusDetails.BANK_DECLINE;
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("1")){
				//approvalStatus="APPROVED";
				status = TransactionStatus.ACCEPTED;
				details = TransactionStatusDetails.SUCCESFULL;
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("2")){
				//approvalStatus="Message/System Error";
				status = TransactionStatus.ERROR;
				details = TransactionStatusDetails.COMMUNICATION_PROBLEM;
			}
		}
		entry.setTransactionStatus(status.name());
		entry.setTransactionStatusDetails(details.name());
	}


	@Override
	public boolean accountVerify(final Request request, final String transCode, String paymentInfoCode)
	{
		final CartModel cartModel = getCartService().getSessionCart();
		String requestObject=jaxbToXML(request);
		LOG.error("Account Verification Request XML: "+requestObject);
		final Response response = getPtSecurePaymentClient().authPayment(requestObject);
		OrderStatus orderStatus;
		PaymentTransactionType paymentTransactionType;
		boolean accountVerifyFalg=false;
		if(response!=null && null!=response.getNewOrderResp())
		{
			String approvalStatus=response.getNewOrderResp().getApprovalStatus();
			String definitionStatus=response.getNewOrderResp().getStatusMsg();
			String respCode=response.getNewOrderResp().getRespCode();
			String hostRespCode=response.getNewOrderResp().getHostRespCode();
			String authCode=response.getNewOrderResp().getAuthCode();
			if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("0")){
				approvalStatus="DECLINED";
				orderStatus=OrderStatus.REJECTED_BY_MERCHANT;
 				paymentTransactionType=PaymentTransactionType.CARD_VERIFICATION;
 				accountVerifyFalg=false;
 				setApprovalStatus("0");
				LOG.error("ERR_NTFY_SUPPORT_0001 - Card Verification has been  "+approvalStatus+ ", Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("1")){
				approvalStatus="APPROVED";
				orderStatus=OrderStatus.APPROVED;
				paymentTransactionType=PaymentTransactionType.CARD_VERIFICATION;
				accountVerifyFalg=true;
				setApprovalStatus("1");
				LOG.error("Card Verification has been  "+approvalStatus+ ", Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("2")){
				approvalStatus="Message/System Error";
				orderStatus=OrderStatus.PAYMENT_NOT_AUTHORIZED;
				paymentTransactionType=PaymentTransactionType.CARD_VERIFICATION;
				accountVerifyFalg=false;
				setApprovalStatus("2");
				LOG.error("ERR_NTFY_SUPPORT_0001 - Card Verification has been  "+approvalStatus+ ", Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}else{
				approvalStatus="Message/System Error";
				orderStatus=OrderStatus.PAYMENT_NOT_AUTHORIZED;
				paymentTransactionType=PaymentTransactionType.CARD_VERIFICATION;
				accountVerifyFalg=false;
				setApprovalStatus("2");
				LOG.error("ERR_NTFY_SUPPORT_0001 - Card Verification has been  "+approvalStatus+ ", Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}
			try
			{
				savePaymentDetails(response, cartModel,paymentTransactionType, 
						orderStatus, request, transCode+ "-" + UUID.randomUUID(), paymentInfoCode, new Double(0.0));
			}catch(Exception e)
			{
				LOG.error("Following error occured while Saving Account Verified Transaction \n {}", e);
				LOG.error("ERR_NTFY_SUPPORT_0001 - Card Verification failed for cart <"+cartModel.getCode()+">");
				return false;
			}
			
			/*if (response.getQuickResp() == null)
			{
				try
				{
					savePaymentDetails(response, cartModel, PaymentTransactionType.CARD_VERIFICATION, 
							OrderStatus.APPROVED, request, transCode+ "-" + UUID.randomUUID(), paymentInfoCode, new Double(0.0));
				}catch(Exception e)
				{
					LOG.error("Following error occured while Saving Account Verified Transaction \n {}", e);
					return false;
				}
			}else
			{
				savePaymentDetails(response, cartModel, PaymentTransactionType.CARD_VERIFICATION, 
						OrderStatus.APPROVED, request, transCode+ "-" + UUID.randomUUID(), paymentInfoCode, new Double(0.0));
				LOG.error("ERR_NTFY_SUPPORT_0001 - Card Verification failed for cart <"+cartModel.getCode()+">");
			}*/
			return accountVerifyFalg;
		}else{
			LOG.error("ERR_NTFY_SUPPORT_0001 - Card Verification failed for cart <"+cartModel.getCode()+">");
			return accountVerifyFalg;
		} 
	}

	@Override
	public boolean authorizePayment(final Request request, final String transCode, final ConsignmentModel consignment)
	{
		final Response response = getPtSecurePaymentClient().authPayment(jaxbToXML(request));
		OrderStatus orderStatus;
		boolean authorizePaymentFlag=false;
		PaymentTransactionType paymentTransactionType;
		if(response.getNewOrderResp() != null){
			if(response.getQuickResp()!=null){
				LOG.error("Payment Authorization getNewOrderResp >>  getQuickResp >> getApprovalStatus "+response.getQuickResp().getApprovalStatus());
				LOG.error("Payment Authorization getNewOrderResp >>  getQuickResp >> getProcStatus "+response.getQuickResp().getProcStatus());
				LOG.error("Payment Authorization getNewOrderResp >>  getQuickResp >> getProfileProcStatus "+response.getQuickResp().getProfileProcStatus());
				LOG.error("Payment Authorization getNewOrderResp >>  getQuickResp >> getQuickResp "+response.getQuickResp().getStatusMsg());
			}
			String approvalStatus=response.getNewOrderResp().getApprovalStatus();
			String definitionStatus=response.getNewOrderResp().getStatusMsg();
			String respCode=response.getNewOrderResp().getRespCode();
			String hostRespCode=response.getNewOrderResp().getHostRespCode();
			String authCode=response.getNewOrderResp().getAuthCode();
			String orderId=response.getNewOrderResp().getOrderID();
			LOG.error("Payment Authorization "+approvalStatus+ " for order id=["+orderId+"] Consingment code [+"+consignment.getCode()+" ] Order Code [ "+consignment.getOrder().getCode()+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode);
			if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("0")){
				 approvalStatus="DECLINED";
				 orderStatus=OrderStatus.PAYMENT_NOT_AUTHORIZED;
				 paymentTransactionType=PaymentTransactionType.AUTHORIZATION;
				 authorizePaymentFlag=false;
				LOG.error("ERR_NTFY_SUPPORT_0002 - Payment Authorization has been "+approvalStatus+ " for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("1")){
				 approvalStatus="APPROVED";
				 orderStatus=OrderStatus.PAYMENT_AUTHORIZED;
				 paymentTransactionType=PaymentTransactionType.AUTHORIZATION;
				 authorizePaymentFlag=true;
				LOG.error("Payment Authorization has been "+approvalStatus+ " for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("2")){
				 approvalStatus="Message/System Error";
				 orderStatus=OrderStatus.PAYMENT_NOT_AUTHORIZED;
				 paymentTransactionType=PaymentTransactionType.AUTHORIZATION;
				 authorizePaymentFlag=false;
				LOG.error("ERR_NTFY_SUPPORT_0002 - Payment Authorization has been "+approvalStatus+ " for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}else{
				 approvalStatus="Message/System Error";
				 orderStatus=OrderStatus.PAYMENT_NOT_AUTHORIZED;
				 paymentTransactionType=PaymentTransactionType.AUTHORIZATION;
				 authorizePaymentFlag=false;
				LOG.error("ERR_NTFY_SUPPORT_0002 - Payment Authorization has been "+approvalStatus+ " for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}
			try
			{
				savePaymentDetails(response, consignment.getOrder(),paymentTransactionType, 
						orderStatus, request,transCode, StringUtils.EMPTY, consignment.getConsignmentValue());
			}catch(Exception e){
				LOG.error("ERR_NTFY_SUPPORT_0002 - Payment Authorization failure for order <"+consignment.getOrder().getCode()+"> "
						+ "and consignment number <"+consignment.getCode()+">");
				LOG.error("Following error occured while Saving Authrorized Transaction \n {}", e);
			}
			/*if (response.getQuickResp() == null)
			{
				try
				{
					LOG.error("Executing savePaymentDetails with PaymentTransactionType as AUTHORIZATION and OrderStatus.PAYMENT_AUTHORIZED");
					savePaymentDetails(response, consignment.getOrder(), PaymentTransactionType.AUTHORIZATION, 
							OrderStatus.PAYMENT_AUTHORIZED, request,transCode, StringUtils.EMPTY, consignment.getConsignmentValue());
				}catch(Exception e){
					LOG.error("Following error occured while Saving Authrorized Transaction \n {}", e);
				}
			}else
			{
				LOG.error("Executing savePaymentDetails with PaymentTransactionType as AUTHORIZATION and OrderStatus.PAYMENT_NOT_AUTHORIZED");
				savePaymentDetails(response, consignment.getOrder(), PaymentTransactionType.AUTHORIZATION, 
						OrderStatus.PAYMENT_NOT_AUTHORIZED, request,transCode, StringUtils.EMPTY, consignment.getConsignmentValue());
				LOG.error("ERR_NTFY_SUPPORT_0002 - Payment Authorization failure for order <"+consignment.getOrder().getCode()+"> "
						+ "and consignment number <"+consignment.getCode()+">");
			}*/

			return authorizePaymentFlag;
		}else{
			LOG.error("ERR_NTFY_SUPPORT_0002 - Payment Authorization failure for order <"+consignment.getOrder().getCode()+"> "
					+ "and consignment number <"+consignment.getCode()+">");
			return authorizePaymentFlag;
		}
	}
	private boolean getMarkForCatureResponse(final Response response , final PaymentTransactionEntryModel transactionEntry){
		OrderStatus orderStatus;
		PaymentTransactionType paymentTransactionType;
		boolean capturePaymentFlag=false;
		try
		{
			if(response.getMarkForCaptureResp()!=null){
			String approvalStatus=response.getMarkForCaptureResp().getApprovalStatus();
			String definitionStatus=response.getMarkForCaptureResp().getStatusMsg();
			String respCode=response.getMarkForCaptureResp().getRespCode();
			String hostRespCode=response.getMarkForCaptureResp().getHostRespCode();
			String authCode=response.getMarkForCaptureResp().getAuthCode();
			String orderId=response.getMarkForCaptureResp().getOrderID();
			if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("0")){
				approvalStatus="DECLINED";
				orderStatus=OrderStatus.PAYMENT_NOT_CAPTURED;
				paymentTransactionType=PaymentTransactionType.CAPTURE;
				capturePaymentFlag=false;
				updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, paymentTransactionType,orderStatus);
				LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("1")){
				approvalStatus="APPROVED";
				orderStatus=OrderStatus.PAYMENT_CAPTURED;
				paymentTransactionType=PaymentTransactionType.CAPTURE;
				capturePaymentFlag=true;
				updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, paymentTransactionType,orderStatus);
				LOG.error("Payment Capture for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}else if(null !=approvalStatus && approvalStatus.equalsIgnoreCase("2")){
				approvalStatus="Message/System Error";
				orderStatus=OrderStatus.PAYMENT_NOT_CAPTURED;
				paymentTransactionType=PaymentTransactionType.CAPTURE;
				capturePaymentFlag=false;
				updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, paymentTransactionType,orderStatus);
				LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}else{
				approvalStatus="Message/System Error";
				orderStatus=OrderStatus.PAYMENT_NOT_CAPTURED;
				paymentTransactionType=PaymentTransactionType.CAPTURE;
				capturePaymentFlag=false;
				updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, paymentTransactionType,orderStatus);
				LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
			}
		  }
		}catch(Exception e){
				String concatedCode = transactionEntry.getPaymentTransaction().getCode();
				int dashIndex = transactionEntry.getCode().indexOf("-");
				
				LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture failure for order <"+concatedCode.substring(0, dashIndex)
						+"> and consignment number <"+concatedCode.substring(dashIndex+1, concatedCode.length())+">");
				
				LOG.error("Following error occured while Saving Payment Captured Transaction \n {}", e);
			}
		return capturePaymentFlag;
	}
	private boolean getQuickRespFlag(final Response response , final PaymentTransactionEntryModel transactionEntry){
		boolean capturePaymentFlag=false;
		LOG.error("Exeucting getQuickRespFlag >>> ");
		OrderStatus orderStatus;
		PaymentTransactionType paymentTransactionType;
		try {
			if(null!=response.getQuickResp()){

				String approvalStatus=response.getQuickResp().getApprovalStatus();
				String definitionStatus=response.getQuickResp().getStatusMsg();
				String respCode="";
				String hostRespCode="";
				String orderId=response.getQuickResp().getOrderID();
					LOG.error("Payment Authorization getNewOrderResp >>  getQuickRespFlag >> getApprovalStatus "+response.getQuickResp().getApprovalStatus());
					LOG.error("Payment Authorization getNewOrderResp >>  getQuickRespFlag >> getProcStatus "+response.getQuickResp().getProcStatus());
					LOG.error("Payment Authorization getNewOrderResp >>  getQuickRespFlag >> getProfileProcStatus "+response.getQuickResp().getProfileProcStatus());
					LOG.error("Payment Authorization getNewOrderResp >>  getQuickRespFlag >> getQuickResp "+response.getQuickResp().getStatusMsg());
				
				if(approvalStatus!=null && approvalStatus.equalsIgnoreCase("0")){
					 approvalStatus="DECLINED";
					 orderStatus=OrderStatus.PAYMENT_NOT_CAPTURED;
					 paymentTransactionType=PaymentTransactionType.CAPTURE;
					 capturePaymentFlag=false;
					 updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, paymentTransactionType,orderStatus);
					 LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture (getQuickRespFlag) for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
				}else if(approvalStatus!=null && approvalStatus.equalsIgnoreCase("1")){
					 approvalStatus="APPROVED";
					 orderStatus=OrderStatus.PAYMENT_CAPTURED;
					 paymentTransactionType=PaymentTransactionType.CAPTURE;
					 capturePaymentFlag=true;
					 updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, paymentTransactionType,orderStatus);
					 LOG.error("Payment Capture (getQuickRespFlag) for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
				}else if(approvalStatus!=null && approvalStatus.equalsIgnoreCase("2")){
					 approvalStatus="Message/System Error";
					 orderStatus=OrderStatus.PAYMENT_NOT_CAPTURED;
					 paymentTransactionType=PaymentTransactionType.CAPTURE;
					 capturePaymentFlag=false;
					 updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, paymentTransactionType,orderStatus);
					 LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture (getQuickRespFlag) for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
				}else{
					 approvalStatus="Message/System Error";
					 orderStatus=OrderStatus.PAYMENT_NOT_CAPTURED;
					 paymentTransactionType=PaymentTransactionType.CAPTURE;
					 capturePaymentFlag=false;
					 updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, paymentTransactionType,orderStatus);
					 LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture (getQuickRespFlag) for order id=["+orderId+"] and Approval Status is:"+approvalStatus+ " for order, Reason: <Approval Status Code> "+approvalStatus + " <DefinitionStatus> "+definitionStatus+ " <RespCode> "+respCode+" <HostRespCode> "+hostRespCode +"Order Status "+orderStatus);
				}
			}
		} catch (Exception exception) {
			String concatedCode = transactionEntry.getPaymentTransaction().getCode();
			int dashIndex = transactionEntry.getCode().indexOf("-");
			LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture (getQuickRespFlag) failure for order < "+concatedCode.substring(0, dashIndex)
					+" > and consignment number <"+concatedCode.substring(dashIndex+1, concatedCode.length())+">");
			
			LOG.error("Following error occured while Saving Payment Capture (getQuickRespFlag) Transaction \n {} ", exception);
		}

		return capturePaymentFlag;
	}
	
	@Override
	public boolean capturePayment(final Request request, final PaymentTransactionEntryModel transactionEntry)
	{ 
		final String capturePaymentRequet=jaxbToXML(request);
		boolean capturePaymentFlag=false;
		if(StringUtils.isNotEmpty(capturePaymentRequet))
		{
			final Response response = getPtSecurePaymentClient().authPayment(capturePaymentRequet);
			LOG.error("Capture Payment in progress" + capturePaymentRequet);
			if(response!=null){
				if(response.getMarkForCaptureResp()!=null){
					LOG.error("Capture Payment in >> response.getMarkForCaptureResp()" );
					capturePaymentFlag=getMarkForCatureResponse(response, transactionEntry);
					LOG.error("Capture Payment in >> response.getMarkForCaptureResp() >> Completed" );
				}
				if(response.getQuickResp()!=null){
					LOG.error("Capture Payment in >> response.getQuickResp()" );
					capturePaymentFlag=getQuickRespFlag(response, transactionEntry);
					LOG.error("Capture Payment in >> response.getQuickResp() >> Completed" );
				}
			}else{
				String concatedCode = transactionEntry.getPaymentTransaction().getCode();
				int dashIndex = transactionEntry.getCode().indexOf("-");
				
				LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture failure for order <"+concatedCode.substring(0, dashIndex)
						+"> and consignment number <"+concatedCode.substring(dashIndex+1, concatedCode.length())+">");
				return capturePaymentFlag;
			}
		}
		return capturePaymentFlag;
	}
/*if (response.getQuickResp() == null)
{
	try
	{
	LOG.error("Exeucting UpdateSettlement ...as PaymentTransactionType.CAPTURE  with OrderStatus.PAYMENT_CAPTURED: ");
	updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, PaymentTransactionType.CAPTURE,
			OrderStatus.PAYMENT_CAPTURED);
	}catch(Exception e){
		LOG.error("Following error occured while Saving Payment Captured Transaction \n {}", e);
	}
}else
{
	LOG.error("Exeucting UpdateSettlement ...as PaymentTransactionType.CAPTURE  with OrderStatus.PAYMENT_NOT_CAPTURED: ");
	updateSettlement(response, transactionEntry.getPaymentTransaction(), transactionEntry, PaymentTransactionType.CAPTURE,
			OrderStatus.PAYMENT_NOT_CAPTURED);
	String concatedCode = transactionEntry.getPaymentTransaction().getCode();
	int dashIndex = transactionEntry.getCode().indexOf("-");
	
	LOG.error("ERR_NTFY_SUPPORT_0003 - Payment Capture failure for order <"+concatedCode.substring(0, dashIndex)
			+"> and consignment number <"+concatedCode.substring(dashIndex+1, concatedCode.length())+">");
}*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.paymentech.hop.services.PTAPIPaymentService#createNewOrderRequest(com.paymentech.payment.data.NewOrderData)
	 */
	@Override
	public Request createNewOrderRequest(final NewOrderData newOrderData)
	{
		final ObjectFactory factory = new ObjectFactory();

		final Request request = factory.createRequest();
		final NewOrderType newOrder = factory.createNewOrderType();

		newOrder.setOrbitalConnectionUsername(getConnectionUsername());
		newOrder.setOrbitalConnectionPassword(getConnectionPassword());
		newOrder.setIndustryType(ValidIndustryTypes.valueOf(getIndustryType()));
		if (newOrderData.getPaymentReqType().equals(PaymentechintegrationConstants.TRANS_TYPE_CAPTURE))
		{
			newOrder.setMessageType(ValidTransTypes.AC);
		}
		else
		{
			newOrder.setMessageType(ValidTransTypes.A);
		}
		newOrder.setBIN(getBIN());
		newOrder.setMerchantID(getMerchantID(newOrderData.getCurrencyCode()));
		newOrder.setTerminalID(getTerminalID());
		//		newOrder.setExp(newOrderData.getCardExpiryDate());
		newOrder.setCurrencyCode(newOrderData.getCurrencyCode());
		newOrder.setCurrencyExponent("2");
		newOrder.setCustomerRefNum(newOrderData.getCustomerRefNo());
		newOrder.setOrderID(newOrderData.getOrderID());
		newOrder.setAmount(newOrderData.getAmount());

		request.setNewOrder(newOrder);
		return request;
	}

	@Override
	public Request createMarkForCaptureRequest(final MarkForCaptureData markForCaptureData){
		final ObjectFactory factory = new ObjectFactory();
		final Request request = factory.createRequest();
		MarkForCaptureType markForCaptureType = factory.createMarkForCaptureType();
		
		markForCaptureType.setOrbitalConnectionUsername(getConnectionUsername());
		markForCaptureType.setOrbitalConnectionPassword(getConnectionPassword());
		markForCaptureType.setOrderID(markForCaptureData.getOrderID());
		markForCaptureType.setAmount(markForCaptureData.getAmount());
		markForCaptureType.setBIN(getBIN());
		markForCaptureType.setMerchantID(getMerchantID(markForCaptureData.getCurrencyCode()));
		markForCaptureType.setTerminalID(getTerminalID());
		markForCaptureType.setTxRefNum(markForCaptureData.getTxRefNum());
		request.setMarkForCapture(markForCaptureType);
		return request;
		
	}
	
	@Override
	public String jaxbToXML(final Request request)
	{
		try
		{
			final JAXBContext context = JAXBContext.newInstance(Request.class);
			final Marshaller m = context.createMarshaller();
			m.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			final StringWriter sw = new StringWriter();
			sw.write(XML_NAMESPACE_HEADER);
			m.marshal(request, sw);
			final String xmlString = sw.toString();
			LOG.error("Payment Tech Request: ["+xmlString+"]");
			return xmlString;
		}
		catch (final JAXBException e)
		{
			LOG.error("Error occurred while generating XML from Jaxb object {}", e);
		}
		return null;
	}

	@Override
	public Response xmlToJAXBObject(final String xml)
	{
		try
		{
			final JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			final StringReader reader = new StringReader(xml);
			final Response response = (Response) unmarshaller.unmarshal(reader);
			return response;
		}
		catch (final JAXBException e)
		{
			LOG.error("Error occurred while unmarshalling to Jaxb object {}", e);
		}
		return null;
	}

	private String getConnectionUsername()
	{
		return configService.getConfiguration().getString("paymentech.config.postXml.authorize.con.username", "dummyuser");
	}

	private String getConnectionPassword()
	{
		return configService.getConfiguration().getString("paymentech.config.postXml.authorize.con.password", "password");
	}

	private String getIndustryType()
	{
		return configService.getConfiguration().getString("paymentech.config.postXml.authorize.industryType", "N");
	}

	private String getBIN()
	{
		return configService.getConfiguration().getString("paymentech.config.postXml.authorize.BIN", "N");
	}

	private String getMerchantID(String currencyCode)
	{
		return configService.getConfiguration().getString("paymentech.config.postXml.authorize.merchantID." + currencyCode, "N");
	}

	private String getTerminalID()
	{
		return configService.getConfiguration().getString("paymentech.config.postXml.authorize.terminalID", "terminalID");
	}


	/**
	 * @return the ptSecurePaymentClient
	 */
	public PTSecurePaymentClient getPtSecurePaymentClient()
	{
		return ptSecurePaymentClient;
	}

	/**
	 * @param ptSecurePaymentClient
	 *           the ptSecurePaymentClient to set
	 */
	@Required
	public void setPtSecurePaymentClient(final PTSecurePaymentClient ptSecurePaymentClient)
	{
		this.ptSecurePaymentClient = ptSecurePaymentClient;
	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}


	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the commerceCheckoutService
	 */
	public CommerceCheckoutService getCommerceCheckoutService()
	{
		return commerceCheckoutService;
	}

	/**
	 * @param commerceCheckoutService
	 *           the commerceCheckoutService to set
	 */
	@Required
	public void setCommerceCheckoutService(final CommerceCheckoutService commerceCheckoutService)
	{
		this.commerceCheckoutService = commerceCheckoutService;
	}

	/**
	 * @return the configService
	 */
	public ConfigurationService getConfigService()
	{
		return configService;
	}

	/**
	 * @param configService the configService to set
	 */
	@Required
	public void setConfigService(ConfigurationService configService)
	{
		this.configService = configService;
	}
	
	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	@Required
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	

}
