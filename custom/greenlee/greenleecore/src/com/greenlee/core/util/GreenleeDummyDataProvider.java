/**
 *
 */
package com.greenlee.core.util;

import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.StandardDateRange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.time.DateUtils;


/**
 * @author peter.asirvatham
 * 
 */
public class GreenleeDummyDataProvider
{
	private static final String BUDGET_VALID_YEATS = "budget.valid.years";
	private static final String BUDGET_MAX_LIMIT = "budget.max.limit";
	private static final String BUDGET_CURRENCY_ISO = "budget.currency.iso";
	private static final String CA = "CA";
	private static final String USD = "USD";
	private static final String CAD = "CAD";
	private I18NService i18NService;

	/**
	 * @param i18nService
	 *           the i18NService to set
	 */
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}

	public void createDummyBudgetAndCost(final B2BUnitModel b2bUnitModel)
	{
		int counter = 1;
		Registry.activateMasterTenant();
		Config.getParameter(BUDGET_VALID_YEATS);
		Config.getParameter(BUDGET_MAX_LIMIT);
		final String configuredCurrencyValue = Config.getParameter(BUDGET_CURRENCY_ISO);
		final StringTokenizer currenciesList = new StringTokenizer(configuredCurrencyValue, ",");
		final List<B2BBudgetModel> budgetModelList = new ArrayList<B2BBudgetModel>();
		while (currenciesList.hasMoreElements())
		{
			final String currencyIsoCode = currenciesList.nextToken();
			final CurrencyModel currencyModel = i18NService.getCurrency(currencyIsoCode);
			final B2BBudgetModel budgetModel = new B2BBudgetModel();
			budgetModel.setCode(b2bUnitModel.getUid() + "-Monthly 50K " + currencyIsoCode + counter);
			budgetModel.setUnit(b2bUnitModel);
			budgetModel.setBudget(new BigDecimal(10000));
			budgetModel.setCurrency(currencyModel);
			final StandardDateRange datePeriodValid = new StandardDateRange(new Date(), DateUtils.addYears(new Date(), 5));
			budgetModel.setDateRange(datePeriodValid);
			budgetModel.setName(b2bUnitModel.getUid() + "-Monthly 50K " + currencyIsoCode + counter, Locale.ENGLISH);
			final B2BCostCenterModel costCenter = getCostCenter(b2bUnitModel, counter);
			final Set<B2BBudgetModel> budget = new HashSet<B2BBudgetModel>(budgetModelList);
			final Set<B2BCostCenterModel> cost = new HashSet<B2BCostCenterModel>();
			cost.add(costCenter);
			budgetModel.setCostCenters(cost);
			costCenter.setBudgets(budget);
			budgetModelList.add(budgetModel);
			counter++;
		}
		b2bUnitModel.setBudgets(budgetModelList);
	}

	protected CurrencyModel getCurrencyForCost(final B2BUnitModel b2bUnitModel)
	{
		CurrencyModel costCurrency = new CurrencyModel();
		if (b2bUnitModel.getCountry() != null && b2bUnitModel.getCountry().getIsocode().equalsIgnoreCase(CA))
		{
			costCurrency = i18NService.getCurrency(CAD);
		}
		else
		{
			costCurrency = i18NService.getCurrency(USD);
		}
		return costCurrency;
	}

	protected B2BCostCenterModel getCostCenter(final B2BUnitModel b2bUnitModel, final int counter)
	{
		final CurrencyModel costcurrency = getCurrencyForCost(b2bUnitModel);
		final B2BCostCenterModel costCenter = new B2BCostCenterModel();
		costCenter.setCode(b2bUnitModel.getUid() + "-CostCenter" + counter);
		costCenter.setName(b2bUnitModel.getUid() + "-CostCenter" + counter, Locale.ENGLISH);
		costCenter.setCurrency(costcurrency);
		costCenter.setUnit(b2bUnitModel);
		return costCenter;
	}
}
