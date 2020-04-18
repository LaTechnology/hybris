/**
 * 
 */
package de.hybris.mediaconversionimageresizeplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.ImageResizePlugin;
import de.hybris.mediaconversionimageresizeplugin.service.ConversionGroupService;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * @author wojciech.piotrowiak
 * 
 */
public class DefaultMediaConversionImageResizePlugin implements ImageResizePlugin
{
	private final String QUALIFIER_PATTERN = "mediaConversion_";
	private final String QUALIFIER_DISABLED = "mediaConversionPlugin";

	private MediaConversionService mediaConversionService;
	private ModelService modelService;
	private MediaService mediaService;

	private ConversionGroupService conversionGroupService;

	@Override
	public MediaContainerModel convertMedia(final MediaContainerModel tempContainer)
	{
		return convert(tempContainer, getConversionGroupService().getDefaultConvertionGroup());
	}

	@Override
	public MediaContainerModel convertMediaForSite(final MediaContainerModel tempContainer, final String siteUid)
	{
		return convert(tempContainer, getConversionGroupService().getConvertionGroupForSite(siteUid));
	}

	private MediaContainerModel convert(final MediaContainerModel tempContainer, final ConversionGroupModel conversionGroupModel)
	{
		tempContainer.setConversionGroup(conversionGroupModel);
		return buildNewContainer(tempContainer);
	}

	private ConversionGroupModel getConversionGroupForSite(final String siteUid)
	{
		return getConversionGroupService().getConvertionGroupForSite(siteUid);
	}

	private MediaContainerModel buildNewContainer(final MediaContainerModel tempContainer)
	{
		final MediaContainerModel newContainer = getModelService().create(MediaContainerModel._TYPECODE);
		getMediaConversionService().convertMedias(tempContainer);
		newContainer.setMedias(getMediasWithAcceleratorFormats(tempContainer));
		newContainer.setCatalogVersion(tempContainer.getCatalogVersion());
		newContainer.setQualifier("base_" + tempContainer.getQualifier());
		getModelService().save(newContainer);
		return newContainer;
	}

	private Collection<MediaModel> getMediasWithAcceleratorFormats(final MediaContainerModel tempContainer)
	{
		final Collection<MediaModel> convertedMedias = getMediaConversionService().getConvertedMedias(tempContainer);
		final List<MediaModel> changedMedia = new ArrayList<MediaModel>();
		for (final MediaModel media : convertedMedias)
		{
			if (media.getMediaFormat().getQualifier().equals(QUALIFIER_DISABLED))//not sure if we need that format
			{
				continue;
			}
			final String accMediaFormatQualifier = media.getMediaFormat().getQualifier().replace(QUALIFIER_PATTERN, "");
			final MediaFormatModel accMediaFormat = getMediaService().getFormat(accMediaFormatQualifier);
			media.setMediaFormat(accMediaFormat);
			getModelService().save(media);
			changedMedia.add(media);
		}
		return changedMedia;
	}


	public MediaConversionService getMediaConversionService()
	{
		return mediaConversionService;
	}

	@Required
	public void setMediaConversionService(final MediaConversionService mediaConversionService)
	{
		this.mediaConversionService = mediaConversionService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	public MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(MediaService mediaService)
	{
		this.mediaService = mediaService;
	}


	public ConversionGroupService getConversionGroupService()
	{
		return conversionGroupService;
	}

	@Required
	public void setConversionGroupService(final ConversionGroupService conversionGroupService)
	{
		this.conversionGroupService = conversionGroupService;
	}
}
