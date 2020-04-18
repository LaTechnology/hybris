/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

/**
 * @author mkostic
 * 
 */
public class DefaultBulkMediaUploadViewModel extends AbstractEditMediaViewModel {

	private final static Logger LOG = Logger.getLogger(DefaultBulkMediaUploadViewModel.class);

	public DefaultBulkMediaUploadViewModel(
			CatalogVersionModel defaultCatalogVersionModel,
			List<ConversionGroupModel> existingConversionGroupModels,
			List<ConversionMediaFormatModel> existingConversionMediaFormats) {
		super(defaultCatalogVersionModel, existingConversionGroupModels, existingConversionMediaFormats);
	}

	@Override
	public boolean isValid() {
		return !uploadedMediaBulks.isEmpty()
				&& selectedCatalogVersionModel != null;
	}

	@Override
	public boolean save() {
		boolean result = false;
		List<MediaModel> mediaModels = new ArrayList<>();
		if (isValid()) {
			for (UploadedMediaBulk mediaBulk : uploadedMediaBulks) {
				for (UploadedMedia media : mediaBulk.getMedias()) {
					try {
						String realFilename = media.getName();
						String fileName = realFilename + "_" + System.nanoTime();
						final MediaModel uploadedMediaModel = saveMedia(media, realFilename, fileName);
						MediaContainerModel tempContainer = addMediaToTempContainer(uploadedMediaModel, "TempContainer_");
						tempContainer = addConvertedMediaToContainer(uploadedMediaModel, tempContainer, getConversionFormats());
						MediaContainerModel targetContainer = tempContainer;
						if (selectedMediaContainerModel != null) {
							targetContainer = moveMediaToSelectedContainer(tempContainer, selectedMediaContainerModel);
						}
						targetContainer = tagContainer(targetContainer, selectedMediaTagModels);
						mediaModels.addAll(targetContainer.getMedias());
						// If there is a selected media container, delete the temp container.
						if (selectedMediaContainerModel != null) {
							getModelService().remove(tempContainer);
						}
						result = true;
					} catch (UnknownIdentifierException e) {
						LOG.warn("Could not add uploaded media to the 'images' folder, no such folder found. Adding uploaded medias to default folder...");
					} catch (AmbiguousIdentifierException e) {
						LOG.warn("Could not add uploaded media to the 'images' folder. Adding uploaded medias to default folder...");
					}
				}
			}
		}
		if (result) {
			fireSaveEvents(mediaModels);
		}
		return result;
	}
}
