/**
 *
 */
package com.paymentech.api.facades.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.paymentech.api.facades.PTAPIPaymentFacade;
import com.paymentech.api.services.PTAPIPaymentService;
import com.paymentech.constants.PaymentechintegrationConstants;
import com.paymentech.jaxb.request.Request;
import com.paymentech.payment.data.MarkForCaptureData;
import com.paymentech.payment.data.NewOrderData;
import com.paymentech.rest.client.PTSecurePaymentClient;


/**
 * @author dipankan
 *
 */
public class DefaultPTAPIPaymentFacade implements PTAPIPaymentFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultPTAPIPaymentFacade.class.getName());
	private CartFacade cartFacade;
	private PTAPIPaymentService ptAPIPaymentService;
	private UserService userService;
	private PTSecurePaymentClient ptSecurePaymentClient;
	private Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;
	private CommonI18NService commonI18NService;

	private String approvalStatus=null;

	@Override
	public String getApprovalStatus() {
		return approvalStatus;
	}

	@Override
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus=approvalStatus;
		
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.paymentech.hop.facades.PTAPIPaymentFacade#accountVerification(de.hybris.platform.commercefacades.order.data
	 * .CartData)
	 */
	@Override
	public boolean accountVerification(final CCPaymentInfoData paymentInfoData)
	{
		
		final NewOrderData newOrderData = setupNewOrderData(paymentInfoData,
				PaymentechintegrationConstants.TRANS_TYPE_ACCOUNT_VERIFICATION);

		final Request request = getPtAPIPaymentService().createNewOrderRequest(newOrderData);
		setApprovalStatus(getPtAPIPaymentService().getApprovalStatus());
		return getPtAPIPaymentService().accountVerify(request, getCartFacade().getSessionCart().getCode(), paymentInfoData.getId());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.paymentech.hop.facades.PTAPIPaymentFacade#authPayment(de.hybris.platform.core.model.order.AbstractOrderModel)
	 */
	@Override
	public boolean authPayment(final ConsignmentModel consignment, final String amount)
	{
		String transCode = consignment.getOrder().getCode()+"-"+consignment.getCode();
		final NewOrderData newOrderData = setupConsignmentNewOrderData(consignment, PaymentechintegrationConstants.TRANS_TYPE_AUTH,
				amount);

		final Request request = getPtAPIPaymentService().createNewOrderRequest(newOrderData);

		return getPtAPIPaymentService().authorizePayment(request,transCode,consignment);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.paymentech.hop.facades.PTAPIPaymentFacade#authCapturePayment(de.hybris.platform.core.model.order.
	 * AbstractOrderModel)
	 */
	@Override
	public boolean authCapturePayment(final ConsignmentModel consignment,
			final PaymentTransactionEntryModel paymentTransactionEntryModel)
	{
		BigDecimal formattedAmount = paymentTransactionEntryModel.getAmount().setScale(2,
				RoundingMode.HALF_DOWN);
		final String amount = paymentTransactionEntryModel != null ? netAmount(formattedAmount) : "0";
		/*final NewOrderData newOrderData = setupConsignmentNewOrderData(consignment,
				PaymentechintegrationConstants.TRANS_TYPE_CAPTURE, amount);*/
		
		MarkForCaptureData markForCaptureData = prepareMarkForCaptureData(consignment, amount);
		final Request request = getPtAPIPaymentService().createMarkForCaptureRequest(markForCaptureData);

		return getPtAPIPaymentService().capturePayment(request, paymentTransactionEntryModel);
	}
	
	private String netAmount(final BigDecimal amount)
	{
		return amount.toString().replace(".","");
	}
	

	private NewOrderData setupNewOrderData(final CCPaymentInfoData paymentInfoData, final String transType)
	{
		final NewOrderData newOrderData = new NewOrderData();
		if (transType.equals(PaymentechintegrationConstants.TRANS_TYPE_ACCOUNT_VERIFICATION))
		{
			newOrderData.setAmount("0");
		}
		newOrderData.setCardExpiryDate(paymentInfoData.getExpiryMonth() + paymentInfoData.getExpiryYear());
		newOrderData.setPaymentReqType(PaymentechintegrationConstants.TRANS_TYPE_ACCOUNT_VERIFICATION);
		newOrderData.setCustomerRefNo(paymentInfoData.getProfileID());
		newOrderData.setOrderID(paymentInfoData.getProfileID());
		final UserModel user = getUserService().getCurrentUser();
		if (user.getSessionCurrency() != null && user.getSessionCurrency().getIsocode().equals("USD"))
		{
			newOrderData.setCurrencyCode(PaymentechintegrationConstants.USD_CURRENCY_CODE);
		}
		else if (user.getSessionCurrency() != null && user.getSessionCurrency().getIsocode().equals("CAD"))
		{
			newOrderData.setCurrencyCode(PaymentechintegrationConstants.CAD_CURRENCY_CODE);
		}
		return newOrderData;
	}

	private NewOrderData setupConsignmentNewOrderData(final ConsignmentModel consignment, 
			final String transType, final String amount)
	{
		final NewOrderData newOrderData = new NewOrderData();
		final AbstractOrderModel orderModel = consignment.getOrder();
		CCPaymentInfoData paymentInfoData = new CCPaymentInfoData();
		PaymentTransactionModel transaction = fetchVerifiedTransaction(orderModel);
		if (orderModel.getPaymentInfo() instanceof CreditCardPaymentInfoModel)
		{
			paymentInfoData = getCreditCardPaymentInfoConverter().convert((CreditCardPaymentInfoModel) orderModel.getPaymentInfo());
		}

		if (transType.equals(PaymentechintegrationConstants.TRANS_TYPE_ACCOUNT_VERIFICATION))
		{
			newOrderData.setAmount("0");
		}
		else
		{
			newOrderData.setAmount(amount);
		}
		newOrderData.setPaymentReqType(transType);
		if (transaction != null)
		{
			newOrderData.setCustomerRefNo(transaction.getRequestId());
			newOrderData.setOrderID(orderModel.getCode()+"-"+consignment.getCode());
		}else{
			newOrderData.setCustomerRefNo(paymentInfoData.getProfileID());
			newOrderData.setOrderID(orderModel.getCode()+"-"+consignment.getCode());
		}
		final UserModel user = orderModel.getUser();
		String curIsoCode = StringUtils.EMPTY;
		if(user.getSessionCurrency() != null)
		{
			curIsoCode = user.getSessionCurrency().getIsocode();
		}else{
			curIsoCode = getCommonI18NService().getCurrentCurrency().getIsocode();
		}
		
		if (curIsoCode.equals("USD"))
		{
			newOrderData.setCurrencyCode(PaymentechintegrationConstants.USD_CURRENCY_CODE);
		}
		else if (curIsoCode.equals("CAD"))
		{
			newOrderData.setCurrencyCode(PaymentechintegrationConstants.CAD_CURRENCY_CODE);
		}
		return newOrderData;
	}
	
	private PaymentTransactionModel fetchVerifiedTransaction(final AbstractOrderModel orderModel)
	{
			final List<PaymentTransactionModel> paymentTransactionList = orderModel.getPaymentTransactions();
			if (paymentTransactionList != null && !paymentTransactionList.isEmpty())
			{
				for (final PaymentTransactionModel paymentTransactionModel : paymentTransactionList)
				{
					if (paymentTransactionModel.getInfo() instanceof CreditCardPaymentInfoModel)
					{
						final List<PaymentTransactionEntryModel> paymentTransactionEntryModelList = paymentTransactionModel
								.getEntries();
						for (final PaymentTransactionEntryModel entry : paymentTransactionEntryModelList)
						{
							//partialCapturePayment
							if (entry.getType().equals(PaymentTransactionType.CARD_VERIFICATION)
									&& TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
							{
								return paymentTransactionModel;

							}
						}

					}
				}
			}

		return null;
	}


	/**
	 * @return the cartFacade
	 */
	public CartFacade getCartFacade()
	{
		return cartFacade;
	}

	/**
	 * @param cartFacade
	 *           the cartFacade to set
	 */
	@Required
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	/**
	 * @return the ptAPIPaymentService
	 */
	public PTAPIPaymentService getPtAPIPaymentService()
	{
		return ptAPIPaymentService;
	}

	/**
	 * @param ptAPIPaymentService
	 *           the ptAPIPaymentService to set
	 */
	@Required
	public void setPtAPIPaymentService(final PTAPIPaymentService ptAPIPaymentService)
	{
		this.ptAPIPaymentService = ptAPIPaymentService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
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

	protected Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> getCreditCardPaymentInfoConverter()
	{
		return creditCardPaymentInfoConverter;
	}

	@Required
	public void setCreditCardPaymentInfoConverter(
			final Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter)
	{
		this.creditCardPaymentInfoConverter = creditCardPaymentInfoConverter;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	/**
	 * @param commonI18NService the commonI18NService to set
	 */
	@Required
	public void setCommonI18NService(CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}
	
	
	private MarkForCaptureData prepareMarkForCaptureData(final ConsignmentModel consignment, 
			 final String amount)
	{
		MarkForCaptureData markForCaptureData = new MarkForCaptureData();
		final AbstractOrderModel orderModel = consignment.getOrder();
		
		markForCaptureData.setAmount(amount);
		
		markForCaptureData.setTxRefNum(getTxRefNum(consignment));
		markForCaptureData.setOrderID(orderModel.getCode()+"-"+consignment.getCode());
		
		final UserModel user = orderModel.getUser();
		String curIsoCode = StringUtils.EMPTY;
		if(user.getSessionCurrency() != null)
		{
			curIsoCode = user.getSessionCurrency().getIsocode();
		}else{
			curIsoCode = getCommonI18NService().getCurrentCurrency().getIsocode();
		}
		
		if (curIsoCode.equals("USD"))
		{
			markForCaptureData.setCurrencyCode(PaymentechintegrationConstants.USD_CURRENCY_CODE);
		}
		else if (curIsoCode.equals("CAD"))
		{
			markForCaptureData.setCurrencyCode(PaymentechintegrationConstants.CAD_CURRENCY_CODE);
		}
		return markForCaptureData;
	}

	private String getTxRefNum(ConsignmentModel consignment) {
		final List<PaymentTransactionModel> paymentTransactionList = consignment.getOrder().getPaymentTransactions();
		final String tCode = consignment.getOrder().getCode() + "-" + consignment.getCode();
		if (paymentTransactionList != null && !paymentTransactionList.isEmpty())
		{
			for (final PaymentTransactionModel paymentTransactionModel : paymentTransactionList)
			{
				if (paymentTransactionModel.getInfo() instanceof CreditCardPaymentInfoModel)
				{
					final List<PaymentTransactionEntryModel> paymentTransactionEntryModelList = paymentTransactionModel
							.getEntries();
					for (final PaymentTransactionEntryModel entry : paymentTransactionEntryModelList)
					{
						if (tCode.equals(entry.getCode()) && entry.getType().equals(PaymentTransactionType.AUTHORIZATION)
								&& TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
						{
							return entry.getRequestId();

						}
					}

				}
			}
		}
		return null;
	}
	
	

}
