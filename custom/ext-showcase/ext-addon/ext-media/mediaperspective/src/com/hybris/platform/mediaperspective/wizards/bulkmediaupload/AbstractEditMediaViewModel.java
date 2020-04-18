/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hybris.mediatags.model.MediaTagModel;
import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.events.BulkMediaUploadModelListener;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author mkostic
 * 
 */
public abstract class AbstractEditMediaViewModel implements BulkMediaUploadViewModel {

	private final static Logger LOG = Logger.getLogger(AbstractEditMediaViewModel.class);

	private List<BulkMediaUploadModelListener> listeners;
	private MediaService mediaService;
	protected Set<MediaTagModel> selectedMediaTagModels;
	private ModelService modelService;
	protected CatalogVersionModel selectedCatalogVersionModel;
	private MediaFormatModel selectedMediaFormatModel;
	private List<ConversionGroupModel> existingConversionGroupModels;
	private List<ConversionMediaFormatModel> existingConversionMediaFormats;
	protected Set<ConversionMediaFormatModel> selectedConversionMediaFormats;
	private MediaConversionService mediaConversionService;
	protected MediaContainerModel selectedMediaContainerModel;
	protected MediaModel mediaModel;
	protected List<UploadedMediaBulk> uploadedMediaBulks;
	protected String resizedMediaContainerCode;

	public AbstractEditMediaViewModel(
			CatalogVersionModel defaultCatalogVersionModel,
			List<ConversionGroupModel> existingConversionGroupModels,
			List<ConversionMediaFormatModel> existingConversionMediaFormats) {
		listeners = new ArrayList<>();
		selectedMediaTagModels = new LinkedHashSet<>();
		this.selectedCatalogVersionModel = defaultCatalogVersionModel;
		this.existingConversionGroupModels = existingConversionGroupModels;
		this.existingConversionMediaFormats = existingConversionMediaFormats;
		selectedConversionMediaFormats = new LinkedHashSet<>();
		uploadedMediaBulks = new ArrayList<>();
	}

	@Override
	public void catalogVersionChanged(CatalogVersionModel catalogVersionModel) {
		selectedCatalogVersionModel = catalogVersionModel;
		fireChangeEvents();
	}

	protected void fireChangeEvents() {
		for (BulkMediaUploadModelListener listener : listeners) {
			listener.onChange();
		}
	}

	@Override
	public void fireInitEvents() {
		for (BulkMediaUploadModelListener listener : listeners) {
			listener.onLoad();
		}
	}

	public void fireSaveEvents(List<MediaModel> mediaModels) {
		for (BulkMediaUploadModelListener listener : listeners) {
			listener.onSave(mediaModels);
		}
	}

	@Override
	public Set<MediaTagModel> getExistingMediaTags() {
		return selectedMediaTagModels;
	}

	protected MediaService getMediaService() {
		if (mediaService == null) {
			mediaService = (MediaService) Registry.getApplicationContext()
					.getBean("mediaService");
		}
		return mediaService;
	}

	protected ModelService getModelService() {
		if (modelService == null) {
			modelService = (ModelService) Registry.getApplicationContext()
					.getBean("modelService");
		}
		return modelService;
	}

	@Override
	public abstract boolean isValid();

	@Override
	public void mediaTagAdded(MediaTagModel mediaTagModel) {
		selectedMediaTagModels.add(mediaTagModel);
		fireChangeEvents();
	}

