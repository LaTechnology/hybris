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
package de.hybris.liveeditaddon.cockpit.navigationbargenerator.impl;

import de.hybris.liveeditaddon.cockpit.navigationbargenerator.CategoriesReader;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.NavigationBarGenerator;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.OutputCreator;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.*;
import de.hybris.platform.core.model.c2l.LanguageModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Created: Jun 13, 2012
 *
 *
 */
public class NavigationBarGeneratorImpl implements NavigationBarGenerator {
	private static final String PREFIX_LINK_UID = "LinkComponent-";
	private static final String PREFIX_LINK_REF = "LinkRef-";
	private static final String PREFIX_NAVI_NODE_UID = "NaviNode-";
	private static final String PREFIX_NAVI_NODE_REF = "NaviNodeRef-";
	private static final String PREFIX_LINK_NAVI_NODE_UID = "LinkNaviNode-";
	private static final String PREFIX_LINK_NAVI_NODE_REF = "LinkNaviNodeRef-";
	private static final String PREFIX_NAVI_BAR_UID = "NaviBarComponent-";
	private static final String PREFIX_NAVI_BAR_REF = "NaviBarRef-";
	private static final String SUFFIX_LINK_NAME = " Link Component";
	private static final String SUFFIX_NAVI_NODE_NAME = " Navigation Node";
	private static final String SUFFIX_LINK_NAVI_NODE_NAME = " Link Navigation Node";
	private static final String SUFFIX_NAVI_BAR_NAME = " Navigation Bar Component";

	private CategoriesReader categoriesReader;
	private OutputCreator outputCreator;

	protected CategoriesReader getCategoriesReader()
	{
		return categoriesReader;
	}

	public void setCategoriesReader(final CategoriesReader categoriesReader)
	{
		this.categoriesReader = categoriesReader;
	}

	protected OutputCreator getOutputCreator()
	{
		return outputCreator;
	}

	@Required
	public void setOutputCreator(final OutputCreator outputCreator)
	{
		this.outputCreator = outputCreator;
	}

	@Override
    public OutputResult runGeneratorForCatalog(final String productCatalogId, final String productCatalogVersionName,
                                               final String contentCatalogId, final String contentCatalogVersionName, final Integer level, final String[] rootCategories,
                                               final boolean shouldHaveProducts, final Set<String> localizations)
	{
		final CategoryData categoryData = categoriesReader.findCategoriesForCatalog(productCatalogId, productCatalogVersionName,
				rootCategories, level, shouldHaveProducts, localizations);
		final NavigationDataHolder dataHolder = transformCategoryData(categoryData);
		dataHolder.setContentCatalogId(contentCatalogId);
		dataHolder.setContentCatalogVersionName(contentCatalogVersionName);
		return outputCreator.createOutput(dataHolder);
	}

	@Override
    public OutputResult runGeneratorForCatalog(final String productCatalogId, final String productCatalogVersionName,
                                               final String contentCatalogId, final String contentCatalogVersionName, final Integer level,
                                               final boolean shouldHaveProducts, final Set<String> localizations)
	{
		final CategoryData categoryData = categoriesReader.findCategoriesForCatalog(productCatalogId, productCatalogVersionName,
				level, shouldHaveProducts, localizations);
		final NavigationDataHolder dataHolder = transformCategoryData(categoryData);
		dataHolder.setContentCatalogId(contentCatalogId);
		dataHolder.setContentCatalogVersionName(contentCatalogVersionName);
		return outputCreator.createOutput(dataHolder);
	}


    /**
     * This method returns inner join of 2 localization collections (set of iso codes located in both collection). If one of the
     * given collection is null, then this method return empty set.
     *
     * @param firstLanguagesCollection
     * @param secondLanguagesCollection
     * @return unmodifiable set with iso codes of localization
     */
    @Override
    public Set<String> generateInnerJoinOfLanguages(Collection<LanguageModel> firstLanguagesCollection, Collection<LanguageModel> secondLanguagesCollection) {
        if ((null == firstLanguagesCollection) || (firstLanguagesCollection.isEmpty())) {
            return Collections.EMPTY_SET;
        }

        if ((null == secondLanguagesCollection) || (secondLanguagesCollection.isEmpty())) {
            return Collections.EMPTY_SET;
        }

        final Set<String> jointLanguages = new HashSet<String>();
        for (LanguageModel languageModelFromFirstCollection: firstLanguagesCollection) {
            for (LanguageModel languageModelFromSecondCollection : secondLanguagesCollection) {
                if (languageModelFromFirstCollection.getIsocode().equals(languageModelFromSecondCollection.getIsocode())) {
                    jointLanguages.add(languageModelFromFirstCollection.getIsocode());
                }
            }
        }
        return Collections.unmodifiableSet(jointLanguages);
    }

