/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.liveeditaddon.cockpit.navigationbargenerator.wizard;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardContext;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.generic.GenericItemMandatoryPage;
import de.hybris.platform.cockpit.wizards.impl.DefaultWizardContext;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.util.CSVReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;

import com.google.common.base.Preconditions;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.NavigationBarGenerator;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.impl.DefaultCatalogDataService;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.impl.ImpexContainer;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.impl.OutputResult;


public class DefaultNavigationGeneratorWizard extends Wizard
{
	private final static Logger LOG = Logger.getLogger(DefaultNavigationGeneratorWizard.class);

	protected static final String GENERATE_NAVIGATION_CATALOG_NAME = "catalogName";
	protected static final String GENERATE_NAVIGATION_CATEGORY_LEVEL = "categoryLevel";
	protected static final String GENERATE_NAVIGATION_ROOT_CATEGORY = "rootCategory";
	protected static final String GENERATE_CATEGORIES_ONLY_WITH_PRODUCTS = "shouldHaveProductFlag";//if true,empty categories will be skipped

	protected static final Integer DEFAULT_CATEGORY_LEVEL = Integer.valueOf(3);
	protected static final Boolean DEFAULT_FLAG_VALUE = Boolean.FALSE;

	public static final String WIZARD_CONTEXT_CATALOG_VERSION = "WIZARD_CONTEXT_CATALOG_VERSION";
	public static final String WIZARD_CONTEXT_PRODUCT_CATALOG_VERSION = "WIZARD_CONTEXT_PRODUCT_CATALOG_VERSION";

	private DefaultWizardContext ctx;
	private UIAccessRightService uiAccessRightService;
	private NavigationBarGenerator navigationBarGenerator;
	private DefaultCatalogDataService defaultCatalogDataService;

	private OutputResult generatedImpex = null;
	private String currentShowing = null;
	private List<ImpexContainer> convertedImpex = null;

	public String runImpexForKey(final ImpexContainer impexContainer)
	{
		Preconditions.checkNotNull(impexContainer);

		String result = null;

		final CSVReader csvReader = new CSVReader(impexContainer.getImpex());
		final Importer importer = new Importer(csvReader);
		try
		{
			importer.importAll();
			LOG.info("Import ended - processed: " + importer.getProcessedItemsCountOverall() + " [" + impexContainer.getLabel()
					+ "]");
		}
		catch (final ImpExException e)
		{
			result = "Import failed - check log for details";
		}
		return result;
	}

	public List<ImpexContainer> getConvertedImpex()
	{
		return convertedImpex;
	}

	public void setConvertedImpex(final List<ImpexContainer> convertedImpex)
	{
		this.convertedImpex = convertedImpex;
	}

	public String getGeneratedImpex()
	{
		if (null == currentShowing)
		{
			currentShowing = this.generatedImpex.getImpexScript(OutputResult.FULL_OUTPUT);
		}
		return currentShowing;
	}

	public void setGeneratedImpexLang(final String keyLang)
	{
		Preconditions.checkNotNull(keyLang);
		currentShowing = this.generatedImpex.getImpexScript(keyLang);
	}

	public void setGeneratedImpex(final OutputResult generatedImpex)
	{
		setConvertedImpex(ImpexContainer.from(generatedImpex));
		this.generatedImpex = generatedImpex;
	}

	public Boolean getShouldGenerateCategoriesOnlyWithProducts()
	{
		Boolean flag = (Boolean) ctx.getAttribute(GENERATE_CATEGORIES_ONLY_WITH_PRODUCTS);
		if (null == flag)
		{
			flag = DEFAULT_FLAG_VALUE;
			getWizardContext().setAttribute(GENERATE_CATEGORIES_ONLY_WITH_PRODUCTS, flag);
		}
		return flag;
	}

	public String getCmsCatalogName()
	{
		String catalogName = "n\\a";
		if (getCurrentContentCatalogVersion() != null)
		{
			catalogName = getCurrentContentCatalogVersion().getCatalog().getName();
		}
		getWizardContext().setAttribute(GENERATE_NAVIGATION_CATALOG_NAME, catalogName);
		return catalogName;
	}

	public String getCmsCatalogVersionName()
	{
		String catalogVersionName = "n\\a";
		if (getCurrentContentCatalogVersion() != null)
		{
			catalogVersionName = getCurrentContentCatalogVersion().getVersion();

		}
		getWizardContext().setAttribute(GENERATE_NAVIGATION_CATALOG_NAME, catalogVersionName);
		return catalogVersionName;
	}

	public String getLanguages()
	{
		final CatalogModel productCatalogVersions = getDefaultCatalogDataService().getProductCatalogVersions();
		final Set<String> localizations = getNavigationBarGenerator().generateInnerJoinOfLanguages(
				getCurrentContentCatalogVersion().getLanguages(), productCatalogVersions.getLanguages());

		final StringBuilder result = new StringBuilder();
		for (final String localization : localizations)
		{
			result.append(localization).append(" ");
		}
		return result.toString();
	}

	public Integer getCmsCategoryLevel()
	{
		getWizardContext().setAttribute(GENERATE_NAVIGATION_CATEGORY_LEVEL, DEFAULT_CATEGORY_LEVEL);
		return DEFAULT_CATEGORY_LEVEL;
	}

	public void setCmsCategoryLevel(final String value)
	{
		getWizardContext().setAttribute(GENERATE_NAVIGATION_CATEGORY_LEVEL, Integer.valueOf(value));
	}

	public Object getCmsRootCategory()
	{
		return getWizardContext().getAttribute(GENERATE_NAVIGATION_ROOT_CATEGORY);
	}

	public Object getCmsProductCatalogName()
	{
		return getDefaultCatalogDataService().getProductCatalogVersions().getName();
	}

