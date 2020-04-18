package com.hybris.platform.mediaperspective.components.listview.impl;

import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.BulkMediaUploadViewModel;
import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.DefaultResizeMediaViewModel;
import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.ResizeMediaWizard;
import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.events.BulkMediaUploadModelListener;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;


/**
 */
public class ResizeMediaAction extends AbstractListViewAction
{
	private static final String BUTTON_ICON = "cmscockpit/images/resize.png";
	private static final String BUTTON_ICON_DISABLED = "cockpit/images/icon_func_delete_unavailable.png";
	private FlexibleSearchService flexibleSearchService;
	
	@Override
	public String getImageURI(Context paramContext) {
		MediaModel media = (MediaModel)paramContext.getItem().getObject();
		if (media != null && media.getMediaContainer() == null)
		{
			return BUTTON_ICON;
		}
		return null;
	}

	@Override
	public EventListener getEventListener(final Context paramContext) {
		return new EventListener()
	    {
	      public void onEvent(Event event)
	        throws Exception
	      {
	    	  SearchResult<ConversionGroupModel> conversionGroupsSearch = flexibleSearchService.search("SELECT {PK} FROM {" + ConversionGroupModel._TYPECODE + "}");
	    	  SearchResult<ConversionMediaFormatModel> mediaFormatsSearch = flexibleSearchService.search("SELECT {PK} FROM {" + ConversionMediaFormatModel._TYPECODE + "}");
	    	  MediaModel media = (MediaModel)paramContext.getItem().getObject();
	    	  BulkMediaUploadViewModel viewModel = new DefaultResizeMediaViewModel(
	    			  media, conversionGroupsSearch.getResult(), mediaFormatsSearch.getResult());
	    	  viewModel.registerListener(getBulkMediaUploadListener());
	    	  new ResizeMediaWizard(viewModel).show();
	      }
	    };
	}
	

	private BulkMediaUploadModelListener getBulkMediaUploadListener() {
		return new BulkMediaUploadModelListener() {
			
			@Override
			public void onSave(List<MediaModel> mediaModels) {
				UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser().updateItems();
			}
			
			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChange() {
				// TODO Auto-generated method stub
				
			}
		};
	}

	@Override
	public Menupopup getPopup(Context paramContext) {
		return null;
	}

	@Override
	public Menupopup getContextPopup(Context paramContext) {
		return null;
	}

	@Override
	public String getTooltip(Context paramContext) {
		return Labels.getLabel("mediaperspective.actions.resize.tooltip");
	}

	@Override
	protected void doCreateContext(Context arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setFlexibleSearchService(
			FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

}
