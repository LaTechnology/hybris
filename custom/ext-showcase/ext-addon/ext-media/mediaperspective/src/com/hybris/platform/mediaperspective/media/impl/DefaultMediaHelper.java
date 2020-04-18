/**
 * 
 */
package com.hybris.platform.mediaperspective.media.impl;

import java.util.Map;
import java.util.Set;

import com.hybris.platform.mediaperspective.media.MediaHelper;

import de.hybris.platform.core.model.media.MediaModel;

/**
 * Default implementation of {@link MediaHelper}.
 * 
 * @author mkostic
 *
 */
public class DefaultMediaHelper implements MediaHelper {
	
	private String defaultPreviewURL;
	private Set<String> imageMimeTypes;
	private Map<String, MimeTypeGroup> mimeTypeGroups;

	/**
	 * Gets the URL of preview image based on given media's mime type. If the
	 * media is an image, then this method returns media's URL. Otherwise, this
	 * method returns one of the pre-configured URLs.
	 */
	@Override
	public String getPreviewImageURLForMedia(MediaModel mediaModel) {
		String ret = defaultPreviewURL;
		if (isImage(mediaModel)) {
			ret = mediaModel.getURL();
		} else if (mimeTypeGroups.containsKey(mediaModel.getMime())){
			ret = mimeTypeGroups.get(mediaModel.getMime()).getPreviewImageURL();
		}
		return ret;
	}

	private boolean isImage(MediaModel mediaModel) {
		return imageMimeTypes.contains(mediaModel.getMime());
	}

	public void setDefaultPreviewURL(String defaultPreviewURL) {
		this.defaultPreviewURL = defaultPreviewURL;
	}

	public void setImageMimeTypes(Set<String> imageMimeTypes) {
		this.imageMimeTypes = imageMimeTypes;
	}

	public void setMimeTypeGroups(Map<String, MimeTypeGroup> mimeTypeGroups) {
		this.mimeTypeGroups = mimeTypeGroups;
	}

}
