/**
 *
 */
package com.greenlee.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorservices.urlresolver.impl.DefaultContentPageUrlResolver;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author aruna
 *
 */
public class TextValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private static final Logger LOG = Logger.getLogger(TextValueProvider.class);
	@Autowired
	private DefaultContentPageUrlResolver contentPageUrlResolver;

	@Autowired
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Autowired
	private CMSSiteService cmsSiteService;

	private FieldNameProvider fieldNameProvider;


	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}


	@SuppressWarnings("deprecation")
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{


		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (model instanceof ContentPageModel)
		{

			final String url = contentPageUrlResolver.resolve((ContentPageModel) model);

			final String websiteUrlForSite = siteBaseUrlResolutionService
					.getWebsiteUrlForSite(cmsSiteService.getSites().iterator().next(), null, true, url);



			Document doc = null;
			try
			{
				doc = Jsoup.connect(websiteUrlForSite).get();
			}
			catch (final IOException e)
			{
				LOG.info(e);
			}


			if (doc != null)
			{
				final String bodyText = doc.body().text();
				for (final String fieldName : fieldNames)
				{
					fieldValues.add(new FieldValue(fieldName, bodyText));
				}
			}
		}
		else
		{
			throw new FieldValueProviderException("It is not ContentPageModel");
		}
		return fieldValues;
	}

	/**
	 * @return the contentPageUrlResolver
	 */
	public DefaultContentPageUrlResolver getContentPageUrlResolver()
	{
		return contentPageUrlResolver;
	}

	/**
	 * @param contentPageUrlResolver
	 *           the contentPageUrlResolver to set
	 */
	public void setContentPageUrlResolver(final DefaultContentPageUrlResolver contentPageUrlResolver)
	{
		this.contentPageUrlResolver = contentPageUrlResolver;
	}


}