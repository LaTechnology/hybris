/**
 *
 */
package com.greenlee.core.user.dao;

import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.daos.TitleDao;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeTitleDao implements TitleDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.user.daos.TitleDao#findTitleByCode(java.lang.String)
	 */
	@Override
	public TitleModel findTitleByCode(final String code)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {c:").append("pk").append("} ");
		builder.append("FROM {").append(TitleModel._TYPECODE).append(" AS c} WHERE {c:code}=?code");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		final Map<String, String> params = Collections.singletonMap("code", code);
		query.addQueryParameters(params);
		final SearchResult<TitleModel> result = flexibleSearchService.search(query);
		final List<TitleModel> results = result.getResult();
		if (results.size() > 1)
		{
			throw new AmbiguousIdentifierException(
					"Found " + results.size() + " objects from type " + "Title" + " with " + params.toString() + "'");
		}
		return results.isEmpty() ? null : (TitleModel) results.get(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.user.daos.TitleDao#findTitles()
	 */
	@Override
	public Collection<TitleModel> findTitles()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {c:").append("pk").append("} ");
		builder.append("FROM {").append(TitleModel._TYPECODE).append(" AS c} order by {c:code}");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		final SearchResult<TitleModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

}
