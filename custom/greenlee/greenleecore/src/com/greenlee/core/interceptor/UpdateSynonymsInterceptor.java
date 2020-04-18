/**
 *
 */
package com.greenlee.core.interceptor;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrSynonymConfigModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * @author peter.asirvatham
 * 
 */
public class UpdateSynonymsInterceptor implements PrepareInterceptor<ProductModel>
{
	private static final Logger LOG = Logger.getLogger(UpdateSynonymsInterceptor.class);
	private static final String DEFAULT_SITE = "greenlee";
	private BaseSiteService baseSiteService;
	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.interceptor.PrepareInterceptor#onPrepare(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onPrepare(final ProductModel product, final InterceptorContext context) throws InterceptorException
	{
		final StringBuilder keyword = new StringBuilder();
		final BaseSiteModel site = getBaseSiteService().getBaseSiteForUID(DEFAULT_SITE);
		final SolrFacetSearchConfigModel solrSearch = site.getSolrFacetSearchConfiguration();
		List<SolrSynonymConfigModel> synonymsList = solrSearch.getSynonyms();
		final SolrSynonymConfigModel newSynonym = new SolrSynonymConfigModel();
		final List<KeywordModel> keyWordList = product.getKeywords();
		boolean isSynonmyReplaced = false;
		LanguageModel language = null;
		boolean isFirstKeyWord = true;
		if (keyWordList != null)
		{
			for (final KeywordModel keywordModel : keyWordList)
			{
				if (isFirstKeyWord)
				{
					isFirstKeyWord = false;
				}
				else
				{
					keyword.append(",");
				}
				keyword.append(keywordModel.getKeyword());
				language = keywordModel.getLanguage();

			}
			if (!keyword.toString().isEmpty())
			{
				for (final SolrSynonymConfigModel synonmy : synonymsList)
				{
					if (synonmy.getSynonymTo() != null && synonmy.getSynonymTo().equals(product.getName()))
					{
						isSynonmyReplaced = true;
						synonmy.setSynonymFrom(keyword.toString());
						synonmy.setLanguage(language);
						modelService.save(synonmy);
						LOG.info("Synonyms has been updated with new key words");
						break;
					}
				}
				if (!isSynonmyReplaced)
				{
					synonymsList = new ArrayList<SolrSynonymConfigModel>();
					newSynonym.setSynonymFrom(keyword.toString());
					newSynonym.setSynonymTo(product.getName());
					newSynonym.setLanguage(site.getDefaultLanguage());
					synonymsList.add(newSynonym);
					solrSearch.setSynonyms(synonymsList);
					modelService.save(solrSearch);
					LOG.info("New Synonyms added");
				}
			}
		}
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}


	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
}