	private NavigationDataHolder transformCategoryData(final CategoryData rootCategory)
	{
		validateParameterNotNull(rootCategory, "rootCategory cannot be null");
		final NavigationDataHolder dataHolder = new NavigationDataHolder();

		// content slot
		final ContentSlotData contentSlot = new ContentSlotData();
		contentSlot.setUid("NavigationBarSlot");
		dataHolder.setContentSlotData(contentSlot);
		recursiveTransform(rootCategory, rootCategory, dataHolder);
		return dataHolder;
	}

	private void recursiveTransform(final CategoryData currentCategory, final CategoryData rootCategory,
			final NavigationDataHolder dataHolder)
	{
		createLinkComponent(currentCategory, dataHolder);
		createNaviNode(currentCategory, dataHolder);
		if (currentCategory.isMain())
		{
			createNaviBar(currentCategory, dataHolder);
		}
		for (final CategoryData subCat : currentCategory.getSubcategories())
		{
			recursiveTransform(subCat, rootCategory, dataHolder);
		}
	}


	/**
	 *
	 * @param category
	 * @param dataHolder
	 * @return freshly created {@link LinkComponentData}
	 */
	private LinkComponentData createLinkComponent(final CategoryData category, final NavigationDataHolder dataHolder)
	{
		final LinkComponentData link = new LinkComponentData();
		link.setUid(PREFIX_LINK_UID + category.getCode());
		link.setRef(PREFIX_LINK_REF + category.getCode());
		link.setName(category.getName() + SUFFIX_LINK_NAME);
		link.setUrl(category.getUrl());

		final Map<String, String> categoryNames = category.getNames();
		for (final String lang : categoryNames.keySet())
		{
			final String catName = categoryNames.get(lang);
			if (catName != null && !catName.isEmpty())
			{
				dataHolder.addLanguage(lang);
				link.addLinkName(lang, catName);
			}
		}
		dataHolder.addLinkComponent(link);
		return link;
	}

	/**
	 *
	 * @param category
	 * @param dataHolder
	 * @return freshly created {@link NavigationNodeData}
	 */
	private NavigationNodeData createNaviNode(final CategoryData category, final NavigationDataHolder dataHolder)
	{
		if (category.getSupercategory() == null)
		{
			// create NavNode
			final NavigationNodeData navi = createNavigationNode(dataHolder, PREFIX_NAVI_NODE_UID, PREFIX_NAVI_NODE_REF,
					SUFFIX_NAVI_NODE_NAME, category, false);
			return navi;
		}
		else if (category.isMain())
		{
			// create NavNode
			final NavigationNodeData navi = createNavigationNode(dataHolder, PREFIX_NAVI_NODE_UID, PREFIX_NAVI_NODE_REF,
					SUFFIX_NAVI_NODE_NAME, category, false);
			navi.setParent(dataHolder.getNavigationNodeForUid(PREFIX_NAVI_NODE_UID +  category.getSupercategory().getCode()));
			if (category.hasLonelySubcategories())
			{
				// create empty-titled LinkNavNode
				final NavigationNodeData linkNavi = createNavigationNode(dataHolder, PREFIX_LINK_NAVI_NODE_UID,
						PREFIX_LINK_NAVI_NODE_REF, SUFFIX_LINK_NAVI_NODE_NAME, category, false);
				linkNavi.setParent(dataHolder.getNavigationNodeForUid(PREFIX_NAVI_NODE_UID + category.getCode()));
			}
			else
			{
				// do NOT create LinkNavNode
			}
			return navi;
		}
		else
		{
			final CategoryData superCat = category.getSupercategory();
			if (superCat != null && superCat.isMain())
			{
				if (category.getSubcategories().isEmpty())
				{
					// add link to parent's LinkNavNode (the one without title)
					if (category.hasProducts())
					{
						final NavigationNodeData linkNaviNode = dataHolder.getNavigationNodeForUid(PREFIX_LINK_NAVI_NODE_UID
								+ superCat.getCode());
						final LinkComponentData link = dataHolder.getLinkComponentForUid(PREFIX_LINK_UID + category.getCode());
						linkNaviNode.addLink(link);
						return linkNaviNode;
					}
				}
				else
				{
					// 1. create LinkNavNode
					final NavigationNodeData linkNavi = createNavigationNode(dataHolder, PREFIX_LINK_NAVI_NODE_UID,
							PREFIX_LINK_NAVI_NODE_REF, SUFFIX_LINK_NAVI_NODE_NAME, category, true);

					// 2. set parent as main NavNode (not LinkNavNode)
					linkNavi.setParent(dataHolder.getNavigationNodeForUid(PREFIX_NAVI_NODE_UID + superCat.getCode()));
					return linkNavi;
				}
			}
			else
			{
				// add link to parent's LinkNavNode
				final NavigationNodeData linkNaviNode = dataHolder.getNavigationNodeForUid(PREFIX_LINK_NAVI_NODE_UID
						+ superCat.getCode());
                //jh: fix issue with NPE on deep tree
				if ((category.hasProducts()) && (linkNaviNode != null))
				{
					final LinkComponentData link = dataHolder.getLinkComponentForUid(PREFIX_LINK_UID + category.getCode());
					linkNaviNode.addLink(link);
				}
				return linkNaviNode;
			}
		}
		return null;
	}

