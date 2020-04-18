package com.paymentech.jalo;

import com.paymentech.constants.PaymentechintegrationConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class PaymentechintegrationManager extends GeneratedPaymentechintegrationManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( PaymentechintegrationManager.class.getName() );
	
	public static final PaymentechintegrationManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (PaymentechintegrationManager) em.getExtension(PaymentechintegrationConstants.EXTENSIONNAME);
	}
	
}
