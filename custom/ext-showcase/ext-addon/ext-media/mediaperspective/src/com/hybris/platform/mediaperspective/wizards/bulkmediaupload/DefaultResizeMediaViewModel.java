/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
public class DefaultResizeMediaViewModel extends AbstractEditMediaViewModel {

	private final static Logger LOG = Logger.getLogger(DefaultResizeMediaViewModel.class);

	public DefaultResizeMediaViewModel(
			MediaModel mediaModel,
			List<ConversionGroupModel> existingConversionGroupModels,
			List<ConversionMediaFormatModel> existingConversionMediaFormats) {
		super(mediaModel.getCatalogVersion(), existingConversionGroupModels, existingConversionMediaFormats);
		this.mediaModel = mediaModel;  
	}

	@Override
	public boolean isValid() {
		return !selectedConversionMediaFormats.isEmpty();
	}

	@Override
	public boolean save() {
		boolean result = false;
		List<MediaModel> mediaModels = new ArrayList<>();
		if (isValid()) {
			try {
				String mediaContainerCode = StringUtils.isEmpty(resizedMediaContainerCode) ? "TempContainerResize_" : resizedMediaContainerCode;
				MediaContainerModel tempContainer = addMediaToTempContainer(mediaModel, mediaContainerCode);
				tempContainer = addConvertedMediaToContainer(mediaModel, tempContainer, getConversionFormats());
				tagContainer(tempContainer, selectedMediaTagModels);
				mediaModels.addAll(tempContainer.getMedias());
				result = true;
			} catch (UnknownIdentifierException e) {
				LOG.warn("Could not add uploaded media to the 'images' folder, no such folder found. Adding uploaded medias to default folder...");
			} catch (AmbiguousIdentifierException e) {
				LOG.warn("Could not add uploaded media to the 'images' folder. Adding uploaded medias to default folder...");
			}
		}
		if (result) {
			fireSaveEvents(mediaModels);
		}
		return result;
	}

}