	@Override
	public DefaultWizardContext getWizardContext()
	{
		return ctx;
	}

	@Override
	public void setWizardContext(final WizardContext context)
	{
		if (context == null || context instanceof DefaultWizardContext)
		{
			ctx = (DefaultWizardContext) context;
		}
		else
		{
			LOG.error("Could not set wizard context, must be an instance of DefaultWizardContext");
		}
	}

	@Override
	public void doNext()
	{
		try
		{
			final CatalogVersionModel catalogVersion = getCurrentContentCatalogVersion();
			final Integer catLevel = (Integer) getWizardContext().getAttribute(GENERATE_NAVIGATION_CATEGORY_LEVEL);
			final List<TypedObject> rootCategories = (List<TypedObject>) getWizardContext().getAttribute(
					GENERATE_NAVIGATION_ROOT_CATEGORY);
			final Boolean shouldHaveProducts = (Boolean) ctx.getAttribute(GENERATE_CATEGORIES_ONLY_WITH_PRODUCTS);

			final CatalogModel productCatalogVersions = getDefaultCatalogDataService().getProductCatalogVersions();
			final Set<String> localizations = getNavigationBarGenerator().generateInnerJoinOfLanguages(
					catalogVersion.getLanguages(), productCatalogVersions.getLanguages());

			OutputResult impexSQL = null;
			if ((null == rootCategories) || (rootCategories.isEmpty()))
			{
				impexSQL = getNavigationBarGenerator().runGeneratorForCatalog(productCatalogVersions.getId(),
						catalogVersion.getVersion(), catalogVersion.getCatalog().getId(), catalogVersion.getVersion(), catLevel,
						shouldHaveProducts.booleanValue(), localizations);
			}
			else
			{
				final String[] textRootCategories = convertRootCategoriesToString(rootCategories);
				impexSQL = getNavigationBarGenerator().runGeneratorForCatalog(productCatalogVersions.getId(),
						catalogVersion.getVersion(), catalogVersion.getCatalog().getId(), catalogVersion.getVersion(), catLevel,
						textRootCategories, shouldHaveProducts.booleanValue(), localizations);
			}
			setGeneratedImpex(impexSQL);
			super.doNext();
		}
		catch (final WizardConfirmationException e)
		{
			updateView();
		}
	}

	private String[] convertRootCategoriesToString(final List<TypedObject> rootCategories)
	{
		final List<String> result = new ArrayList<String>();
		for (final TypedObject rootCategory : rootCategories)
		{
			final CategoryModel categoryModel = (CategoryModel) rootCategory.getObject();
			result.add(categoryModel.getCode());
		}
		return result.toArray(new String[result.size()]);
	}

	@Override
	public void doDone()
	{
		if (CollectionUtils.isNotEmpty(this.getMessages()))
		{
			this.getMessages().clear();
		}
		if (getCurrentController().validate(this, getCurrentPage()))
		{
			try
			{
				getCurrentController().done(this, getCurrentPage());
				close();
			}
			catch (final WizardConfirmationException exc)
			{
				//everything is OK it will be handled internally by wizard framework!
				if (LOG.isDebugEnabled())
				{
					LOG.debug(exc);
				}
			}
			catch (final RuntimeException e)
			{
				LOG.error("Could not finish wizard, reason: ", e);
				updateView();
			}
		}
		else
		{
			updateView();
		}
	}

	public void init(final GenericItemMandatoryPage page, final Component parent)
	{
		page.setWizard(this);
		parent.appendChild(page.createRepresentationItself());
	}

	@Override
	public void updateView()
	{
		super.updateView(); //To change body of overridden methods use File | Settings | File Templates.
	}

	private CatalogVersionModel getCurrentContentCatalogVersion()
	{
		final CatalogVersionModel catalogVersion = (CatalogVersionModel) getWizardContext().getAttribute(
				WIZARD_CONTEXT_CATALOG_VERSION);
		return catalogVersion;
	}

	private CatalogVersionModel getCurrentProductCatalogVersion()
	{
		final CatalogVersionModel catalogVersion = (CatalogVersionModel) getWizardContext().getAttribute(
				WIZARD_CONTEXT_PRODUCT_CATALOG_VERSION);
		return catalogVersion;
	}

	public Map<Object, Object> getPredefiniedSearchValues()
	{
		final Map<Object, Object> values = new HashMap<Object, Object>();
		final PropertyDescriptor propertyDescriptor = UISessionUtils.getCurrentSession().getTypeService()
				.getPropertyDescriptor("Category.catalogVersion");

		if (getCurrentProductCatalogVersion() != null)
		{
			values.put(propertyDescriptor,
					UISessionUtils.getCurrentSession().getTypeService().wrapItem(getCurrentProductCatalogVersion()));
		}
		return values;
	}

	public UIAccessRightService getUiAccessRightService()
	{
		if (this.uiAccessRightService == null)
		{
			this.uiAccessRightService = (UIAccessRightService) SpringUtil.getBean("uiAccessRightService");
		}
		return uiAccessRightService;
	}

	public void setUiAccessRightService(final UIAccessRightService uiAccessRightService)
	{
		this.uiAccessRightService = uiAccessRightService;
	}

	public void setNavigationBarGenerator(final NavigationBarGenerator navigationBarGenerator)
	{
		this.navigationBarGenerator = navigationBarGenerator;
	}

	public NavigationBarGenerator getNavigationBarGenerator()
	{
		return navigationBarGenerator;
	}

	public void setDefaultCatalogDataService(final DefaultCatalogDataService defaultCatalogDataService)
	{
		this.defaultCatalogDataService = defaultCatalogDataService;
	}

	public DefaultCatalogDataService getDefaultCatalogDataService()
	{
		return defaultCatalogDataService;
	}
}
