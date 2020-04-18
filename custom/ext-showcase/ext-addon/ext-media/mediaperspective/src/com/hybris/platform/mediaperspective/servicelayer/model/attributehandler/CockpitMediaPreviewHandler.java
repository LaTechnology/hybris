/**
 * 
 */
package com.hybris.platform.mediaperspective.servicelayer.model.attributehandler;

import com.hybris.platform.mediaperspective.media.MediaHelper;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

/**
 * @author mkostic
 *
 */
public class CockpitMediaPreviewHandler implements DynamicAttributeHandler<String, MediaModel> {
	
	private MediaHelper mediaHelper;
	
	@Override
	public String get(MediaModel media) {
		return mediaHelper.getPreviewImageURLForMedia(media);
	}

	@Override
	public void set(MediaModel media, String previewURL) {
		throw new UnsupportedOperationException("This dynamic attribute is not writable.");
	}

	public void setMimeTypeGrouper(MediaHelper mediaHelper) {
		this.mediaHelper = mediaHelper;
	}

}