	private NavigationNodeData createNavigationNode(final NavigationDataHolder dataHolder, final String prefixUid,
			final String prefixRef, final String suffix, final CategoryData category, final boolean addTitles)
	{
		final NavigationNodeData navi = new NavigationNodeData();
		navi.setUid(prefixUid + category.getCode());
		navi.setRef(prefixRef + category.getCode());
		navi.setName(category.getName() + suffix);

		if (addTitles)
		{
			final Map<String, String> categoryNames = category.getNames();
			for (final String lang : categoryNames.keySet())
			{
				final String value = categoryNames.get(lang);
				if (value != null && !value.isEmpty())
				{
					dataHolder.addLanguage(lang);
					navi.addTitle(lang, value);
				}
			}
		}
		dataHolder.addNavigationNode(navi);
		return navi;
	}

	/**
	 * @param category
	 * @param dataHolder
	 * @return freshly created {@link NavigationNodeData}
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private NavigationNodeData createNaviNode_old(final CategoryData category, final NavigationDataHolder dataHolder)
	{
		// last level - no navigation nodes but add link for supercategory
		if (category.getSubcategories().isEmpty() && !category.isMain() && !category.hasProducts())
		{
			final CategoryData supCat = category.getSupercategory();
			if (supCat != null)
			{
				final String code = supCat.getCode();
				final NavigationNodeData naviNode = dataHolder.getNavigationNodeForUid(PREFIX_NAVI_NODE_UID + code);
				final LinkComponentData link = dataHolder.getLinkComponentForUid(PREFIX_LINK_UID + code);
				naviNode.addLink(link);
			}
			return null;
		}
		else
		{
			final NavigationNodeData navi = new NavigationNodeData();
			navi.setUid(PREFIX_NAVI_NODE_UID + category.getCode());
			navi.setRef(PREFIX_NAVI_NODE_REF + category.getCode());
			navi.setName(category.getName() + SUFFIX_NAVI_NODE_NAME);

			if (category.getSupercategory() != null)
			{
				navi.setParent(dataHolder.getNavigationNodeForUid(PREFIX_NAVI_NODE_UID + category.getSupercategory().getCode()));
			}
			final Map<String, String> categoryNames = category.getNames();
			for (final String lang : categoryNames.keySet())
			{
				final String value = categoryNames.get(lang);
				if (value != null && !value.isEmpty())
				{
					dataHolder.addLanguage(lang);
					navi.addTitle(lang, value);
				}
			}
			dataHolder.addNavigationNode(navi);
			return navi;
		}
	}

	/**
	 * Only for main categories
	 *
	 * @param category
	 *           root category
	 * @param dataHolder
	 * @return freshly created {@link NavigationBarComponentData}
	 */
	private NavigationBarComponentData createNaviBar(final CategoryData category, final NavigationDataHolder dataHolder)
	{
		final NavigationBarComponentData naviBar = new NavigationBarComponentData();
		dataHolder.getContentSlotData().addComponent(naviBar);
		naviBar.setUid(PREFIX_NAVI_BAR_UID + category.getCode());
		naviBar.setRef(PREFIX_NAVI_BAR_REF + category.getCode());
		naviBar.setName(category.getName() + SUFFIX_NAVI_BAR_NAME);
		naviBar.setLink(dataHolder.getLinkComponentForUid(PREFIX_LINK_UID + category.getCode()));
		naviBar.setNavigationNode(dataHolder.getNavigationNodeForUid(PREFIX_NAVI_NODE_UID + category.getCode()));

		dataHolder.addNavigationComponent(naviBar);
		return naviBar;
	}

