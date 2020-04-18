/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.util.List;

/**
 * @author mkostic
 * 
 */
public class UploadedMediaBulk extends UploadedMedia {

	private List<UploadedMedia> medias;

	public UploadedMediaBulk(String name, byte[] bytes,
			List<UploadedMedia> medias) {
		super(name, bytes);
		this.medias = medias;
	}

	protected List<UploadedMedia> getMedias() {
		return medias;
	}

}
