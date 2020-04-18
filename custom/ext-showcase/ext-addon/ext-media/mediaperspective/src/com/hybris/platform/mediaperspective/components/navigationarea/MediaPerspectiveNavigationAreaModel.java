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
package com.hybris.platform.mediaperspective.components.navigationarea;

import de.hybris.platform.cockpit.components.navigationarea.DefaultNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;


/**
 */
public class MediaPerspectiveNavigationAreaModel extends DefaultNavigationAreaModel
{
	public MediaPerspectiveNavigationAreaModel()
	{
		super();
	}

	public MediaPerspectiveNavigationAreaModel(final AbstractUINavigationArea area)
	{
		super(area);
	}

	@Override
	public void update()
	{
		for (final Section s : getSections())
		{
			if (s instanceof DefaultSectionSelectorSection)
			{
				((DefaultSectionSelectorSection) s).updateItems();
			}
			sectionUpdated(s);
		}
		super.update();
	}

}
