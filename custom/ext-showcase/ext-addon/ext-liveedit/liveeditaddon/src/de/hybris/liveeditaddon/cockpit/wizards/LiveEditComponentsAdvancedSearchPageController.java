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
import de.hybris.platform.cmscockpit.wizard.controller.ComponentsAdvancedSearchPageController;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;


/**
 * 
 */
public class LiveEditComponentsAdvancedSearchPageController extends ComponentsAdvancedSearchPageController
{
	private LiveEditView liveEditView;

	public LiveEditComponentsAdvancedSearchPageController(final String contentSlotPosition, final LiveEditView liveEditView)
	{
		super(contentSlotPosition);
		this.liveEditView = liveEditView;
	}

	public LiveEditComponentsAdvancedSearchPageController(final String contentSlotPosition)
	{
		super(contentSlotPosition);
	}

	/**
	 * @param liveEditView
	 *           the liveEditView to set
	 */
	public void setLiveEditView(final LiveEditView liveEditView)
	{
		this.liveEditView = liveEditView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cmscockpit.wizard.controller.ComponentsAdvancedSearchPageController#done(de.hybris.platform
	 * .cockpit.wizards.Wizard, de.hybris.platform.cockpit.wizards.WizardPage)
	 */
	@Override
	public void done(final Wizard wizard, final WizardPage page)
	{
		super.done(wizard, page);
		if (liveEditView != null)
		{
			liveEditView.update();
		}

	}
}
