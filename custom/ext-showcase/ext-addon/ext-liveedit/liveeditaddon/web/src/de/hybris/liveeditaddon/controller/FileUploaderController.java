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
package de.hybris.liveeditaddon.controller;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * File upload controller for the HTML5 FileReader (FileDrop).
 * 
 * 
 */
@Controller
@RequestMapping(value = "/**/mediaupload.htm")
public class FileUploaderController
{
	private static final Logger LOG = Logger.getLogger(FileUploaderController.class);

	@Autowired
	private MediaService mediaService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private CatalogVersionService catalogVersionService;

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	ResponseData handleFormUpload(final HttpServletRequest request, final HttpServletResponse response) throws IOException,
			ServletException
	{

		String fileName = null;
		try
		{
			final List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			for (final FileItem item : items)
			{
				if (item.isFormField())
				{
					throw new IllegalArgumentException("Not expecting a form field");

				}
				else
				{
					final String catalogVersionId = request.getParameter("catalogVersionId");
					final String catalogId = request.getParameter("catalogId");

					final boolean versionMedia = catalogVersionId != null && catalogId != null;


					LOG.info(request.getQueryString());

					fileName = item.getName();
					final InputStream content = item.getInputStream();
					boolean exists = true;
					try
					{
						if (versionMedia)
						{
							catalogVersionService.setSessionCatalogVersion(catalogId, catalogVersionId);
							mediaService.getMedia(catalogVersionService.getCatalogVersion(catalogId, catalogVersionId), fileName);
						}
						else
						{
							mediaService.getMedia(fileName);
						}
					}
					catch (final UnknownIdentifierException e)
					{
						exists = false;
					}
					catch (final AmbiguousIdentifierException e)
					{
						exists = true;
					}

					if (exists)
					{
						// we need a new file name
						fileName = fileName + System.currentTimeMillis() + "-";
					}

					final MediaModel media = (MediaModel) (versionMedia ? modelService.create(MediaModel.class) : modelService
							.create(CatalogUnawareMediaModel.class));
					media.setCode(fileName);

					if (versionMedia)
					{
						media.setCatalogVersion(catalogVersionService.getCatalogVersion(catalogId, catalogVersionId));
					}
					modelService.save(media);
					mediaService.setStreamForMedia(media, content);
					modelService.refresh(media);
				}

			}

		}
		catch (final FileUploadException e)
		{

			LOG.error("Parsing file upload failed:", e);
			final ResponseData responseData = new ResponseData();
			responseData.setFileName(fileName);
			responseData.setSuccess(false);
			responseData.setErrorMessage("Failed to upload file:" + e.getMessage());
			return responseData;

		}

		final ResponseData responseData = new ResponseData();
		responseData.setFileName(fileName);
		responseData.setSuccess(true);
		return responseData;

	}

	private static class ResponseData
	{
		private boolean success;
		private String errorMessage;
		private String fileName;

		/**
		 * @return the success
		 */
		public boolean isSuccess()
		{
			return success;
		}

		/**
		 * @param success
		 *           the success to set
		 */
		public void setSuccess(final boolean success)
		{
			this.success = success;
		}

		/**
		 * @return the errorMessage
		 */
		public String getErrorMessage()
		{
			return errorMessage;
		}

		/**
		 * @param errorMessage
		 *           the errorMessage to set
		 */
		public void setErrorMessage(final String errorMessage)
		{
			this.errorMessage = errorMessage;
		}

		/**
		 * @return the fileName
		 */
		public String getFileName()
		{
			return fileName;
		}

		/**
		 * @param fileName
		 *           the fileName to set
		 */
		public void setFileName(final String fileName)
		{
			this.fileName = fileName;
		}

	}
}
