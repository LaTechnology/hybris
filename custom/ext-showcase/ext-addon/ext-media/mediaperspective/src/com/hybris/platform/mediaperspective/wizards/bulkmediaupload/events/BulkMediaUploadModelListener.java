/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload.events;

import java.util.List;

import de.hybris.platform.core.model.media.MediaModel;

/**
 * @author mkostic
 *
 */
public interface BulkMediaUploadModelListener {
	
	void onChange();

	void onLoad();
	
	void onSave(List<MediaModel> mediaModels);

}
