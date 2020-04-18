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
package com.greenlee.greenleesalsifyservices.dataimport.batch.task;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.datahub.csv.domain.CsvData;
import com.hybris.datahub.csv.service.CsvReaderService;


/**
 * @author savarimuthu.s
 *
 *         Initially setup the batch header. The header is used throughout the pipeline as a reference and for cleanup.
 */
public class GreenleeClassificationSetupTask
{
	protected String storeBaseDirectory;

	protected String catalog;
	protected boolean net;
	protected CsvReaderService csvReaderService;
	protected String fieldSeparator;

	/**
	 * Initially creates the header.
	 *
	 * @param file
	 * @return the header
	 */
	public BatchHeader execute(final File file)
	{
		Assert.notNull(file);
		final BatchHeader result = new BatchHeader();
		result.setFile(file);

		InputStream targetStream = null;
		File fileToWritten = null;
		try
		{
			fileToWritten = new File(
					"..\\data\\acceleratorservices\\import\\master\\gre_classification_class" + System.currentTimeMillis() + ".csv");
			fileToWritten.createNewFile();
			result.setStoreBaseDirectory(storeBaseDirectory);
			result.setCatalog(catalog);
			result.setNet(net);
			targetStream = new FileInputStream(file);
			final CsvData csvData = csvReaderService.extractBodyAndHeaders(targetStream);
			final StringBuilder stringToWrite = writeClassificationClass(csvData);
			FileUtils.writeStringToFile(fileToWritten, stringToWrite.toString());

		}
		catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return result;
	}



	/*
	 *
	 * material number,unspsc code, classification name 000000000001111,14111512,Graph paper
	 * 000000000001112,14111512,Graph paper 000000000001113,14111818,Thermal paper
	 */
	private StringBuilder writeClassificationClass(final CsvData csvData)
	{
		final Map<String, List<String>> multimap = new HashMap<String, List<String>>();
		final LinkedList<String> classHeader = new LinkedList<String>(csvData.getCsvHeaders());
		final LinkedList<String> newCsvBody = new LinkedList<String>();
		final StringBuilder rows = new StringBuilder(1024);
		final String csvInputHeaders = StringUtils.join(classHeader, fieldSeparator);
		rows.append(csvInputHeaders);
		rows.append(System.lineSeparator());
		for (int x = 0; x < csvData.getCsvBody().size(); ++x)
		{
			final LinkedList<String> csvBody = new LinkedList<String>(csvData.getCsvBody().get(x));
			final String unspscCode = csvBody.get(1).trim();
			final String newMaterialNumber = csvBody.get(0);
			if (!multimap.containsKey(unspscCode))
			{
				multimap.put(unspscCode, csvBody); //create a map
			}
			else
			{
				final List<String> fromMapStrings = multimap.get(unspscCode);
				final LinkedList<String> classBody = new LinkedList<String>();
				final String oldMaterialNumber = fromMapStrings.get(0);
				multimap.remove(unspscCode);
				classBody.add("\"" + oldMaterialNumber);
				classBody.add(newMaterialNumber + "\"");
				final String className = fromMapStrings.get(2);
				final String newMaterialNumbers = StringUtils.join(classBody, fieldSeparator);
				newCsvBody.add(newMaterialNumbers);
				newCsvBody.add(unspscCode);
				newCsvBody.add(className);
				multimap.put(unspscCode, newCsvBody);//override that map
			}
		}
		csvData.getCsvBody().clear();
		csvData.getCsvBody().addAll(multimap.values());
		for (int csv = 0; csv < csvData.getCsvBody().size(); ++csv)//loop through the list value on csv data.
		{
			final List<String> row = csvData.getCsvBody().get(csv);
			for (final String stringRows : row)
			{
				rows.append(stringRows);
				rows.append(System.lineSeparator());
			}
		}
		return rows;
	}

	private StringBuilder classWriteClassificationClass(final LinkedHashMap<String, List<String>> hashMap, final CsvData csvData)
	{
		final StringBuilder rows = new StringBuilder(1024);
		final LinkedList<String> classHeader = new LinkedList<String>(csvData.getCsvHeaders());
		final String csvInputHeaders = StringUtils.join(classHeader, fieldSeparator);
		rows.append(csvInputHeaders);
		rows.append(System.lineSeparator());

		return rows;
	}

	private HashMap<String, List<String>> newwriteClassificationClass(final CsvData csvData)
	{
		final HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

		for (int x = 0; x < csvData.getCsvBody().size(); ++x)
		{
			hashMap.put(csvData.getCsvBody().get(x).get(1), csvData.getCsvBody().get(x));
		}
		return hashMap;
	}

	public String getStoreBaseDirectory()
	{
		return storeBaseDirectory;
	}

	@Required
	public void setStoreBaseDirectory(final String storeBaseDirectory)
	{
		this.storeBaseDirectory = storeBaseDirectory;
	}

	public String getCatalog()
	{
		return catalog;
	}

	@Required
	public void setCatalog(final String catalog)
	{
		this.catalog = catalog;
	}


	public boolean isNet()
	{
		return net;
	}

	@Required
	public void setNet(final boolean net)
	{
		this.net = net;
	}

	/**
	 * @return the csvReaderService
	 */
	public CsvReaderService getCsvReaderService()
	{
		return csvReaderService;
	}

	/**
	 * @param csvReaderService
	 *           the csvReaderService to set
	 */
	@Required
	public void setCsvReaderService(final CsvReaderService csvReaderService)
	{
		this.csvReaderService = csvReaderService;
	}


	/**
	 * @return the fieldSeparator
	 */
	public String getFieldSeparator()
	{
		return fieldSeparator;
	}

	/**
	 * @param fieldSeparator
	 *           the fieldSeparator to set
	 */
	@Required
	public void setFieldSeparator(final String fieldSeparator)
	{
		this.fieldSeparator = fieldSeparator;
	}

}
