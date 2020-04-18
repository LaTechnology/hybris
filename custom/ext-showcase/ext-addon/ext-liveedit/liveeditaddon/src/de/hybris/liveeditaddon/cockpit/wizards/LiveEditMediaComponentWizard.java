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

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.page.*;
import de.hybris.platform.cockpit.services.config.WizardConfiguration;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.liveeditaddon.cms.media.CmsComponentMediaTypesResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 */
public class LiveEditMediaComponentWizard extends LiveEditCmsComponentWizard
{
	private MediaModel mediaModel;

	public LiveEditMediaComponentWizard(final BrowserSectionModel model, final Component parent, final BrowserModel browserModel,
			final LiveEditView liveEditView)
	{
		super(model, parent, browserModel, liveEditView);
	}

	@Override
	protected CmsWizard initWizard()
	{
		final DragDropAwareCmsWizard wizard = new DragDropAwareCmsWizard(this.browserModel, this.model);
		if (mediaModel != null)
		{
			wizard.setMediaModel(mediaModel);
		}

		wizard.setTitle(WIZARD_TITLE);
		wizard.setDefaultController(getDefaultPageController());
		wizard.setComponentURI(DEFAULT_WIZARD_FRAME);

		return wizard;
	}

	@Override
	protected void addComponentTypeFilters(final CmsComponentSelectorPage typeSelectorPage)
	{
		if (typeSelectorPage instanceof TypeSelectorPage)
		{
			final TypeSelectorPage pageSelector = (TypeSelectorPage) typeSelectorPage;
			Map<String, Object> pageParameters = pageSelector.getPageParameters();
			if (pageParameters == null)
			{
				pageParameters = new HashMap<String, Object>();
			}
			final CmsComponentMediaTypesResolver cmsComponentMediaTypesResolver = (CmsComponentMediaTypesResolver) SpringUtil
					.getBean("cmsComponentMediaTypesResolver");
			pageParameters.put(RESTRICTED_TYPES, cmsComponentMediaTypesResolver.getMediaComponentTypesAsStringList());
			pageSelector.setPageParameters(pageParameters);
		}
	}

	@Override
	protected void initWizardPages(final CmsWizard wizard, final DecisionPage decisionPage,
			final CmsComponentSelectorPage typeSelectorPage, final AdvancedSearchPage advancedSearchPage,
			final MandatoryPage mandatoryPage)
	{
		final WizardConfiguration config = getWizardConfiguration();
		final List<WizardPage> pages = new ArrayList<WizardPage>();
		pages.add(typeSelectorPage);

		if (config != null)
		{
			wizard.setShowPrefilledValues(config.isShowPrefilledValues());
			mandatoryPage.setDisplayedAttributes(new ArrayList<String>(config.getQualifiers(true).keySet()));
			typeSelectorPage.setDisplaySubtypes(config.isDisplaySubtypes() && isDisplaySubtypes());

			if (config.isCreateMode() && config.isSelectMode() && !isMediaUploadMode())
			{
				pages.add(decisionPage);
			}
			if (config.isSelectMode() && !isMediaUploadMode())
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

	private boolean isMediaUploadMode()
	{
		return mediaModel != null;
	}

	/**
	 * @return the mediaModel
	 */
	public MediaModel getMediaModel()
	{
		return mediaModel;
	}

	/**
	 * @param mediaModel
	 *           the mediaModel to set
	 */
	public void setMediaModel(final MediaModel mediaModel)
	{
		this.mediaModel = mediaModel;
	}
}
