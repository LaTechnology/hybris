package com.greenlee.sapintegration.interceptors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.greenlee.pi.data.PIDeliveryData;
import com.greenlee.pi.service.GreenleePIDeliveryService;
import com.paymentech.api.facades.PTAPIPaymentFacade;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * This class validates product reference added to cart entry model and ensures
 * if it must be a warranty product
 *
 * @author dipankan sahoo
 *
 */
public class GreenleeDeliveryConsignmentValidateInterceptor implements
		ValidateInterceptor<ConsignmentModel> {

	private static final Logger LOG = Logger
			.getLogger(GreenleeDeliveryConsignmentValidateInterceptor.class);
	private ModelService modelService;
	private PTAPIPaymentFacade ptAPIPaymentFacade;
	private GreenleePIDeliveryService greenleePIDeliveryService;
	private static final String STATUS_FAILURE = "ERROR";

	/**
	 * Validates whether the consignment is the first one got created for order
	 * Then checks whether the consignment has an payment transaction entry, if
	 * not create one Amount recalculation and saves in to payment transaction
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void onValidate(final ConsignmentModel consignment,
			final InterceptorContext ctx) throws InterceptorException {
		if (isReadyForPaymentAuth(consignment)) {
			boolean isPaymentAuthorized = false;

			final AbstractOrderModel orderModel = consignment.getOrder();
			double shippingCost = 0.0;
			double amortizedDuty = 0.0;
			double totalPrice = 0.0;
			double aTax = 0.0;

			final Double tax = orderModel.getTotalTax();
			final Double dutyCharge = orderModel.getTotalDuty();

			if (orderModel.getConsignments().size() == 1) {
				shippingCost = orderModel.getDeliveryCost().doubleValue();
			}
			/*
			 * if
			 * (CollectionUtils.isNotEmpty(consignment.getConsignmentEntries()))
			 * { for (final ConsignmentEntryModel consignmentEntryModel :
			 * consignment.getConsignmentEntries()) { totalPrice +=
			 * consignmentEntryModel
			 * .getOrderEntry().getBasePrice().doubleValue()
			 * consignmentEntryModel.getQuantity().intValue(); aTax +=
			 * getAmortizedAmount(tax, getTotalQuantity(orderModel),
			 * consignmentEntryModel.getQuantity().doubleValue()); amortizedDuty
			 * += getAmortizedAmount(dutyCharge, getTotalQuantity(orderModel),
			 * consignmentEntryModel .getQuantity().doubleValue()); } }
			 */
			BigDecimal totalAmount = new BigDecimal(0.0);
			if (consignment.getConsignmentValue() != null
					&& consignment.getConsignmentValue().doubleValue() > 0) {
				totalAmount = new BigDecimal(consignment.getConsignmentValue())
						.setScale(2, RoundingMode.HALF_DOWN);
			}
			// authorize the payment
			isPaymentAuthorized = getPtAPIPaymentFacade().authPayment(
					consignment, netAmount(totalAmount));
			LOG.error("Authorize the payment...."
					+ consignment.getOrder().getCode() + "> "
					+ "and consignment number <" + consignment.getCode()
					+ "] Payment Tech Transaction Statuses ["
					+ isPaymentAuthorized + " ]");

			if (isPaymentAuthorized) {
				final PIDeliveryData piDeliveryData = new PIDeliveryData();
				piDeliveryData.setOrderNumber(consignment.getOrder().getCode());
				piDeliveryData.setConsignmentNumber(consignment.getCode());
				String transactionRef=getTransactionReference(consignment);
				StringBuilder builder = new StringBuilder(consignment.getCode());
				builder.append(",");
				builder.append(transactionRef);
				piDeliveryData.setDeliveryNo(builder.toString());
				LOG.error("ERR_NTFY_SUPPORT_00026 - PI Delivery block removal service <"
						+ consignment.getOrder().getCode()
						+ "> "
						+ "and consignment number <"
						+ consignment.getCode()
						+ "] Payment Tech Transaction Statuses ["
						+ getTransactionReference(consignment) + " ]");

				// PI Delivery block removal service expects the request with
				// "<<Consignment_No,Transaction_Reference_No>>"
				final PIDeliveryData deliveryData = getGreenleePIDeliveryService().piRemoveDelivery(piDeliveryData);
				/*
				 * if(!StringUtils.equalsIgnoreCase(deliveryData.getStatus(),
				 * STATUS_FAILURE)){
				 * consignment.setStatus(ConsignmentStatus.DELIVERY_BLOCK_RELEASED
				 * ); getModelService().save(consignment); }
				 */
				LOG.error("ERR_NTFY_SUPPORT_0004 - PI Delivery block removal service completed <"
						+ consignment.getOrder().getCode()
						+ "> "
						+ "and consignment number <"
						+ consignment.getCode()
						+ "] Payment Tech Transaction Refer # ["
						+ transactionRef + " ]");

			}
		}

	}

	private String netAmount(final BigDecimal amount) {
		return amount.toString().replace(".", "");
	}

	private String getTransactionReference(final ConsignmentModel consignment) {
		if (consignment != null) {
			final String tCode = consignment.getOrder().getCode() + "-"
					+ consignment.getCode();
			final List<PaymentTransactionModel> paymentTransactionList = consignment
					.getOrder().getPaymentTransactions();
			if (paymentTransactionList != null
					&& !paymentTransactionList.isEmpty()) {
				for (final PaymentTransactionModel paymentTransactionModel : paymentTransactionList) {
					if (paymentTransactionModel.getInfo() instanceof CreditCardPaymentInfoModel) {
						final List<PaymentTransactionEntryModel> paymentTransactionEntryModelList = paymentTransactionModel
								.getEntries();
						for (final PaymentTransactionEntryModel entry : paymentTransactionEntryModelList) {
							// partialCapturePayment
							if (tCode.equals(entry.getCode())
									&& entry.getType()
											.equals(PaymentTransactionType.AUTHORIZATION)
									&& TransactionStatus.ACCEPTED.name()
											.equals(entry
													.getTransactionStatus())) {
								return entry.getRequestId();

							}
						}

					}
				}
			}
		}

		return null;
	}

	private boolean isReadyForPaymentAuth(final ConsignmentModel consignment) {
		boolean authTrans = false;
		boolean cardTypeOrder = false;
		boolean validAuthPayment = false;

		if (consignment != null) {
			final String tCode = consignment.getOrder().getCode() + "-"
					+ consignment.getCode();
			final List<PaymentTransactionModel> paymentTransactionList = consignment
					.getOrder().getPaymentTransactions();
			if (paymentTransactionList != null
					&& !paymentTransactionList.isEmpty()) {
				for (final PaymentTransactionModel paymentTransactionModel : paymentTransactionList) {
					if (paymentTransactionModel.getInfo() instanceof CreditCardPaymentInfoModel) {
						cardTypeOrder = true;
						final List<PaymentTransactionEntryModel> paymentTransactionEntryModelList = paymentTransactionModel
								.getEntries();
						for (final PaymentTransactionEntryModel entry : paymentTransactionEntryModelList) {
							// partialCapturePayment
							if (tCode.equals(entry.getCode())
									&& entry.getType()
											.equals(PaymentTransactionType.AUTHORIZATION)) {
								authTrans = true;
								break;
							}
						}

					}
				}
			}

			validAuthPayment = cardTypeOrder
					&& !authTrans
					&& consignment.getStatus().equals(
							ConsignmentStatus.READY_FOR_PICKUP);
		}

		return validAuthPayment;
	}

	protected double getTotalQuantity(final AbstractOrderModel orderModel) {

		double totalQuantity = 0;
		for (final AbstractOrderEntryModel childEntry : orderModel.getEntries()) {
			totalQuantity += (childEntry.getQuantity() != null ? childEntry
					.getQuantity().longValue() : 0);
		}

		return totalQuantity;
	}

	/*
	 * private double getAmortizedAmount(final Double amount, final double
	 * totalQuantity, final double entryQuantity) { // divide bg1 with bg2 with
	 * 3 scale if (totalQuantity > 0) { final BigDecimal amortizedAmount = (new
	 * BigDecimal(entryQuantity).divide(new BigDecimal(totalQuantity), 2,
	 * RoundingMode.CEILING)).multiply(new BigDecimal(amount.doubleValue()));
	 * return amortizedAmount.doubleValue(); } return 0.0; }
	 */

	/**
	 * @return the modelService
	 */
	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * @param modelService
	 *            the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}

	/**
	 * @return the ptAPIPaymentFacade
	 */
	public PTAPIPaymentFacade getPtAPIPaymentFacade() {
		return ptAPIPaymentFacade;
	}

	/**
	 * @param ptAPIPaymentFacade
	 *            the ptAPIPaymentFacade to set
	 */
	@Required
	public void setPtAPIPaymentFacade(
			final PTAPIPaymentFacade ptAPIPaymentFacade) {
		this.ptAPIPaymentFacade = ptAPIPaymentFacade;
	}

	/**
	 * @return the greenleePIDeliveryService
	 */
	public GreenleePIDeliveryService getGreenleePIDeliveryService() {
		return greenleePIDeliveryService;
	}

	/**
	 * @param greenleePIDeliveryService
	 *            the greenleePIDeliveryService to set
	 */
	@Required
	public void setGreenleePIDeliveryService(
			final GreenleePIDeliveryService greenleePIDeliveryService) {
		this.greenleePIDeliveryService = greenleePIDeliveryService;
	}

}
