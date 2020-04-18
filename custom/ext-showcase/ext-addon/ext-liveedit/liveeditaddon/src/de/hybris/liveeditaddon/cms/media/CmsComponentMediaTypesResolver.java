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
package de.hybris.liveeditaddon.cms.media;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;


/**
 * 
 */
public interface CmsComponentMediaTypesResolver
{
	boolean isContentSlotPositionSupportingMedia(final String position, final AbstractPageModel page);

	boolean isMediaComponent(final String typeCode);

	String getMediaComponentAttribute(final String typeCode);

	String getMediaComponentTypesAsStringList();

}
