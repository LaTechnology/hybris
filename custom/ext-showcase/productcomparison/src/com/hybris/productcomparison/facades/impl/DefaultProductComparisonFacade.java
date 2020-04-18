/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.productcomparison.facades.impl;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hybris.productcomparison.constants.ProductcomparisonConstants;
import com.hybris.productcomparison.data.WrapperMapVariantAttributes;
import com.hybris.productcomparison.data.WrapperQueueProductComparison;
import com.hybris.productcomparison.facades.ProductComparisonFacade;


/** Default product comparison facade */
public class DefaultProductComparisonFacade implements ProductComparisonFacade
{

	private final int MAX_QUEUE_SIZE = 6;

	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.CLASSIFICATION, ProductOption.STOCK,  ProductOption.VARIANT_FULL, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.SHOWADDTOCART, ProductOption.EXTENDEDATTRIBUTES);

	private final Logger LOG = Logger.getLogger(DefaultProductComparisonFacade.class);

	private ProductFacade productFacade;

	private SessionService sessionService;

	@Override
	public List<ProductData> getProductComparisonList()
	{
		final List recentlyViewedProductCodes = Arrays.asList(retrieveQueueFromSession());
		Collections.reverse(recentlyViewedProductCodes);

		//@TODO .The below code can be refactored
		final List<ProductData> productComparisonList = new ArrayList<ProductData>();

		//Contruct a product list - logic to retrieve a productdata against a product code, and then append to a product list.
		for (final Object productCode : (List) recentlyViewedProductCodes.get(0))
		{
			try
			{
				//String productCode;
				final ProductData prodData = productFacade.getProductForCodeAndOptions((String) productCode, PRODUCT_OPTIONS);
				if (prodData != null)
				{
					//then add to a list of products that need to be visible.
					productComparisonList.add(prodData);
				}
			}
			catch (final Exception e)
			{
				remove((String) productCode);
				LOG.error("Product detail for product article number  " + productCode + " is not available.", e);
			}
		}
		return productComparisonList;
	}

	private void updateProductClassifications(final List<ProductData> productComparisonList,
			final Map<String, Set<String>> classificationIds)
	{
		if (productComparisonList != null)
		{
			for (final ProductData product : productComparisonList)
			{
				final List<ClassificationData> resultList = new ArrayList<ClassificationData>();
				if (product.getClassifications() != null)
				{
					for (final ClassificationData classData : product.getClassifications())
					{
						if (classificationIds.containsKey(classData.getCode()))
						{
							final List<FeatureData> features = new ArrayList<FeatureData>();
							final Set<String> featureNames = classificationIds.get(classData.getCode());
							final Map<String, FeatureData> fMap = new HashMap<String, FeatureData>();
							for (final FeatureData fd : classData.getFeatures())
							{
								if (fd.isComparable())
								{
									fMap.put(fd.getName(), fd);
								}
							}
							for (final String fData : featureNames)
							{
								if (!fMap.containsKey(fData))
								{
									final FeatureData fd = new FeatureData();
									fd.setName(fData);
									fd.setComparable(true);
									fMap.put(fData, fd);
								}
							}
							features.addAll(fMap.values());
							
							if (!fMap.isEmpty()) 
							{
								Collections.sort(features, new ProductComparisonClassificationFeatureComparator());
								classData.setFeatures(features);
								resultList.add(classData);
							}
						}
					}
				}
				//sort result list
				Collections.sort(resultList, new ProductComparisonClassificationComparator());

				product.setClassifications(resultList);
			}
		}
	}

	public Map<String, Map<String, Set<FeatureData>>> aggregateAllClassificationAttributes(final List<ProductData> productComparisonList)
	{
		final Map<String, Map<String, Set<FeatureData>>> result = new HashMap<String, Map<String, Set<FeatureData>>>();
		try{			
				if (!productComparisonList.isEmpty())
				{
					for(ProductData productData: productComparisonList)
					{
						//final ProductData productData = productComparisonList.get(0);
						if (productData.getClassifications() != null)
						{
							final Map<String, Set<FeatureData>> classificationMap = new HashMap<String, Set<FeatureData>>();
							for (final ClassificationData classData : productData.getClassifications())
							{						
								final Set<FeatureData> classFeatureCodes = new LinkedHashSet<FeatureData>();
								//search through class attr
									for (final FeatureData fd : classData.getFeatures())
									{
										//final List<FeatureValueData> featureValues = new ArrayList<FeatureValueData>(fd.getFeatureValues());								
										classFeatureCodes.add(fd);							
									}						
								//add it to the main map if its not in there already.
								if (result !=null && result.get(classData.getName()) == null)
								{
									classificationMap.put(classData.getName(), classFeatureCodes);
								}
							}					
							//Put an entry in product data map now
							result.put(productData.getCode(), classificationMap);					
						}
					}
				}
			}
			catch (final Exception e)
			{
				LOG.error("Unable to aggregate the features and values, something is not right", e);
				e.printStackTrace();
			}
		
		return result;
	}

	@Override
	public List<String> getProductComparisonCodes()
	{
		final List recentlyViewedProductCodes = Arrays.asList(retrieveQueueFromSession());
		return (List) recentlyViewedProductCodes.get(0);
	}

	@Override
	public int size()
	{
		return retrieveQueueFromSession().size();
	}

	@Override
	public boolean isEmpty()
	{
		return retrieveQueueFromSession().isEmpty();
	}

	@Override
	public boolean contains(final String productCode)
	{
		if (productCode == null)
		{
			return false;
		}
		return retrieveQueueFromSession().contains(productCode);
	}

	@Override
	public boolean add(final String code)
	{
		if (code == null)
		{
			return false;
		}

		//RETRIEVE QUEUE
		final Queue queue = retrieveQueueFromSession();

		//if the variable size hits the max, then remove an entry.
		if ((queue.size() == this.MAX_QUEUE_SIZE))
		{
			/*
			 * whilst removing, if the queue does not contain the new code then accomodate by removing the head, else find
			 * and remove the older instance.
			 */
			if (!queue.contains(code))
			{
				queue.remove();
			}
		}
		//if at any time there is an existing code, then first remove it from current position and then add this one to the tail.
		if (queue.contains(code))
		{
			queue.remove(code);
		}

		//UPDATE QUEUE TO SESSION
		if (this.MAX_QUEUE_SIZE > 0)
		{
			queue.add(code);

		}
		updateQueueToSession(queue);

		return true;
	}

	@Override
	public String remove()
	{
		//RETRIEVE CURRENT QUEUE
		final Queue queue = retrieveQueueFromSession();

		if (queue.size() <= 0)
		{
			return null;
		}

		final String removed = (String) queue.remove();

		//UPDATE CURRENT QUEUE
		updateQueueToSession(queue);
		return removed;
	}

	@Override
	public boolean remove(final String productCode)
	{
		//RETRIEVE QUEUE
		final Queue queue = retrieveQueueFromSession();
		if (productCode == null || productCode.isEmpty() || queue.size() < 0)
		{
			return false;
		}
		final boolean removed = queue.remove(productCode);

		//UPDATE QUEUE
		updateQueueToSession(queue);
		return removed;
	}

	@Override
	public void clear()
	{
		final Queue queue = retrieveQueueFromSession();
		queue.clear();
		updateQueueToSession(queue);
	}

	private Queue retrieveQueueFromSession()
	{
		if (sessionService.getAttribute(ProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON) == null)
		{
			sessionService.setAttribute(ProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON,
					new WrapperQueueProductComparison());
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Retrieveing Session id = " + sessionService.getCurrentSession().getSessionId());
		}
		return ((WrapperQueueProductComparison) sessionService
				.getAttribute(ProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON)).getQueue();
	}

	private void updateQueueToSession(final Queue queue)
	{
		final WrapperQueueProductComparison wrapperQueue = (WrapperQueueProductComparison) sessionService
				.getAttribute(ProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON);
		wrapperQueue.setQueue(queue);
		sessionService.setAttribute(ProductcomparisonConstants.SESSION_ATTR_PRODUCTCOMPARISON, wrapperQueue);
	}

	@Override
	public List<WrapperMapVariantAttributes> getProductVariantAttributes(final List<ProductData> productList)
	{
		final List<WrapperMapVariantAttributes> result = new ArrayList<WrapperMapVariantAttributes>();
		if (productList != null && productList.size() > 0)
		{
			//Map<[VariantOptionQualifier.name], Map<[productCode],[VariantOptionQualifier.value]>>
			//final Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

			final ProductData firstProduct = productList.get(0);
			if (firstProduct.getBaseOptions() != null)
			{
				for (final BaseOptionData fBaseOption : firstProduct.getBaseOptions())
				{
					if (fBaseOption.getSelected() != null && fBaseOption.getSelected().getCode().equals(firstProduct.getCode()))
					{
						if (fBaseOption.getSelected().getVariantOptionQualifiers() != null)
						{
							boolean addToCommonList = true;
							for (final VariantOptionQualifierData voqd : fBaseOption.getSelected().getVariantOptionQualifiers())
							{
								final String name = voqd.getName();
								final String qualifier = voqd.getQualifier();
								final Map<String, String> valueMap = new HashMap<String, String>();
								valueMap.put(firstProduct.getCode(), voqd.getValue());
								boolean found = false;
								for (final ProductData product : productList)
								{
									if (product.getBaseOptions() != null && product.getBaseOptions().size() > 0)
									{
										for (final BaseOptionData baseOption : product.getBaseOptions())
										{
											if (baseOption.getSelected() != null
													&& baseOption.getSelected().getCode().equals(product.getCode()))
											{
												if (baseOption.getSelected().getVariantOptionQualifiers() != null)
												{
													for (final VariantOptionQualifierData vo : baseOption.getSelected()
															.getVariantOptionQualifiers())
													{
														if (voqd.getQualifier().equals(vo.getQualifier()))
														{
															found = true;
															valueMap.put(product.getCode(), vo.getValue());
															break;
														}
													}
												}
											}
										}
									}
									if (!found)
									{
										addToCommonList = false;
										break;
									}
								}

								if (addToCommonList)
								{
									final WrapperMapVariantAttributes wrapper = new WrapperMapVariantAttributes();
									wrapper.setName(name);
									wrapper.setQualifier(qualifier);
									wrapper.getProductAttrValueMap().putAll(valueMap);
									result.add(wrapper);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

}

class ProductComparisonClassificationComparator implements Comparator<ClassificationData>
{
	@Override
	public int compare(final ClassificationData a, final ClassificationData b)
	{
		return a.getName().compareTo(b.getName());
	}
}

class ProductComparisonClassificationFeatureComparator implements Comparator<FeatureData>
{
	@Override
	public int compare(final FeatureData a, final FeatureData b)
	{
		return a.getName().compareTo(b.getName());
	}
}


