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
package de.hybris.liveeditaddon.cockpit.wizards;

import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.constants.CmsImgUrls;
import de.hybris.platform.cmscockpit.services.config.ContentElementConfiguration;
import de.hybris.platform.cmscockpit.wizard.CmsComponentWizard;
import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.controller.CmsDecisionPageController;
import de.hybris.platform.cmscockpit.wizard.page.*;
import de.hybris.platform.cmscockpit.wizard.page.DecisionPage.Decision;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;

import java.util.*;


/**
 * 
 */
public class LiveEditCmsComponentWizard extends CmsComponentWizard
{
	protected static final String RESTRICTED_TYPES = "restrictToCreateTypes";

	protected static final String WIZARD_TITLE = "wizard.component";

	protected static final String DECISION_TITLE = "wizard.page.decision";
	protected static final String DECISION_PAGE_ID = "decisionPage";
	protected static final String DECISION_PAGE_SELECT_LABEL = "wizard.page.decision.select";
	protected static final String DECISION_PAGE_CREATE_LABEL = "wizard.page.decision.create";

	protected static final String SEARCH_PAGE_WIDTH = "600px";
	protected static final String SEARCH_PAGE_HEIGHT = "490px";
	protected static final String SEARCH_PAGE_ID = "advancedSearchPage";

	protected static final String MANDATORY_PAGE_TITLE = "wizard.cmscomponent.title";
	protected static final String MANDATORY_PAGE_ID = "mandatoryPage";

	private Map<String, Object> additionalContextValues;
	private final LiveEditView liveEditView;
	private WizardPageController defaultPageController;
	private String componentTypeFilters;
	private boolean isOnlyCreateDecisionPage = false;

	public LiveEditCmsComponentWizard(final BrowserSectionModel model, final Component parent, final BrowserModel browserModel,
			final LiveEditView liveEditView)
	{
		super(model, parent, browserModel);
		this.liveEditView = liveEditView;
	}

	@Override
	public Wizard start()
	{
		final Map<String, Object> contextValues = initContextValues();

		final CmsWizard wizard = initWizard();
		final DecisionPage decisionPage = initDecisionPage(wizard);
		final CmsComponentSelectorPage typeSelectorPage = initTypeSelectorPage(wizard);
		final AdvancedSearchPage advancedSearchPage = initSearchPage(wizard);
		final MandatoryPage mandatoryPage = initMandatoryPage(wizard);

		initWizardPages(wizard, decisionPage, typeSelectorPage, advancedSearchPage, mandatoryPage);

		wizard.setParent(parent);
		wizard.initialize(null, contextValues);
		wizard.show();

		return wizard;
	}

	protected void initWizardPages(final CmsWizard wizard, final DecisionPage decisionPage,
			final CmsComponentSelectorPage typeSelectorPage, final AdvancedSearchPage advancedSearchPage,
			final MandatoryPage mandatoryPage)
	{
		List<WizardPage> pages = new ArrayList<WizardPage>();
		final WizardConfiguration config = getWizardConfiguration();

		pages.add(typeSelectorPage);

		if (config != null)
		{
			wizard.setShowPrefilledValues(config.isShowPrefilledValues());
			mandatoryPage.setDisplayedAttributes(new ArrayList<String>(config.getQualifiers(true).keySet()));
			typeSelectorPage.setDisplaySubtypes(config.isDisplaySubtypes() && isDisplaySubtypes());

			if (config.isCreateMode() && config.isSelectMode() && decisionPage != null)
			{
				pages.add(decisionPage);
			}
			if (config.isSelectMode() && advancedSearchPage != null)
			{
				pages.add(advancedSearchPage);
			}
			if (config.isCreateMode())
			{
				pages.add(mandatoryPage);
			}
		}

		wizard.setPages(pages);
	}

	protected CmsWizard initWizard()
	{
		final CmsWizard wizard = new CmsWizard(this.browserModel, this.model);
		wizard.setTitle(WIZARD_TITLE);
		wizard.setDefaultController(getDefaultPageController());
		wizard.setComponentURI(DEFAULT_WIZARD_FRAME);
		return wizard;
	}

	protected Map<String, Object> initContextValues()
	{
		final Map<String, Object> contextValues = new HashMap<String, Object>();
		contextValues.put(CMSITEM_CATALOGVERSION, getCmsAdminSiteService().getActiveCatalogVersion());
		contextValues.put(CMSITEM_UID,
				getGenericRandomNameProducer().generateSequence(Cms2Constants.TC.ABSTRACTCMSCOMPONENT, CMSITEM_UID_PREFIX));

		if (getAdditionalContextValues() != null && !getAdditionalContextValues().isEmpty())
		{
			contextValues.putAll(getAdditionalContextValues());
		}
		return contextValues;
	}

