/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.util.List;
import java.util.Set;

import com.hybris.mediatags.model.MediaTagModel;
import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.events.BulkMediaUploadModelListener;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;

/**
 * @author mkostic
 *
 */
public interface BulkMediaUploadViewModel {

	void catalogVersionChanged(CatalogVersionModel catalogVersionModel);
	
	void fireInitEvents();

	Set<MediaTagModel> getExistingMediaTags();
	
	CatalogVersionModel getSelectedCatalogVersionModel();

	boolean isValid();

	void mediaBulkUploaded(UploadedMediaBulk mediaBulk);

	void mediaFormatChanged(MediaFormatModel mediaFormatModel);

	void mediaTagAdded(MediaTagModel mediaTagModel);
	
	void registerListener(BulkMediaUploadModelListener listener);

	boolean save();

	List<ConversionGroupModel> getExistingConversionGroupModels();
	
	List<ConversionMediaFormatModel> getExistingConversionMediaFormats();
	
	void removeConversionFormatToSelection(ConversionMediaFormatModel conversionFormat);

	void addConversionFormatToSelection(ConversionMediaFormatModel conversionFormat);

	void mediaContainerChanged(MediaContainerModel mediaContainer);

	MediaFormatModel getSelectedMediaFormat();

	MediaContainerModel getSelectedMediaContainer();

	MediaModel getSelectedMedia();
	
	void resizedMediaContainerCodeChanged(String containerCode);

}
