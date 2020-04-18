/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.platform.mediaperspective.session.impl;

import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;


/**
 */
public class MediaNavigationAreaModel extends AbstractNavigationAreaModel
{


	@Override
	public void initialize()
	{
		// YTODO Auto-generated method stub
	}

	@Override
	public void update()
	{
		for (final Section s : this.getSections())
		{
			if (s instanceof DefaultSectionSelectorSection)
			{
				((DefaultSectionSelectorSection) s).updateItems();
			}
			sectionUpdated(s);
		}

	}


}
