/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.liveeditaddon.cockpit.callbackevent;

import de.hybris.platform.acceleratorcms.model.components.AbstractMediaContainerComponentModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;

import javax.annotation.Resource;

import de.hybris.platform.core.model.media.MediaModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Map;


/**
 * 
 */
public class CloneComponentCallbackEventHandler extends AbstractLockingLiveEditCallbackEventHandler<LiveEditView>
{

	protected static final String ABSTRACTCOMPONENT_UID_PREFIX = "comp";


	@Resource(name = "genericRandomNameProducer")
	private GenericRandomNameProducer genericRandomNameProducer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#getEventId()
	 */
	@Override
	public String getEventId()
	{
		return "cloneComponent";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cmscockpit.components.liveedit.CallbackEventHandler#onCallbackEvent(de.hybris.platform.cmscockpit
	 * .components.liveedit.LiveEditView, java.lang.String[])
	 */
	@Override
	public void onCallbackEventInternal(final LiveEditView view, final Map<String, Object> attributeMap) throws Exception
	{
		final String componentId = (String) attributeMap.get("cmp_id");
		final String contentSlotId = (String) attributeMap.get("slot_id");
		cloneComponent(view, contentSlotId, componentId);
	}

	protected void cloneComponent(final LiveEditView view, final String contentSlotId, final String componentId)
	{
		final AbstractCMSComponentModel component = getComponentForUid(componentId, view);
		final ContentSlotModel contentSlot = getContentSlotForPreviewCatalogVersions(contentSlotId, view);
		if (contentSlot != null)
		{
			final AbstractCMSComponentModel clonedComponent = getModelService().clone(component);
            clonedComponent.setUid(getGenericRandomNameProducer().generateSequence(AbstractCMSComponentModel._TYPECODE, ABSTRACTCOMPONENT_UID_PREFIX));
            if (AbstractMediaContainerComponentModel.class.isAssignableFrom(clonedComponent.getClass())){
                MediaContainerModel oldclonedMediaContainer = ((AbstractMediaContainerComponentModel)component).getMedia();
                MediaContainerModel newclonedMediaContainer = getModelService().clone(oldclonedMediaContainer);
                newclonedMediaContainer.setMedias(new ArrayList<MediaModel>());
                for (MediaModel media : oldclonedMediaContainer.getMedias()){
                    MediaModel cloneMedia = getModelService().clone(media);
                    cloneMedia.setCode(cloneMedia.getCode()+"_clone_"+Math.random());
                    newclonedMediaContainer.getMedias().add(cloneMedia);
                }
                newclonedMediaContainer.setQualifier(newclonedMediaContainer.getQualifier()+"_clone_"+Math.random());
                ((AbstractMediaContainerComponentModel)clonedComponent).setMedia(newclonedMediaContainer);
                getModelService().save(newclonedMediaContainer);
            }
			getModelService().save(clonedComponent);
		}
		view.update();
	}

	public GenericRandomNameProducer getGenericRandomNameProducer()
	{
		return genericRandomNameProducer;
	}

	@Required
	public void setGenericRandomNameProducer(final GenericRandomNameProducer genericRandomNameProducer)
	{
		this.genericRandomNameProducer = genericRandomNameProducer;
	}

}
