///*
// * [y] hybris Platform
// *
// * Copyright (c) 2000-2013 hybris AG
// * All rights reserved.
// *
// * This software is the confidential and proprietary information of hybris
// * ("Confidential Information"). You shall not disclose such Confidential
// * Information and shall use it only in accordance with the terms of the
// * license agreement you entered into with hybris.
// *
// *
// */
//package com.hybris.addon.cockpits.components.liveedit.converter;
//
//import de.hybris.platform.cms2.misc.CMSFilter;
//import de.hybris.platform.cmscockpit.events.impl.CmsUrlChangeEvent;
//import de.hybris.platform.cockpit.session.UISessionUtils;
//import de.hybris.platform.servicelayer.dto.converter.ConversionException;
//import de.hybris.platform.servicelayer.dto.converter.Converter;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.hybris.addon.cockpits.cms.events.impl.AddOnCmsUrlChangeEvent;
//import com.hybris.addon.cockpits.session.liveedit.impl.AddOnFrontendAttributes;
//
//
///**
// * @author rmcotton
// *
// */
//public class DefaultCmsUrlChangeEventConverter implements Converter<String[], CmsUrlChangeEvent>
//{
//
//	private Converter<String[], AddOnFrontendAttributes> frontEndAttributesConverter;
//
//	/**
//	 * Extracts request path from long URL (request path with query search)
//	 *
//	 * @param longUrl
//	 *           request path with query search
//	 * @return extracted request path
//	 */
//	protected String extractRequestPath(final String longUrl)
//	{
//		String ret = StringUtils.EMPTY;
//		if (!longUrl.contains(CMSFilter.PREVIEW_TOKEN))
//		{
//			final String[] urlParts = longUrl.split("[\\?&]" + CMSFilter.PREVIEW_TICKET_ID_PARAM);
//			ret = urlParts[0];
//		}
//		return ret;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see de.hybris.platform.servicelayer.dto.converter.Converter#convert(java.lang.Object)
//	 */
//	@Override
//	public CmsUrlChangeEvent convert(final String[] attributes) throws ConversionException
//	{
//		final AddOnCmsUrlChangeEvent event = new AddOnCmsUrlChangeEvent(UISessionUtils.getCurrentSession().getCurrentPerspective(),
//				extractRequestPath(attributes[1]), attributes[2], attributes[3], attributes[4]);
//		return convert(attributes, event);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see de.hybris.platform.servicelayer.dto.converter.Converter#convert(java.lang.Object, java.lang.Object)
//	 */
//	@Override
//	public CmsUrlChangeEvent convert(final String[] attributes, final CmsUrlChangeEvent prototype) throws ConversionException
//	{
//		if (prototype instanceof AddOnCmsUrlChangeEvent && getFrontEndAttributesConverter() != null)
//		{
//			((AddOnCmsUrlChangeEvent) prototype).setAddOnFrontendAttributes(getFrontEndAttributesConverter().convert(attributes));
//		}
//		return prototype;
//	}
//
//	public Converter<String[], AddOnFrontendAttributes> getFrontEndAttributesConverter()
//	{
//		return frontEndAttributesConverter;
//	}
//
//	public void setFrontEndAttributesConverter(final Converter<String[], AddOnFrontendAttributes> frontEndAttributesConverter)
//	{
//		this.frontEndAttributesConverter = frontEndAttributesConverter;
//	}
//
//
//
//}
