/**
 *
 */
package com.greenlee.facades.search.populators;

import de.hybris.greenlee.facades.content.data.ContentData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;


/**
 * @author aruna
 *
 */

public class SearchResultContentPopulator implements Populator<SearchResultValueData, ContentData>
{

	@Override
	public void populate(final SearchResultValueData source, final ContentData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		// Pull the values directly from the SearchResult object
		target.setTitle(this.<String> getValue(source, "title"));

		final StringBuilder sb = new StringBuilder();
		final List<String> contentList = this.<List> getValue(source, "contentText");


		if (contentList != null)
		{
			for (final String contentStr : contentList)
			{
				sb.append(contentStr);
			}
			target.setText(sb.toString());

		}
		else
		{

			target.setText("");
		}

		populateUrl(source, target);

	}



	protected void populateUrl(final SearchResultValueData source, final ContentData target)
	{
		final String url = this.<String> getValue(source, "url");
		if (StringUtils.isEmpty(url))
		{
			// Resolve the URL and set it on the content data

			target.setUrl(this.<String> getValue(source, "url"));
		}
		else
		{
			target.setUrl(url);
		}
	}

	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}

		// DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
		return (T) source.getValues().get(propertyName);
	}

}
