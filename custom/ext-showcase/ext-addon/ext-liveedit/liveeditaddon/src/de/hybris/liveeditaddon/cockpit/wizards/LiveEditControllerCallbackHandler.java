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

import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;


/**
 */
public interface LiveEditControllerCallbackHandler
{

	void onDone(final Wizard wizard, final WizardPage page);

	void onCancel(final Wizard wizard, final WizardPage page);

}
