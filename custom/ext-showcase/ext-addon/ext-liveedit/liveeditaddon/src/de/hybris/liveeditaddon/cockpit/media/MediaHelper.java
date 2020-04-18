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
package de.hybris.liveeditaddon.cockpit.media;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;


/**
 * 
 */
public class MediaHelper
{
	public static byte[] extractMediaBytesFromMedia(final Logger LOG, final org.zkoss.util.media.Media uploadedMedia)
	{
		byte[] ret = null;
		//early exit
		if (uploadedMedia == null)
		{
			return ret;
		}
		InputStream anInputStream = null;
		Reader reader = null;

		try
		{
			if (uploadedMedia.isBinary())
			{
				anInputStream = uploadedMedia.getStreamData();
				if (anInputStream != null)
				{
					ret = IOUtils.toByteArray(anInputStream);
				}
			}
			else
			{
				reader = uploadedMedia.getReaderData();
				if (reader != null)
				{
					ret = IOUtils.toByteArray(reader);
				}
			}
		}
		catch (final IOException e)
		{
			LOG.warn("An error ocured while extracting byte stream from " + org.zkoss.util.media.Media.class.getName() + " object!");
		}
		finally
		{
			try
			{
				if (anInputStream != null)
				{
					anInputStream.close();
				}
				if (reader != null)
				{
					reader.close();
				}
			}
			catch (final IOException e)
			{
				LOG.warn("An error ocured while extracting byte stream from " + org.zkoss.util.media.Media.class.getName()
						+ " object!");
			}
		}
		return ret;
	}

	public static List<byte[]> extractBytesFromZipInputStream(final Logger LOG, final Media uploadedMedia)
	{
		final List<byte[]> result = new ArrayList<byte[]>();
		//Make sure uploaded media is not NULL and is in ZIP format.
		if (uploadedMedia == null)
		{
			return result;
		}
		if (!uploadedMedia.getFormat().equals("zip"))
		{
			LOG.warn("Given media is not in ZIP format, returning empty list.");
			return result;
		}
		final ZipInputStream zipInputStream = new ZipInputStream(uploadedMedia.getStreamData());
		try
		{
			// This positions the stream at the beginning of an entry.
			while (zipInputStream.getNextEntry() != null)
			{
				final ByteArrayOutputStream output = new ByteArrayOutputStream();
				IOUtils.copy(zipInputStream, output);
				result.add(output.toByteArray());
				IOUtils.closeQuietly(output);
			}
		}
		catch (final Exception e)
		{
			LOG.warn("An error ocured while extracting byte stream from ZIP archive!");
		}
		finally
		{
			IOUtils.closeQuietly(zipInputStream);
		}
		return result;
	}
}
