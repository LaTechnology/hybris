/**
 *
 */
package com.paymentech.api.services;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import com.paymentech.jaxb.request.Request;
import com.paymentech.jaxb.response.Response;
import com.paymentech.payment.data.MarkForCaptureData;
import com.paymentech.payment.data.NewOrderData;


/**
 * @author dipankan
 *
 */
public interface PTAPIPaymentService
{
	public String getApprovalStatus();
	public void setApprovalStatus(String approvalStatus);
	
	/**
	 * @param reponse
	 * @param orderModel
	 * @param paymentTransactionType
	 * @param paymentStatus
	 * @return paymentTransactionEntryModel
	 */
	public PaymentTransactionEntryModel savePaymentDetails(final Response reponse, final AbstractOrderModel orderModel,
			final PaymentTransactionType paymentTransactionType, final OrderStatus orderStatus, final Request requst, final String transCode, final String paymentInfoCode, final Double amount);

	/**
	 * @param request
	 * @return
	 */
	public boolean accountVerify(final Request request, final String transCode, String paymentInfoCode);

	/**
	 * @param request
	 * @return
	 */
	public boolean authorizePayment(final Request request, final String transCode, final ConsignmentModel consignment);

	/**
	 * @param request
	 * @return
	 */
	public boolean capturePayment(final Request request, final PaymentTransactionEntryModel transactionEntry);

	/**
	 * @param newOrderData
	 * @param transactionType
	 * @return
	 */
	public Request createNewOrderRequest(final NewOrderData newOrderData);

	/**
	 * @param request
	 * @return
	 */
	public String jaxbToXML(final Request request);

	/**
	 * @param xmlResponse
	 * @return
	 */
	public Response xmlToJAXBObject(final String xmlResponse);

	/**
	 * @param reponse
	 * @param orderModel
	 * @param paymentTransactionType
	 * @param paymentStatus
	 * @param request
	 * @return
	 */
	PaymentTransactionEntryModel updateSettlement(Response reponse, final PaymentTransactionModel transaction,
			final PaymentTransactionEntryModel transactionEntry, PaymentTransactionType paymentTransactionType,
			final OrderStatus orderStatus);
	
	
	/**
	 * @param markForCapture
	 * @param transactionType
	 * @return
	 */
	public Request createMarkForCaptureRequest(final MarkForCaptureData markForCaptureData);
}
