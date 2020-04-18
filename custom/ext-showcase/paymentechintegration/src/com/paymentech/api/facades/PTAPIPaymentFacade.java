package com.paymentech.api.facades;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;


/**
 * @author dipankan
 *
 */
public interface PTAPIPaymentFacade
{
	public boolean accountVerification(final CCPaymentInfoData paymentInfoData);

	public boolean authPayment(ConsignmentModel consignment, String amount);

	public boolean authCapturePayment(ConsignmentModel consignment, PaymentTransactionEntryModel paymentTransactionEntryModel);
	public String getApprovalStatus();
	public void setApprovalStatus(String approvalStatus);
	
}
