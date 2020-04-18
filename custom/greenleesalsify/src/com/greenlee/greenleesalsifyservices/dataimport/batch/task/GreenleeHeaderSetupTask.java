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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.datahub.csv.domain.CsvData;
import com.hybris.datahub.csv.service.CsvReaderService;


/**
 * @author savarimuthu.s
 *
 *         Initially setup the batch header. The header is used throughout the pipeline as a reference and for cleanup.
 */
public class GreenleeHeaderSetupTask
{
	protected String storeBaseDirectory;

	protected String catalog;
	protected boolean net;
	protected String attributecode;
	protected CsvReaderService csvReaderService;
	private static String[] csvHeaders;
	private static String[][] csvBody;
	private static LinkedList<String> newCsvHeaders;
	protected String fieldSeparator;
	protected String headerLastIndex;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");


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
		final TimeStamp timestamp = new TimeStamp(System.currentTimeMillis());
		final String timeStamps = sdf.format(timestamp);

		InputStream targetStream = null;
		File fileToWritten = null;
		try
		{
			fileToWritten = new File("..\\data\\acceleratorservices\\import\\master\\gre_class_attr" + timeStamps + ".csv");
			fileToWritten.createNewFile();
			result.setStoreBaseDirectory(storeBaseDirectory);
			result.setCatalog(catalog);
			result.setNet(net);
			targetStream = new FileInputStream(file);
			final CsvData csvData = csvReaderService.extractBodyAndHeaders(targetStream);
			final StringBuilder stringToWrite = writeStringToCSVFile02(csvData);
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

	private int getAttributeIndexValue()
	{
		return newCsvHeaders.indexOf(attributecode);
	}

	private int getLastIndex()
	{
		return newCsvHeaders.indexOf(headerLastIndex);
	}

	private int getHeaderSize()
	{
		return newCsvHeaders.size();
	}


	private StringBuilder writeStringToCSVFile(final CsvData csvData)
	{
		newCsvHeaders = new LinkedList<String>(csvData.getCsvHeaders());
		final int attributePosition = getAttributeIndexValue();
		newCsvHeaders.remove(attributePosition);
		newCsvHeaders.add(0, StringEscapeUtils.escapeCsv(attributecode));
		final StringBuilder rows = new StringBuilder(1024);
		final String csvInputHeaders = StringUtils.join(newCsvHeaders, fieldSeparator);
		rows.append(csvInputHeaders);
		rows.append(System.lineSeparator());

		for (int x = 0; x < csvData.getCsvBody().size(); ++x)
		{
			final String unpsc = csvData.getCsvBody().get(x).get(attributePosition).toString();
			LinkedList<String> newCsvBody = null;
			if (unpsc != null && !unpsc.isEmpty())
			{
				newCsvBody = new LinkedList<String>();
				newCsvBody.add(unpsc);
			}
			final String row = StringUtils.join(newCsvBody, fieldSeparator);
			final LinkedList<String> newCsvHeader = new LinkedList<String>(newCsvHeaders);
			newCsvHeader.pollFirst();
			final String appendAttributes = StringUtils.join(newCsvHeader, fieldSeparator);
			csvBody = createBody(createRow(row, appendAttributes));
			for (int body = 0; body < csvBody.length; ++body)
			{
				final String rowString = StringUtils.join(csvBody[body], fieldSeparator);
				rows.append(rowString);
				rows.append(System.lineSeparator());
			}
		}
		return rows;
	}

	private StringBuilder writeStringToCSVFile02(final CsvData csvData)
	{
		newCsvHeaders = new LinkedList<String>(csvData.getCsvHeaders());
		final StringBuilder rows = new StringBuilder(1024);
		final String csvInputHeaders = StringUtils.join(newCsvHeaders, fieldSeparator);
		rows.append(csvInputHeaders);
		rows.append(System.lineSeparator());
		return rows;
	}

	/*
	 *
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
				classBody.add(oldMaterialNumber);
				classBody.add(newMaterialNumber);
				final String className = fromMapStrings.get(2);
				final String newMaterialNumbers = StringUtils.join(classBody, fieldSeparator);
				newCsvBody.add(newMaterialNumbers);
				newCsvBody.add(unspscCode);
				newCsvBody.add(className);
				multimap.remove(unspscCode);
				multimap.put(unspscCode, newCsvBody);//override that map
			}
			csvData.getCsvBody().clear();
			csvData.getCsvBody().addAll(multimap.values());
			for (int csv = 0; csv < csvData.getCsvBody().size(); ++csv)//loop through the list value on csv data.
			{
				rows.append(csvData.getCsvBody().get(csv));
				rows.append(System.lineSeparator());
			}
		}
		return rows;
	}

	private StringBuilder writeStringToCSVFile01(final CsvData csvData)
	{
		newCsvHeaders = new LinkedList<String>(csvData.getCsvHeaders());
		final int attributePosition = getAttributeIndexValue();//721
		final StringBuilder rows = new StringBuilder(1024);
		final String csvInputHeaders = StringUtils.join(newCsvHeaders, fieldSeparator);
		rows.append(csvInputHeaders);
		rows.append(System.lineSeparator());

		final LinkedList<String> headerPart1 = new LinkedList<String>(newCsvHeaders.subList(0, 720));
		final LinkedList<String> headerPart2 = new LinkedList<String>(getUnspcValues(csvData, attributePosition));//721
		final LinkedList<String> headerPart3 = new LinkedList<String>(newCsvHeaders.subList(721, getLastIndex() + 1));
		final String headerPart1String = StringUtils.join(headerPart1, fieldSeparator);
		final String headerPart3String = StringUtils.join(headerPart3, fieldSeparator);

		for (int x = 0; x < headerPart2.size(); ++x)//loop through the Unspc value on csv data.
		{
			rows.append(headerPart1String);
			rows.append(fieldSeparator);
			rows.append(StringUtils.join(Arrays.asList(headerPart2.get(x)), fieldSeparator));
			rows.append(fieldSeparator);
			rows.append(headerPart3String);
			rows.append(System.lineSeparator());
		}
		return rows;
	}

	private LinkedList<String> getUnspcValues(final CsvData csvData, final int attributePosition)
	{
		final LinkedList<String> newCsvBody = new LinkedList<String>();
		for (int x = 0; x < csvData.getCsvBody().size(); ++x)
		{
			final String unpsc = csvData.getCsvBody().get(x).get(attributePosition);
			if (unpsc != null && !unpsc.isEmpty() && !unpsc.startsWith("#"))
			{
				newCsvBody.add(unpsc.trim());
			}
		}
		return newCsvBody;
	}

	/**
	 * @return the headerLastIndex
	 */
	public String getHeaderLastIndex()
	{
		return headerLastIndex;
	}

	/**
	 * @param headerLastIndex
	 *           the headerLastIndex to set
	 */
	@Required
	public void setHeaderLastIndex(final String headerLastIndex)
	{
		this.headerLastIndex = headerLastIndex;
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

	/**
	 * @return the attributecode
	 */
	public String getAttributecode()
	{
		return attributecode;
	}

	/**
	 * @param attributecode
	 *           the attributecode to set
	 */
	@Required
	public void setAttributecode(final String attributecode)
	{
		this.attributecode = attributecode;
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

	private String[] createHeaders(final String... headers)
	{
		return headers;
	}

	private String[] createRow(final String... rowValues)
	{
		return rowValues;
	}

	private String[][] createBody(final String[]... rows)
	{
		return rows;
	}
}