	@Override
	public void registerListener(BulkMediaUploadModelListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void mediaBulkUploaded(UploadedMediaBulk mediaBulk) {
		this.uploadedMediaBulks.add(mediaBulk);
		fireChangeEvents();
	}

	@Override
	public abstract boolean save();

	protected MediaContainerModel tagContainer(MediaContainerModel container, Set<MediaTagModel> mediaTagModels) {
		if (mediaTagModels != null && !mediaTagModels.isEmpty()) {
			Collection<MediaModel> medias = container.getMedias();
			for (MediaModel mediaModel : medias) {
				List<MediaTagModel> tags = new ArrayList<>(mediaModel.getMediaTags());
				tags.addAll(mediaTagModels);
				mediaModel.setMediaTags(tags);
			}
			getModelService().saveAll(medias);
			List<MediaTagModel> containerMediaTags = new ArrayList<>(container.getMediaTags());
			containerMediaTags.addAll(mediaTagModels);
			container.setMediaTags(containerMediaTags);
			getModelService().save(container);
		}
		return container;
	}

	protected MediaContainerModel moveMediaToSelectedContainer(MediaContainerModel sourceContainer, MediaContainerModel targetContainer) {
		List<MediaModel> mergedMediaModels = new ArrayList<>(targetContainer.getMedias());
		mergedMediaModels.addAll(sourceContainer.getMedias());
		targetContainer.setMedias(mergedMediaModels);
		getModelService().save(targetContainer);
		return targetContainer;
	}

	protected MediaContainerModel addConvertedMediaToContainer(
			final MediaModel uploadedMediaModel, MediaContainerModel mediaContainerModel, List<ConversionMediaFormatModel> conversionFormats) {
		List<MediaModel> convertedMediaModels = new ArrayList<>();
		for (ConversionMediaFormatModel formatModel : conversionFormats) {
			MediaModel mediaModel = getMediaConversionService().getOrConvert(mediaContainerModel, formatModel);
			convertedMediaModels.add(mediaModel);
		}
		List<MediaModel> uploadedMediaModels = new ArrayList<>();
		uploadedMediaModels.addAll(convertedMediaModels);
		mediaContainerModel.setMedias(uploadedMediaModels);
		getModelService().save(mediaContainerModel);
		return mediaContainerModel;
	}

	protected List<ConversionMediaFormatModel> getConversionFormats() {
		List<ConversionMediaFormatModel> conversionFormats = new ArrayList<>();
		conversionFormats.addAll(selectedConversionMediaFormats);
		return conversionFormats;
	}

	protected MediaContainerModel addMediaToTempContainer(final MediaModel uploadedMediaModel, String containerCode) {
		MediaContainerModel mediaContainerModel = getModelService().create(MediaContainerModel.class);
		mediaContainerModel.setCatalogVersion(getSelectedCatalogVersionModel());
		mediaContainerModel.setQualifier(containerCode + System.nanoTime() + uploadedMediaModel.getRealFileName());
		mediaContainerModel.setMedias(Collections.singletonList(uploadedMediaModel));
		getModelService().save(mediaContainerModel);
		return mediaContainerModel;
	}

	protected MediaModel saveMedia(UploadedMedia media, String realFilename,
			String fileName) {
		MediaFolderModel folder;
		final MediaModel uploadedMediaModel = getModelService().create("Media");
		uploadedMediaModel.setCode(fileName);
		folder = getMediaService().getFolder("images");
		uploadedMediaModel.setFolder(folder);
		uploadedMediaModel.setCatalogVersion(selectedCatalogVersionModel);
		uploadedMediaModel.setMediaFormat(selectedMediaFormatModel);
		uploadedMediaModel.setRealFileName(realFilename);
		// Model needs to be saved before we set the bytes.
		getModelService().save(uploadedMediaModel);
		getMediaService().setStreamForMedia(uploadedMediaModel,
				new DataInputStream(new ByteArrayInputStream(media.getBytes())));
		return uploadedMediaModel;
	}

	@Override
	public void mediaFormatChanged(MediaFormatModel mediaFormatModel) {
		selectedMediaFormatModel = mediaFormatModel;
		fireChangeEvents();
	}

	@Override
	public CatalogVersionModel getSelectedCatalogVersionModel() {
		return selectedCatalogVersionModel;
	}

	@Override
	public List<ConversionGroupModel> getExistingConversionGroupModels() {
		return existingConversionGroupModels;
	}

	@Override
	public List<ConversionMediaFormatModel> getExistingConversionMediaFormats() {
		return existingConversionMediaFormats;
	}

	protected MediaConversionService getMediaConversionService() {
		if (mediaConversionService == null) {
			mediaConversionService = (MediaConversionService) Registry.getApplicationContext()
					.getBean("mediaConversionService");
		}
		return mediaConversionService;
	}

	@Override
	public void removeConversionFormatToSelection(
			ConversionMediaFormatModel value) {
		selectedConversionMediaFormats.remove(value);
		fireChangeEvents();
	}

	@Override
	public void addConversionFormatToSelection(ConversionMediaFormatModel value) {
		selectedConversionMediaFormats.add(value);
		fireChangeEvents();
	}

	@Override
	public void mediaContainerChanged(MediaContainerModel mediaContainer) {
		this.selectedMediaContainerModel = mediaContainer;
	}

	@Override
	public MediaFormatModel getSelectedMediaFormat() {
		return selectedMediaFormatModel;
	}

	@Override
	public MediaContainerModel getSelectedMediaContainer() {
		return selectedMediaContainerModel;
	}

	@Override
	public MediaModel getSelectedMedia() {
		return mediaModel;
	}
	
	@Override
	public void resizedMediaContainerCodeChanged(String containerCode) {
		resizedMediaContainerCode = containerCode;
	}

}
