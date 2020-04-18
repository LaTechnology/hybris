package com.greenlee.storefront.interceptors.beforecontroller;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import com.greenlee.core.enums.UserTypes;
import com.greenlee.core.greenleeb2bcustomer.services.GreenleeB2BCustomerService;
import com.greenlee.core.maxmind.services.MaxmindService;
import com.greenlee.core.model.GreenleeB2BCustomerModel;
import com.greenlee.storefront.interceptors.BeforeControllerHandler;


/**
 * @author xiaochen bian
 *
 */
public class ForceCurrencyForSessionBeforeControllerHandler implements BeforeControllerHandler
{

	private static final Logger LOG = Logger.getLogger(ForceCurrencyForSessionBeforeControllerHandler.class);

	public static final String B2E_UID = "0010000014";
	public static final String B2C_UID = "0010000012";

	public static final String DUMMY_UNIT_B2B = (null != Config.getParameter("greenlee.account.dummy.distributor.uid")) ? Config
			.getParameter("greenlee.account.dummy.distributor.uid") : "dummydistributor";
	public static final String DUMMY_UNIT_B2E = (null != Config.getParameter("greenlee.account.dummy.distributor.b2e.uid")) ? Config
			.getParameter("greenlee.account.dummy.distributor.b2e.uid") : B2E_UID;
	public static final String DUMMY_UNIT_B2C = (null != Config.getParameter("greenlee.account.dummy.distributor.b2c.uid")) ? Config
			.getParameter("greenlee.account.dummy.distributor.b2c.uid") : B2C_UID;


	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "maxmindService")
	private MaxmindService maxmindService;

	@Resource(name = "currencyDao")
	private CurrencyDao currencyDao;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "greenleeB2BCustomerService")
	private GreenleeB2BCustomerService greenleeB2BCustomerService;

	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response,
			final HandlerMethod handler)
	{
		final UserModel currentUser = userService.getCurrentUser();
		if (userFacade.isAnonymousUser())
		{
			final HttpSession session = request.getSession();
			if (session.getAttribute("sessionCount") == null)
			{
				session.setAttribute("sessionCount", 1);
				if (isGetMethod(request))
				{
					final String ip = maxmindService.getIpFromRequest(request);
					final String countryIsoCode = maxmindService.getCountryForIP(ip);
					final String currencyIsoCode = getCurrencyFromCountryIsoCode(countryIsoCode);

					setAnonymousCurrency(currencyIsoCode);
				}
			}
		}

		else if (currentUser != null && currentUser.getUid() != null)
		{
			try
			{
				final List<GreenleeB2BCustomerModel> customerList = greenleeB2BCustomerService
						.getGreenleeB2BCustomersById(currentUser.getUid());
				if (customerList.isEmpty())
				{
					return true;
				}
				final GreenleeB2BCustomerModel greenleeB2BCustomer = greenleeB2BCustomerService.getGreenleeB2BCustomersById(
						currentUser.getUid()).get(0);
				final B2BUnitModel b2bUnit = greenleeB2BCustomer.getDefaultB2BUnit();
				if (b2bUnit != null && !isBeforeRealtimeCustomerCreation(greenleeB2BCustomer))
				{
					CurrencyModel currencyModel = b2bUnit.getCurrencyPreference();
					LOG.info("Step 1 >> " + b2bUnit.getCurrencyPreference().getIsocode() + " : Unit " + b2bUnit.getUid());
					if (null == currencyModel)
					{
						currencyModel = greenleeB2BCustomer.getSessionCurrency();
					}
					LOG.info("Step 2 >> " + b2bUnit.getCurrencyPreference().getIsocode() + " : Unit " + b2bUnit.getUid());
					commonI18NService.setCurrentCurrency(currencyModel);
				}
				// GRE-1970 - In case of users before real time customer creation get the ip based on maxmind service.
				// Keep firing maxmind for every request until real time customer is created
				else if (isBeforeRealtimeCustomerCreation(greenleeB2BCustomer))
				{
					LOG.info("Step 3 >> User belongs to base reference account, so currency is either from maxmind or from session");
					final HttpSession session = request.getSession();
					if (session.getAttribute("sessionCountRefAcct") == null)
					{
						session.setAttribute("sessionCountRefAcct", 1);
						if (isGetMethod(request))
						{
							final String ip = maxmindService.getIpFromRequest(request);
							final String countryIsoCode = maxmindService.getCountryForIP(ip);
							final String currencyIsoCode = getCurrencyFromCountryIsoCode(countryIsoCode);
							LOG.info("currency is from maxmind:" + currencyIsoCode);
							setAnonymousCurrency(currencyIsoCode);
						}
					}
				}
				else
				{
					LOG.info("Step 4 >> User belongs to base reference account, so currency is either from maxmind or from session");
					CurrencyModel currencyModel = userService.getCurrentUser().getSessionCurrency();
					if (currencyModel == null)
					{
						currencyModel = currencyDao.findCurrenciesByCode("USD").get(0);
					}
					commonI18NService.setCurrentCurrency(currencyModel);
				}
				userFacade.syncSessionCurrency();
			}
			catch (final IllegalArgumentException ile)
			{
				LOG.warn("Can not set session currency " + ile.getMessage());
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Exception setting the currency", ile);
				}
			}
		}

		return true;
	}

	protected boolean isGetMethod(final HttpServletRequest request)
	{
		return RequestMethod.GET.name().equalsIgnoreCase(request.getMethod());
	}

	protected void setAnonymousCurrency(final String isoCode)
	{
		if (StringUtils.isNotBlank(isoCode))
		{
			try
			{
				final CurrencyModel currencyModel = currencyDao.findCurrenciesByCode(isoCode).get(0);

				commonI18NService.setCurrentCurrency(currencyModel);
				userFacade.syncSessionCurrency();
			}
			catch (final IllegalArgumentException ile)
			{
				LOG.warn("Can not set session currency to [" + isoCode + "]. " + ile.getMessage());
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Exception setting the currency", ile);
				}
			}
		}
		else if (isoCode == null)
		{
			try
			{
				final CurrencyModel currencyModel = currencyDao.findCurrenciesByCode(getCurrencyFromCountryIsoCode("US")).get(0);

				commonI18NService.setCurrentCurrency(currencyModel);
				userFacade.syncSessionCurrency();
			}
			catch (final IllegalArgumentException ile)
			{
				LOG.warn("Can not set session currency to [" + isoCode + "]. " + ile.getMessage());
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Exception setting the currency", ile);
				}
			}
		}
	}

	protected String getCurrencyFromCountryIsoCode(final String isoCode)
	{
		if (("CA").equals(isoCode))
		{
			return "CAD";
		}
		else
		{
			return "USD";
		}
	}

	protected boolean isBeforeRealtimeCustomerCreation(final GreenleeB2BCustomerModel userModel)
	{
		boolean hasDummyB2BUnit = false;
		final Set<B2BUnitModel> b2bUnits = new HashSet();
		final Set<PrincipalGroupModel> userGroup = userModel.getGroups();
		final Set<PrincipalGroupModel> newUserGroup = new HashSet<>();

		for (final PrincipalGroupModel userGroupModel : userGroup)
		{
			if (userGroupModel instanceof B2BUnitModel)
			{
				final B2BUnitModel b2bunit = (B2BUnitModel) userGroupModel;
				b2bUnits.add(b2bunit);
			}
			newUserGroup.add(userGroupModel);
		}
		for (final B2BUnitModel dummyB2BUnit : b2bUnits)
		{
			if (UserTypes.B2C.equals(dummyB2BUnit.getUserType()) && dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2C))
			{
				hasDummyB2BUnit = true;
			}
			if (UserTypes.B2E.equals(dummyB2BUnit.getUserType()) && dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2E))
			{
				hasDummyB2BUnit = true;
			}
			if (UserTypes.B2B.equals(dummyB2BUnit.getUserType()) && dummyB2BUnit.getUid().equalsIgnoreCase(DUMMY_UNIT_B2B))
			{
				hasDummyB2BUnit = true;
			}
		}
		return hasDummyB2BUnit;
	}

}
