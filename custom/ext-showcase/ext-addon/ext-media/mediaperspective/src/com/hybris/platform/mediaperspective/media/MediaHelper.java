/**
 * 
 */
package com.hybris.platform.mediaperspective.media;


import de.hybris.platform.core.model.media.MediaModel;

/**
 * Helper for getting useful media info. 
 * 
 * @author mkostic
 *
 */
public interface MediaHelper {
	
	/**
	 * Gets the URL for preview image of the given media. This URL is meant to be used inside cockpit views. 
	 * 
	 * @param mediaModel
	 * @return URL of the image to be displayed.
	 */
	String getPreviewImageURLForMedia(MediaModel mediaModel);

}
