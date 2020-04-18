/**
 * 
 */
package com.hybris.platform.mediaperspective.media.impl;

import java.util.List;

/**
 * Use this class to group similar mime types together.
 * 
 * @author mkostic
 * 
 */
public class MimeTypeGroup {

	private String identifier;
	private List<String> mimeTypes;
	private String previewImageURL;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<String> getMimeTypes() {
		return mimeTypes;
	}

	public void setMimeTypes(List<String> mimeTypes) {
		this.mimeTypes = mimeTypes;
	}

	public String getPreviewImageURL() {
		return previewImageURL;
	}

	public void setPreviewImageURL(String previewImageURL) {
		this.previewImageURL = previewImageURL;
	}

}
