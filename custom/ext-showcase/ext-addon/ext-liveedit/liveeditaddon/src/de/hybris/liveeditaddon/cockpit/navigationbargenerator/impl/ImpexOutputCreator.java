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

import static de.hybris.liveeditaddon.cockpit.navigationbargenerator.impl.OutputResult.BASIC_OUTPUT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import de.hybris.liveeditaddon.cockpit.navigationbargenerator.OutputCreator;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.ContentSlotData;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.LinkComponentData;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.NavigationBarComponentData;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.NavigationDataHolder;
import de.hybris.liveeditaddon.cockpit.navigationbargenerator.data.NavigationNodeData;



/**
 * Created: Jun 13, 2012
 * 
 * 
 */
public class ImpexOutputCreator implements OutputCreator
{
	private static final Map<String, String> MACROS;
	static
	{
		MACROS = new LinkedHashMap<String, String>();
		MACROS.put("$contentCatalog", ""); // must be changed
		MACROS.put("$contentVersion", ""); // must be changed
		MACROS.put(
				"$catalogVersion",
				"catalogVersion(catalog(id[default=$contentCatalog]),version[default=$contentVersion])[unique=true,default=$contentCatalog:$contentVersion]");
	}
	private static final String CONTENT_CV = ";$catalogVersion;";
	private static final String LANG_MACRO = "$lang";
	private static final String LINK_NAME_TPL = "linkName[lang=" + LANG_MACRO + "]";
	private static final String TITLE_TPL = "title[lang=" + LANG_MACRO + "]";
	private static final String LINK_COMPONENT_HEADER = "INSERT_UPDATE CMSLinkComponent;uid[unique=true];name;url;&linkRef;target(code)[default='sameWindow']"
			+ CONTENT_CV;
	private static final String LINK_COMPONENT_UPDATE_HEADER = "UPDATE CMSLinkComponent;uid[unique=true]";
	private static final String NAVIGATION_NODE_HEADER = "INSERT_UPDATE CMSNavigationNode;uid[unique=true];name;parent(uid, $catalogVersion);links(&linkRef);&nodeRef"
			+ CONTENT_CV;
	private static final String NAVIGATION_NODE_UPDATE_HEADER = "UPDATE CMSNavigationNode;uid[unique=true]";
	private static final String NAVIGATION_BAR_COMPONENT_HEADER = "INSERT_UPDATE NavigationBarComponent;uid[unique=true];name;wrapAfter;link(&linkRef);styleClass;&componentRef;navigationNode(&nodeRef);dropDownLayout(code)[default=AUTO]"
			+ CONTENT_CV;
	private static final String CONTENT_SLOT_HEADER = "INSERT_UPDATE ContentSlot;uid[unique=true];cmsComponents(&componentRef)[mode=append]"
			+ CONTENT_CV;

	private static final String LIST_SEPARATOR = ",";
	private static final String FIELDS_SEPARATOR = ";";
	private static final String EOL = System.getProperty("line.separator");

	private String catalogId;
	private String catalogVersion;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputResult createOutput(final NavigationDataHolder data)
	{
		final OutputResult outputResult = new OutputResult(data.getLanguages());

		prepareHeaders(outputResult, data);
		prepareLinkComponents(outputResult, data);
		prepareNavigationNodes(outputResult, data);

		prepareLinkComponentsUpdate(outputResult, data);
		prepareNavigationNodesUpdate(outputResult, data);
		prepareNavigationBarComponents(outputResult, data);
		prepareContentSlot(outputResult, data);
		return outputResult;
	}

	/**
	 * This method prepares part of text with inserting macros
	 * 
	 * @param output
	 *           StringBuilder for appending part of impex
	 * @param data
	 *           {@link NavigationDataHolder} with needed data
	 */
	private void prepareHeaders(final OutputResult output, final NavigationDataHolder data)
	{
		MACROS.put("$contentCatalog", data.getContentCatalogId());
		MACROS.put("$contentVersion", data.getContentCatalogVersionName());
		for (final Entry<String, String> entry : MACROS.entrySet())
		{
			output.appendToAll(entry.getKey()).appendToAll("=").appendToAll(entry.getValue()).appendToAll(EOL);
		}
		output.appendToAll(EOL);
	}

	/**
	 * @param fields
	 *           String array of fields
	 * @return assembled line, starting with ";"
	 */
	private String assembleLine(final String[] fields)
	{
		final List<String> fieldList = new ArrayList<String>(Arrays.asList(fields));
		return assembleLine(fieldList);
	}

	/**
	 * @param fields
	 *           Collection of fields
	 * @return assembled line, starting with ";"
	 */
	private String assembleLine(final Collection<String> fields)
	{
		return FIELDS_SEPARATOR + StringUtils.join(fields, FIELDS_SEPARATOR);
	}

	/**
	 * This method prepares part of text with inserting CMSLinkComponent items
	 * 
	 * @param output
	 *           StringBuilder for appending part of impex
	 * @param data
	 *           {@link NavigationDataHolder} with needed data
	 */
	private void prepareLinkComponents(final OutputResult output, final NavigationDataHolder data)
	{
		output.append(EOL, BASIC_OUTPUT).append(LINK_COMPONENT_HEADER, BASIC_OUTPUT).append(EOL, BASIC_OUTPUT);
		for (final LinkComponentData link : data.getLinkComponents())
		{
			final String[] componentFields =
			{ link.getUid(), link.getName(), link.getUrl(), link.getRef() };
			output.append(assembleLine(componentFields), BASIC_OUTPUT).append(EOL, BASIC_OUTPUT);
		}
	}