	@SuppressWarnings("unused")
	private void retrieveLanguagesFromCategory(final CategoryData category, final NavigationDataHolder dataHolder)
	{
		final Map<String, String> categoryNames = category.getNames();
		for (final String lang : categoryNames.keySet())
		{
			final String value = categoryNames.get(lang);
			if (value != null && !value.isEmpty())
			{
				dataHolder.addLanguage(lang);
			}
		}
	}

	public static void main(final String[] args)
	{
		testOutput();
	}

	public static void testOutput()
	{
		final NavigationDataHolder data = new NavigationDataHolder();

		final Collection<String> languages = new ArrayList<String>();
		languages.add("en");
		languages.add("de");

		// links
		final Collection<LinkComponentData> linkComponents1 = new ArrayList<LinkComponentData>();
		final Collection<LinkComponentData> linkComponents2 = new ArrayList<LinkComponentData>();

		final LinkComponentData link1 = new LinkComponentData();
		link1.setUid("Link1-uid");
		link1.setName("Link 1 name");
		link1.setRef("Link1-ref");
		link1.setUrl("/c/vvvv1");
		final Map<String, String> linkNames1 = new HashMap<String, String>();
		linkNames1.put("en", "the Link 1");
		linkNames1.put("de", "das Link 1");
		link1.setLinkNames(linkNames1);
		data.addLinkComponent(link1);

		final LinkComponentData link2 = new LinkComponentData();
		link2.setUid("Link2-uid");
		link2.setName("Link 2 name");
		link2.setRef("Link2-ref");
		link2.setUrl("/c/vvvv2");
		final Map<String, String> linkNames2 = new HashMap<String, String>();
		linkNames2.put("en", "the Link 2");
		linkNames2.put("de", "das Link 2");
		link2.setLinkNames(linkNames2);
		data.addLinkComponent(link2);

		final LinkComponentData link11 = new LinkComponentData();
		link11.setUid("Link11-uid");
		link11.setName("Link 11 name");
		link11.setRef("Link11-ref");
		link11.setUrl("/c/vvvv11");
		final Map<String, String> linkNames11 = new HashMap<String, String>();
		linkNames11.put("en", "the Link 11");
		linkNames11.put("de", "das Link 11");
		link11.setLinkNames(linkNames11);
		linkComponents1.add(link11);
		data.addLinkComponent(link11);

		final LinkComponentData link12 = new LinkComponentData();
		link12.setUid("Link12-uid");
		link12.setName("Link 12 name");
		link12.setRef("Link12-ref");
		link12.setUrl("/c/vvvv12");
		final Map<String, String> linkNames12 = new HashMap<String, String>();
		linkNames12.put("en", "the Link 12");
		linkNames12.put("de", "das Link 12");
		link12.setLinkNames(linkNames12);
		linkComponents1.add(link12);
		data.addLinkComponent(link12);

		final LinkComponentData link21 = new LinkComponentData();
		link21.setUid("Link21-uid");
		link21.setName("Link 21 name");
		link21.setRef("Link21-ref");
		link21.setUrl("/c/vvvv21");
		final Map<String, String> linkNames21 = new HashMap<String, String>();
		linkNames21.put("en", "the Link 21");
		linkNames21.put("de", "das Link 21");
		link21.setLinkNames(linkNames21);
		linkComponents2.add(link21);
		data.addLinkComponent(link21);

		final LinkComponentData link22 = new LinkComponentData();
		link22.setUid("Link22-uid");
		link22.setName("Link 22 name");
		link22.setRef("Link22-ref");
		link22.setUrl("/c/vvvv22");
		final Map<String, String> linkNames22 = new HashMap<String, String>();
		linkNames22.put("en", "the Link 22");
		linkNames22.put("de", "das Link 22");
		link22.setLinkNames(linkNames22);
		linkComponents2.add(link22);
		data.addLinkComponent(link22);

		// navigation nodes
		final Set<NavigationNodeData> navigationNodes = new HashSet<NavigationNodeData>(2);

		final NavigationNodeData navigationNode0 = new NavigationNodeData();
		navigationNode0.setUid("naviNode0-uid");
		navigationNode0.setRef("naviNode0-ref");
		navigationNode0.setName("naviNode0-name");
		navigationNode0.setParent(null);
		navigationNode0.setLinks(null);
		final Map<String, String> titles0 = new HashMap<String, String>();
		titles0.put("en", "the naviNode0-title-en");
		titles0.put("de", "das naviNode0-title-de");
		navigationNode0.setTitles(titles0);
		navigationNodes.add(navigationNode0);

		final NavigationNodeData navigationNode1 = new NavigationNodeData();
		navigationNode1.setUid("naviNode1-uid");
		navigationNode1.setRef("naviNode1-ref");
		navigationNode1.setName("naviNode1-name");
		navigationNode1.setParent(navigationNode0);
		navigationNode1.setLinks(null);
		final Map<String, String> titles1 = new HashMap<String, String>();
		titles1.put("en", "the naviNode1-title-en");
		titles1.put("de", "das naviNode1-title-de");
		navigationNode1.setTitles(titles1);
		navigationNodes.add(navigationNode1);

		final NavigationNodeData navigationNode2 = new NavigationNodeData();
		navigationNode2.setUid("naviNode2-uid");
		navigationNode2.setRef("naviNode2-ref");
		navigationNode2.setName("naviNode2-name");
		navigationNode2.setParent(navigationNode0);
		navigationNode2.setLinks(null);
		final Map<String, String> titles2 = new HashMap<String, String>();
		titles2.put("en", "the naviNode2-title-en");
		titles2.put("de", "das naviNode2-title-de");
		navigationNode2.setTitles(titles2);
		navigationNodes.add(navigationNode2);

		final NavigationNodeData navigationLinkNode1 = new NavigationNodeData();
		navigationLinkNode1.setUid("naviLinkNode1-uid");
		navigationLinkNode1.setRef("naviLinkNode1-ref");
		navigationLinkNode1.setName("naviLinkNode1-name");
		navigationLinkNode1.setParent(navigationNode1);
		navigationLinkNode1.setLinks(linkComponents1);
		final Map<String, String> titlesLink1 = new HashMap<String, String>();
		titlesLink1.put("en", "the naviLinkNode1-title-en");
		titlesLink1.put("de", "das naviLinkNode1-title-de");
		navigationLinkNode1.setTitles(titlesLink1);
		navigationNodes.add(navigationLinkNode1);

		final NavigationNodeData navigationLinkNode2 = new NavigationNodeData();
		navigationLinkNode2.setUid("naviLinkNode2-uid");
		navigationLinkNode2.setRef("naviLinkNode2-ref");
		navigationLinkNode2.setName("naviLinkNode2-name");
		navigationLinkNode2.setParent(navigationNode2);
		navigationLinkNode2.setLinks(linkComponents2);
		final Map<String, String> titlesLink2 = new HashMap<String, String>();
		titlesLink2.put("en", "the naviLinkNode2-title-en");
		titlesLink2.put("de", "das naviLinkNode2-title-de");
		navigationLinkNode2.setTitles(titlesLink2);
		navigationNodes.add(navigationLinkNode2);


		// navigation bars
		final Set<NavigationBarComponentData> navigationBarComponents = new HashSet<NavigationBarComponentData>();

		final NavigationBarComponentData naviBar1 = new NavigationBarComponentData();
		naviBar1.setUid("naviBar1-uid");
		naviBar1.setRef("naviBar1-ref");
		naviBar1.setName("naviBar1-name");
		naviBar1.setLink(link1);
		naviBar1.setNavigationNode(navigationNode1);
		navigationBarComponents.add(naviBar1);

		final NavigationBarComponentData naviBar2 = new NavigationBarComponentData();
		naviBar2.setUid("naviBar2-uid");
		naviBar2.setRef("naviBar2-ref");
		naviBar2.setName("naviBar2-name");
		naviBar2.setLink(link2);
		naviBar2.setNavigationNode(navigationNode1);
		navigationBarComponents.add(naviBar2);

		final ContentSlotData contentSlot = new ContentSlotData();
		contentSlot.setUid("NavigationBarSlot");
		contentSlot.setComponents(navigationBarComponents);

		data.setLanguages(languages);
		//data.setLinkComponents(allLinkComponents);
		data.setNavigationNodes(navigationNodes);
		data.setNavigationBarComponents(navigationBarComponents);
		data.setContentSlotData(contentSlot);

		//final String output = outputCreator.createOutput(data);
		//System.out.println("output=" + output);
	}

}