	protected DecisionPage initDecisionPage(final CmsWizard wizard)
	{
		final DecisionPage decisionPage = new DecisionPage(DECISION_TITLE, wizard);
		final List<Decision> decisionList = new ArrayList<Decision>();

		if (!isOnlyCreateDecisionPage)
		{
			decisionList.add(decisionPage.new Decision("advancedSearchPage", Labels.getLabel(DECISION_PAGE_SELECT_LABEL),
					CmsImgUrls.CHOOSE_COMPONENT_REFERENCE_DEFAULT_IMAGE)
			{
				@Override
				public String getDecisionIcon()
				{
					String icon = null;
					final ContentElementConfiguration typeConfig = getCurrentTypeElementConfiguration(wizard);
					if (typeConfig != null)
					{
						icon = typeConfig.getRefImage();
					}
					if (StringUtils.isEmpty(icon))
					{
						icon = this.decisionIcon;
					}
					return icon;
				}
			});
		}

		decisionList.add(decisionPage.new Decision("mandatoryPage", Labels.getLabel(DECISION_PAGE_CREATE_LABEL),
				CmsImgUrls.ADD_NEW_COMPONENT_DEFAULT_IMAGE)
		{
			@Override
			public String getDecisionIcon()
			{
				String icon = null;
				final ContentElementConfiguration typeConfig = getCurrentTypeElementConfiguration(wizard);
				if (typeConfig != null)
				{
					icon = typeConfig.getImage();
				}
				if (StringUtils.isEmpty(icon))
				{
					icon = this.decisionIcon;
				}
				return icon;
			}
		});
		decisionPage.setController(new CmsDecisionPageController());
		decisionPage.setDecisions(decisionList);
		decisionPage.setId(DECISION_PAGE_ID);

		return decisionPage;
	}

	protected CmsComponentSelectorPage initTypeSelectorPage(final CmsWizard wizard)
	{
		final CmsComponentSelectorPage typeSelectorPage = getCmsComponentSelectorPageFactory().createCmsComponentSelectorPage();
		typeSelectorPage.setWizard(wizard);
		typeSelectorPage.setRootSelectorType(UISessionUtils.getCurrentSession().getTypeService()
				.getObjectType(Cms2Constants.TC.ABSTRACTCMSCOMPONENT));
		typeSelectorPage.setPosition(this.position);
		addComponentTypeFilters(typeSelectorPage);
		return typeSelectorPage;
	}

	protected AdvancedSearchPage initSearchPage(final CmsWizard wizard)
	{
		final AdvancedSearchPage advancedSearchPage = new AdvancedSearchPage("advancedSearchPage", wizard);
		advancedSearchPage.setWidth(SEARCH_PAGE_WIDTH);
		advancedSearchPage.setHeight(SEARCH_PAGE_HEIGHT);
		advancedSearchPage.setController(new LiveEditComponentsAdvancedSearchPageController(position, liveEditView));
		advancedSearchPage.setId(SEARCH_PAGE_ID);
		return advancedSearchPage;
	}

	protected MandatoryPage initMandatoryPage(final CmsWizard wizard)
	{
		final MandatoryPage mandatoryPage = new MandatoryPage(MANDATORY_PAGE_TITLE, wizard);
		final LiveEditCmsComponentController mandatoryController = new LiveEditCmsComponentController();
		mandatoryController.setPosition(position);
		mandatoryController.setLiveEditView(liveEditView);
		mandatoryController.setBrowserModel(browserModel);
		mandatoryPage.setController(mandatoryController);
		mandatoryPage.setId(MANDATORY_PAGE_ID);
		return mandatoryPage;
	}

	protected void addComponentTypeFilters(final CmsComponentSelectorPage typeSelectorPage)
	{
		if (typeSelectorPage instanceof TypeSelectorPage && getComponentTypeFilters() != null
				&& !getComponentTypeFilters().isEmpty())
		{
			final TypeSelectorPage pageSelector = (TypeSelectorPage) typeSelectorPage;
			Map<String, Object> pageParameters = pageSelector.getPageParameters();
			if (pageParameters == null)
			{
				pageParameters = new HashMap<String, Object>();
			}

			pageParameters.put(RESTRICTED_TYPES, getComponentTypeFilters());
			pageSelector.setPageParameters(pageParameters);
		}
	}

	public ContentElementConfiguration getCurrentTypeElementConfiguration(final CmsWizard wizard)
	{
		ObjectType type = wizard.getCurrentType();
		if (type != null)
		{
			Map<ObjectType, ContentElementConfiguration> contentElements = Collections.EMPTY_MAP;
			if ((type instanceof ObjectTemplate) && ((ObjectTemplate) type).isDefaultTemplate())
			{
				contentElements = getContentElementConfiguration((ObjectTemplate) type).getContentElements();
				type = ((ObjectTemplate) type).getBaseType();
			}

			if (MapUtils.isEmpty(contentElements) || !contentElements.containsKey(type))
			{
				contentElements = getContentElementConfiguration().getContentElements();
			}
			return contentElements.get(type);

		}
		return null;
	}

	public Map<String, Object> getAdditionalContextValues()
	{
		return additionalContextValues;
	}

	public void setAdditionalContextValues(final Map<String, Object> additionalContextValues)
	{
		this.additionalContextValues = additionalContextValues;
	}

	public WizardPageController getDefaultPageController()
	{
		return defaultPageController == null ? new DefaultPageController() : defaultPageController;
	}

	public void setDefaultPageController(final WizardPageController defaultPageController)
	{
		this.defaultPageController = defaultPageController;
	}

	public String getComponentTypeFilters()
	{
		return componentTypeFilters;
	}

	public void setComponentTypeFilters(final String componentTypeFilters)
	{
		this.componentTypeFilters = componentTypeFilters;
	}

	public void setOnlyCreateDecisionPage(final boolean onlyCreateDecisionPage)
	{
		isOnlyCreateDecisionPage = onlyCreateDecisionPage;
	}

	public LiveEditView getLiveEditView()
	{
		return liveEditView;
	}

	public boolean isOnlyCreateDecisionPage()
	{
		return isOnlyCreateDecisionPage;
	}
}