	/**
	 * This method prepares part of text with inserting CMSNavigationNode items
	 * 
	 * @param output
	 *           StringBuilder for appending part of impex
	 * @param data
	 *           {@link NavigationDataHolder} with needed data
	 */
	private void prepareNavigationNodes(final OutputResult output, final NavigationDataHolder data)
	{
		output.append(EOL, BASIC_OUTPUT).append(NAVIGATION_NODE_HEADER, BASIC_OUTPUT).append(EOL, BASIC_OUTPUT);
		for (final NavigationNodeData navi : data.getNavigationNodes())
		{
			final String links = StringUtils.join(navi.getLinkReferences(), LIST_SEPARATOR);
			final String parentUid = navi.getParent() != null ? navi.getParent().getUid() : "";

			final String[] componentFields =
			{ navi.getUid(), navi.getName(), parentUid, links, navi.getRef() };
			output.append(assembleLine(componentFields), BASIC_OUTPUT).append(EOL, BASIC_OUTPUT);
		}
	}

	/**
	 * This method prepares part of text with inserting NavigationBarComponent items
	 * 
	 * @param output
	 *           StringBuilder for appending part of impex
	 * @param data
	 *           {@link NavigationDataHolder} with needed data
	 */
	private void prepareNavigationBarComponents(final OutputResult output, final NavigationDataHolder data)
	{
		output.append(EOL, BASIC_OUTPUT).append(NAVIGATION_BAR_COMPONENT_HEADER, BASIC_OUTPUT).append(EOL, BASIC_OUTPUT);
		for (final NavigationBarComponentData component : data.getContentSlotData().getComponents())
		{
			final String linkRef = component.getLink() != null ? component.getLink().getRef() : "";
			final String naviRef = component.getNavigationNode() != null ? component.getNavigationNode().getRef() : "";
			final String[] componentFields =
			{ component.getUid(), component.getName(), "10", linkRef, "", component.getRef(), naviRef };
			output.append(assembleLine(componentFields), BASIC_OUTPUT).append(EOL, BASIC_OUTPUT);
		}
	}

	/**
	 * This method prepares part of text with updating ContentSlot item
	 * 
	 * @param output
	 *           StringBuilder for appending part of impex
	 * @param data
	 *           {@link NavigationDataHolder} with needed data
	 */
	private void prepareContentSlot(final OutputResult output, final NavigationDataHolder data)
	{
		final ContentSlotData contentSlot = data.getContentSlotData();
		final String[] contentSlotFields =
		{ contentSlot.getUid(), StringUtils.join(contentSlot.getComponentsReferences(), LIST_SEPARATOR) };
		output.append(EOL, BASIC_OUTPUT).append(CONTENT_SLOT_HEADER, BASIC_OUTPUT).append(EOL, BASIC_OUTPUT);
		output.append(assembleLine(contentSlotFields), BASIC_OUTPUT).append(EOL, BASIC_OUTPUT);
	}

	/**
	 * This method prepares part of text with updating CMSLinkComponent items and populating them with linkNames
	 * 
	 * @param output
	 *           StringBuilder for appending part of impex
	 * @param data
	 *           {@link NavigationDataHolder} with needed data
	 */
	private void prepareLinkComponentsUpdate(final OutputResult output, final NavigationDataHolder data)
	{
		// header
		final Collection<String> languages = new ArrayList<String>(data.getLanguages());
		final Collection<String> linkNameHeaders = new ArrayList<String>(languages.size());
		for (final String language : languages)
		{
			linkNameHeaders.add(LINK_NAME_TPL.replace(LANG_MACRO, language));

			output.append(EOL, language).append(LINK_COMPONENT_UPDATE_HEADER, language)
					.append(assembleLine(linkNameHeaders), language).append(CONTENT_CV, language).append(EOL, language);

			// data
			for (final LinkComponentData link : data.getLinkComponents())
			{
				final Map<String, String> linkNames = link.getLinkNames();
				final List<String> componentFields = new ArrayList<String>(languages.size() + 1);
				componentFields.add(link.getUid());
				componentFields.add(linkNames.get(language));
				output.append(assembleLine(componentFields), language).append(EOL, language);
			}
			linkNameHeaders.clear();
		}
	}

	/**
	 * This method prepares part of text with updating CMSNavigationNode items and populating them with titles
	 * 
	 * @param output
	 *           StringBuilder for appending part of impex
	 * @param data
	 *           {@link NavigationDataHolder} with needed data
	 */
	private void prepareNavigationNodesUpdate(final OutputResult output, final NavigationDataHolder data)
	{
		// header
		final Collection<String> languages = new ArrayList<String>(data.getLanguages());
		final Collection<String> titleHeaders = new ArrayList<String>(languages.size());
		for (final String language : languages)
		{
			titleHeaders.add(TITLE_TPL.replace(LANG_MACRO, language));

			output.append(EOL, language).append(NAVIGATION_NODE_UPDATE_HEADER, language)
					.append(assembleLine(titleHeaders), language).append(CONTENT_CV, language).append(EOL, language);

			// data
			for (final NavigationNodeData link : data.getNavigationNodes())
			{
				final Map<String, String> titles = link.getTitles();
				final List<String> componentFields = new ArrayList<String>(languages.size() + 1);
				componentFields.add(link.getUid());
				componentFields.add(titles.get(language));
				output.append(assembleLine(componentFields), language).append(EOL, language);
			}
			titleHeaders.clear();
		}
	}

}
