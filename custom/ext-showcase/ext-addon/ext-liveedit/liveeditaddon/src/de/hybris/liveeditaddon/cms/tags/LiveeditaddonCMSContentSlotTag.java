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
package de.hybris.liveeditaddon.cms.tags;

import de.hybris.liveeditaddon.cms.media.CmsComponentMediaTypesResolver;
import de.hybris.platform.acceleratorcms.tags2.CMSContentSlotTag;
import de.hybris.platform.acceleratorservices.util.SpringHelper;
import de.hybris.platform.cmscockpit.enums.LiveEditVariant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;


/**
 * 
 */
public class LiveeditaddonCMSContentSlotTag extends CMSContentSlotTag
{
	private static final Logger LOG = Logger.getLogger(LiveeditaddonCMSContentSlotTag.class);

	protected CmsComponentMediaTypesResolver cmsComponentMediaTypesResolver;
	protected MessageSource messageSource;
    private final static boolean DEFAULT_LOCKED_STATE = false;


    @Override
	public int doStartTag() throws JspException
	{
		loadServices();
		prepare();
		if (hasSlot())
		{
			if (!isActiveSlot())
			{
				return SKIP_BODY;
			}

			if (hasItem())
			{
				beforeAllItems();
				beforeItem();
				return EVAL_BODY_INCLUDE;
			}

			noItems();
		}
		else
		{
			noSlot();
		}

		return SKIP_BODY;
	}

	@Override
	protected void noSlot()
	{
		if (currentCmsPageRequestContextData.isLiveEdit())
		{
			noItems();
		}
	}

	@Override
	protected void noItems()
	{
		writeOpenElement();
		afterAllItems();

		resetState();
		resetAttributes();
	}

	@Override
	protected Map<String, String> createLiveEditAttributes()
	{
		final Map<String, String> attributes = super.createLiveEditAttributes();
		if (currentCmsPageRequestContextData.isLiveEdit())
		{
			if (!StringUtils.isEmpty(currentContentSlotPosition) && !attributes.containsKey("data-cms-content-slot-position"))
            {
				attributes.put("data-cms-content-slot-position", currentContentSlotPosition);
				attributes.put("data-cms-content-slot-empty", String.valueOf(true));
			}
			if (!attributes.containsKey("data-cms-content-slot-from-master"))
			{
				attributes.put("data-cms-content-slot-from-master", String.valueOf(false));
			}

            Boolean isLocked = currentContentSlot!=null?currentContentSlot.getLocked():DEFAULT_LOCKED_STATE;
            attributes.put("data-cms-content-slot-locked", isLocked.toString());


            attributes.put("data-cms-content-slot-is-staged-catalog", String.valueOf(!currentCmsPageRequestContextData.getPage().getCatalogVersion().getActive()));

		}

		return attributes;
	}

	@Override
	protected void afterAllItems()
	{
		writeImageUploadArea();
		super.afterAllItems();
	}

	protected void writeImageUploadArea()
	{
		if (currentCmsPageRequestContextData.isLiveEdit() && currentCmsPageRequestContextData.getPreviewData() != null
				&& currentCmsPageRequestContextData.getPreviewData().getLiveEditVariant() != null
				&& currentCmsPageRequestContextData.getPreviewData().getLiveEditVariant().equals(LiveEditVariant.QUICKEDIT))
		{
			final String position = StringUtils.isNotBlank(currentContentSlotPosition) ? currentContentSlotPosition
					: null;


			if (StringUtils.isNotBlank(position)
					//&& !currentContentSlotFromMaster
					&& cmsComponentMediaTypesResolver.isContentSlotPositionSupportingMedia(position,
							currentCmsPageRequestContextData.getPage()))
			{
				htmlElementHelper.writeOpenElement(pageContext, "div", Collections.singletonMap("class", "filedrop"));
				htmlElementHelper.writeOpenElement(pageContext, "span", Collections.singletonMap("class", "message"));
				try
				{
					pageContext.getOut().write(
							messageSource.getMessage("liveeditaddon.imageupload.message", null, "Drop images here to upload",
									LocaleContextHolder.getLocale()));
				}
				catch (final IOException e)
				{
					LOG.warn("Could not write image uplod message : " + e.getMessage());
				}
				htmlElementHelper.writeEndElement(pageContext, "span");
				htmlElementHelper.writeEndElement(pageContext, "div");
			}
		}
	}

	@Override
	protected void loadServices()
	{
		super.loadServices();
		this.cmsComponentMediaTypesResolver = lookupCmsComponentMediaTypeResolver();
		this.messageSource = lookupMessageSource();

	}

	protected boolean isActiveSlot()
	{
		return currentContentSlot.getActive();
	}

	protected CmsComponentMediaTypesResolver lookupCmsComponentMediaTypeResolver()
	{
		return SpringHelper.getSpringBean(pageContext.getRequest(), "cmsComponentMediaTypesResolver",
                CmsComponentMediaTypesResolver.class, true);
	}

	protected MessageSource lookupMessageSource()
	{
		return SpringHelper.getSpringBean(pageContext.getRequest(), "storefrontMessageSource", MessageSource.class, true);
	}
}
