/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.platform.mediaperspective.session.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Toolbarbutton;

import com.hybris.platform.mediaperspective.media.impl.MimeTypeGroup;
import com.hybris.platform.mediaperspective.search.MediaPerspectiveQueryProvider;
import com.hybris.platform.mediaperspective.session.PerspectiveToolbarAddition;
import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.BulkMediaUploadWizard;
import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.DefaultBulkMediaUploadViewModel;
import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.events.BulkMediaUploadModelListener;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultSearchContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.SearchToolbarBrowserComponent;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.generic.NewItemWizard;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;


/**
 */
public class MediaPerspectiveSearchBrowserModel extends DefaultSearchBrowserModel
{
	private static final Logger LOG = Logger.getLogger(MediaPerspectiveSearchBrowserModel.class);
	private transient CatalogVersionModel selectedCatalogVersion;
	private transient CatalogModel selectedCatalog;
	private List<PerspectiveToolbarAddition> toolbarAdditions;
	private boolean defaultFiltersApplied;
	
	public MediaPerspectiveSearchBrowserModel()
	{
		super(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Media"));
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		final MediaPerspectiveSearchBrowserModel dis = (MediaPerspectiveSearchBrowserModel) super.clone();
		dis.setSelectedCatalogVersion(selectedCatalogVersion);
		dis.setSelectedCatalog(selectedCatalog);
		return dis;
	}

	protected CatalogVersionModel getSelectedCatalogVersion()
	{
		if (selectedCatalogVersion != null)
		{
			return this.selectedCatalogVersion;
		}
		final CatalogVersionModel ret = (CatalogVersionModel) getLastQuery().getContextParameter("selectedCatalogVersions");
		return ret;
	}

	public void setSelectedCatalogVersion(final CatalogVersionModel selectedCatalog)
	{
		this.selectedCatalogVersion = selectedCatalog;
	}

	@Override
	protected SearchProvider getSearchProvider()
	{
		if (searchProvider == null)
		{
			searchProvider = (MediaPerspectiveQueryProvider) SpringUtil.getBean("mediaSearchProvider");
		}
		return searchProvider;
	}

	@Override
	public BrowserFilter getBrowserFilter()
	{
		return null;
	}


	@Override
	public AbstractContentBrowser createViewComponent()
	{
		return new DefaultSearchContentBrowser()
		{
			@Override
			protected AbstractBrowserComponent createToolbarComponent()
			{
				return new SearchToolbarBrowserComponent(getModel(), this)
				{
					@Override
					protected HtmlBasedComponent createToolbar()
					{
						final HtmlBasedComponent toolbar = super.createToolbar();
						//toolbar.setSclass("cms_catalog_browser_toolbar");
						return toolbar;
					}

					@Override
					protected Hbox createRightToolbarHbox()
					{
						final Hbox ret = super.createRightToolbarHbox();

						final Div btnContainer = new Div();
						btnContainer.setSclass("new_btn_container");
						
						addAdditionalFilterContent(ret);
						addAdditionalToolbarButtons(ret);

						final Toolbarbutton addElementButton = new Toolbarbutton("", "/cmscockpit/images/add_btn.gif");
						addElementButton.setTooltiptext(Labels.getLabel("showcasecmscockpit.create_assets"));
						UITools.addBusyListener(addElementButton, Events.ON_CLICK, new EventListener()
						{

							@Override
							public void onEvent(final Event event) throws Exception //NOPMD: ZK specific
							{
								final NewItemWizard itemWizard = new NewItemWizard(UISessionUtils.getCurrentSession().getTypeService()
										.getObjectTemplate("Media"), getRoot(), null);

								final Map<String, Object> contextValues = new HashMap<String, Object>();
								
								if (getSelectedCatalogVersion() != null)
								{
									contextValues.put(MediaModel._TYPECODE + "." + MediaModel.CATALOGVERSION, (UISessionUtils.getCurrentSession().getTypeService().wrapItem( getSelectedCatalogVersion())));
									
								}
								contextValues.put(MediaModel._TYPECODE + "." + MediaModel.FOLDER, (UISessionUtils.getCurrentSession().getTypeService().wrapItem( findMediaFolder())));
								contextValues.put(MediaModel._TYPECODE + "." + MediaModel.CODE, getGenericRandomNameProducer().generateSequence("Media", "media"));
								
								final Map<String,Object> params = new HashMap<String,Object>();
								params.put("forceCreateInWizard", Boolean.TRUE);
								
								itemWizard.setParameters(params);
								itemWizard.setCreateContext(new CreateContext(UISessionUtils.getCurrentSession().getTypeService()
										.getObjectTemplate("Media").getBaseType(), null, null, null));
								itemWizard.setAllowCreate(UISessionUtils.getCurrentSession().getSystemService()
										.checkPermissionOn("Media", AccessManager.CREATE));
								itemWizard.setAllowSelect(false);
								itemWizard.setDisplaySubTypes(false);
								itemWizard.setActiaveAfterCreate(true);
								itemWizard.setPredefinedValues(contextValues);

								itemWizard.start();
							}
						}, null, "general.updating.busy");
						btnContainer.appendChild(addElementButton);
						ret.appendChild(btnContainer);
						return ret;
					}

				};
			}
		};
	}
	
	private void addAdditionalFilterContent(final Hbox ret)
	{
		final List<PerspectiveToolbarAddition> toolbarAdditions = getToolbarAdditions();
		final Component first = ret.getFirstChild();

		UISessionUtils.getCurrentSession().getCurrentPerspective();

		if (toolbarAdditions != null)
		{
			for (final PerspectiveToolbarAddition toolbarAddition : toolbarAdditions)
			{
				final Component newChild = toolbarAddition.getContent(this);
				if (first != null)
				{
					ret.insertBefore(newChild, first);
				}
				else
				{
					ret.appendChild(newChild);
				}
			}
		}
	}
	
	private void addAdditionalToolbarButtons(Hbox ret) {
		final Toolbarbutton createButton = new Toolbarbutton("", "/cmscockpit/images/upload_zip.png");
		createButton.setTooltiptext(Labels.getLabel("mediaperspective.media.upload.bulk.wizard.label", "Upload a zip containing your media"));
		UITools.addBusyListener(createButton, Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD: ZK specific
			{
				SearchResult<ConversionGroupModel> conversionGroupsSearch = getFlexibleSearchService().search("SELECT {PK} FROM {" + ConversionGroupModel._TYPECODE + "}");
				SearchResult<ConversionMediaFormatModel> mediaFormatsSearch = getFlexibleSearchService().search("SELECT {PK} FROM {" + ConversionMediaFormatModel._TYPECODE + "}");
				DefaultBulkMediaUploadViewModel viewModel = new DefaultBulkMediaUploadViewModel(
						getSelectedCatalogVersion(), conversionGroupsSearch.getResult(), mediaFormatsSearch.getResult());
				viewModel.registerListener(getBulkMediaUploadListener());
				new BulkMediaUploadWizard(viewModel).show();
			}
			
		}, null, "general.updating.busy");
		final Component first = ret.getFirstChild();
		if (first != null)
		{
			ret.insertBefore(createButton, first);
		}
		else
		{
			ret.appendChild(createButton);
		}
	}

