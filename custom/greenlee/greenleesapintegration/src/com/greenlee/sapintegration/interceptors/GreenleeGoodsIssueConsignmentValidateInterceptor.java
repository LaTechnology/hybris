package com.greenlee.sapintegration.interceptors;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
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

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.paymentech.api.facades.PTAPIPaymentFacade;


/**
 * This class validates product reference added to cart entry model and ensures if it must be a warranty product
 *
 * @author dipankan sahoo
 *
 */
public class GreenleeGoodsIssueConsignmentValidateInterceptor implements ValidateInterceptor<ConsignmentModel>
{

	private static final Logger LOG = Logger.getLogger(GreenleeGoodsIssueConsignmentValidateInterceptor.class);
	private ModelService modelService;
	private PTAPIPaymentFacade ptAPIPaymentFacade;

	/**
	 * Validates whether the consignment is the first one got created for order Then checks whether the consignment has
	 * an payment transaction entry, if not create one Amount recalculation and saves in to payment transaction
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void onValidate(final ConsignmentModel consignment, final InterceptorContext ctx) throws InterceptorException
	{
		boolean isPaymentCaptured = false;
		if (isReadyForPaymentCapture(consignment))
		{

			final PaymentTransactionEntryModel transactionEntry = fetchAuthTransactionEntry(consignment);

			if (transactionEntry != null)
			//capture the payment
			{
					isPaymentCaptured = getPtAPIPaymentFacade().authCapturePayment(consignment, transactionEntry);
			}
			else
			{
				LOG.error("Error while fetching previously Authorized transaction");
			}

		}

	}

	private boolean isReadyForPaymentCapture(final ConsignmentModel consignment)
	{
		boolean paymentTrans = false;
		boolean validCapturePayment = false;
		boolean cardTypeOrder = false;

		if (consignment != null)
		{
			final String tCode = consignment.getOrder().getCode() + "-" + consignment.getCode();
			final List<PaymentTransactionModel> paymentTransactionList = consignment.getOrder().getPaymentTransactions();
			if (paymentTransactionList != null && !paymentTransactionList.isEmpty())
			{
				for (final PaymentTransactionModel paymentTransactionModel : paymentTransactionList)
				{
					if (paymentTransactionModel.getInfo() instanceof CreditCardPaymentInfoModel)
					{
						cardTypeOrder = true;
						final List<PaymentTransactionEntryModel> paymentTransactionEntryModelList = paymentTransactionModel
								.getEntries();
						for (final PaymentTransactionEntryModel entry : paymentTransactionEntryModelList)
						{
							//partialCapturePayment
							if (tCode.equals(entry.getCode()) && entry.getType().equals(PaymentTransactionType.CAPTURE))
							{
								paymentTrans = true;
								break;
							}
						}

					}
				}
			}

			validCapturePayment = cardTypeOrder && !paymentTrans && consignment.getStatus().equals(ConsignmentStatus.SHIPPED);
		}


		return validCapturePayment;
	}

	private PaymentTransactionEntryModel fetchAuthTransactionEntry(final ConsignmentModel consignment)
	{
		if (consignment != null)
		{
			final String tCode = consignment.getOrder().getCode() + "-" + consignment.getCode();
			final List<PaymentTransactionModel> paymentTransactionList = consignment.getOrder().getPaymentTransactions();
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
							if (tCode.equals(entry.getCode()) && entry.getType().equals(PaymentTransactionType.AUTHORIZATION)
									&& TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
							{
								return entry;

							}
						}

					}
				}
			}
		}

		return null;
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
	 * @return the ptAPIPaymentFacade
	 */
	public PTAPIPaymentFacade getPtAPIPaymentFacade()
	{
		return ptAPIPaymentFacade;
	}

	/**
	 * @param ptAPIPaymentFacade
	 *           the ptAPIPaymentFacade to set
	 */
	@Required
	public void setPtAPIPaymentFacade(final PTAPIPaymentFacade ptAPIPaymentFacade)
	{
		this.ptAPIPaymentFacade = ptAPIPaymentFacade;
	}


}