	private BulkMediaUploadModelListener getBulkMediaUploadListener() {
		return new BulkMediaUploadModelListener() {
			
			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChange() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSave(List<MediaModel> mediaModels) {
				updateItems(0);
			}
		};
	}
	
	private List<PerspectiveToolbarAddition> getToolbarAdditions() {
		if (toolbarAdditions == null)
		{
			toolbarAdditions = (List<PerspectiveToolbarAddition>) SpringUtil
					.getBean("mediaPerspectiveToolbarRightAdditions");
		}
		return toolbarAdditions;
	}
	
	@Override
	protected ExtendedSearchResult doSearchInternal(Query query) {
	    if (query == null)
	    {
	      throw new IllegalArgumentException("Query can not be null.");
	    }

	    ExtendedSearchResult result = null;

	    SearchProvider searchProvider = getSearchProvider();
	    if (searchProvider != null)
	    {
	      Query searchQuery = null;

	      int pageSize = (query.getCount() > 0) ? query.getCount() : getPageSize();

	      SearchType selectedType = null;
	      if (query.getSelectedTypes().size() == 1)
	      {
	        selectedType = (SearchType)query.getSelectedTypes().iterator().next();
	      }
	      else if (!(query.getSelectedTypes().isEmpty()))
	      {
	        selectedType = (SearchType)query.getSelectedTypes().iterator().next();
	        LOG.warn("Query has ambigious search types. Using '" + selectedType.getCode() + "' for searching.");
	      }

	      if (selectedType == null)
	      {
	        selectedType = getSearchType();
	      }

	      searchQuery = new Query(Collections.singletonList(selectedType), query.getSimpleText(), query.getStart(), pageSize);
	      searchQuery.setNeedTotalCount(!(isSimplePaging()));
	      searchQuery.setParameterValues(query.getParameterValues());
	      searchQuery.setParameterOrValues(query.getParameterOrValues());

	      ObjectTemplate selTemplate = (ObjectTemplate)query.getContextParameter("objectTemplate");
	      if (selTemplate != null)
	      {
	        searchQuery.setContextParameter("objectTemplate", selTemplate);

	      }

	      Map sortCriterion = getSortCriterion(query);

	      PropertyDescriptor sortProp = null;
	      boolean asc = false;

	      if ((sortCriterion != null) && (!(sortCriterion.isEmpty())))
	      {
	        sortProp = (PropertyDescriptor)sortCriterion.keySet().iterator().next();
	        if (sortProp == null)
	        {
	          LOG.warn("Could not add sort criterion (Reason: Specified sort property is null).");
	        }
	        else
	        {
	          if (sortCriterion.get(sortProp) != null)
	          {
	            asc = ((Boolean)sortCriterion.get(sortProp)).booleanValue();
	          }
	          searchQuery.addSortCriterion(sortProp, asc);
	        }

	      }

	      updateAdvancedSearchModel(searchQuery, sortProp, asc);

	      try
	      {
	        Query clonedQuery = (Query)searchQuery.clone();
	        setLastQuery(clonedQuery);
	      }
	      catch (CloneNotSupportedException localCloneNotSupportedException)
	      {
	        LOG.error("Cloning the query is not supported");

	      }
	      searchQuery.setContextParameter("selectedCatalogVersion",
					getSelectedCatalogVersion() != null ? getSelectedCatalogVersion() : null);

	      if (getBrowserFilter() != null)
	      {
	        getBrowserFilter().filterQuery(searchQuery);
	      }
	      
	      if (!isDefaultFiltersApplied()) {
	    	  applyDefaultFilters(searchQuery);
	    	  defaultFiltersApplied = true;
	      }
	      
	      final List<PerspectiveToolbarAddition> toolbarRightAdditions = getToolbarAdditions();
			if (toolbarRightAdditions != null)
			{
				for (final PerspectiveToolbarAddition roolbarRightAddition : toolbarRightAdditions)
				{
					final BrowserFilter filter = roolbarRightAddition.getSelectedFilter();
					if (filter != null)
					{
						filter.filterQuery(searchQuery);
					}
				}
			}

	      result = searchProvider.search(searchQuery);
	      updateLabels();
	    }

	    return result;
	}

	private void applyDefaultFilters(Query searchQuery) {
		MimeTypeGroup mimeTypeGroup = (MimeTypeGroup) SpringUtil.getBean("defaultMimeTypeGroup");
		BrowserFilter browserFilter = new MimeTypeGroupBrowserFilterImpl(mimeTypeGroup.getIdentifier(), mimeTypeGroup.getMimeTypes());
		browserFilter.filterQuery(searchQuery);
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return (CMSAdminSiteService) SpringUtil.getBean("cmsAdminSiteService");
	}

	public CatalogModel getSelectedCatalog()
	{
		return selectedCatalog;
	}

	public void setSelectedCatalog(final CatalogModel selectedCatalog)
	{
		this.selectedCatalog = selectedCatalog;
	}

	public boolean isDefaultFiltersApplied() {
		return defaultFiltersApplied;
	}
	
	private FlexibleSearchService getFlexibleSearchService() {
		return (FlexibleSearchService) SpringUtil.getBean("flexibleSearchService");
	}
	

	protected MediaFolderModel findMediaFolder()
	{
		return getMediaService().getFolder("images");
	}

	protected MediaService getMediaService()
	{
		return (MediaService) SpringUtil.getBean("mediaService");
	}
	
	protected GenericRandomNameProducer getGenericRandomNameProducer()
	{
		return (GenericRandomNameProducer) SpringUtil.getBean("genericRandomNameProducer");
	}
}
