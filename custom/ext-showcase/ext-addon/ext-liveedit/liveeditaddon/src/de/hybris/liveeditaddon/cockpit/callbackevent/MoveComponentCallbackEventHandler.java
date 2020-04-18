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

import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 
 */
public class MoveComponentCallbackEventHandler extends AbstractLockingLiveEditCallbackEventHandler<LiveEditView>
{

	@Resource(name = "typeService")
	private TypeService typeService;

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
		return "moveComponent";
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

		final String pageUid = (String) attributeMap.get("page_id");
		final String fromContentSlotId = (String) attributeMap.get("slot_id");
		final String toContentSlotId = (String) attributeMap.get("new_slot_id");
		// in case we don't have a content slot already created for the destination slot then we need the position to create a new one
		final String toPosition = (String) attributeMap.get("new_slot_position");
		final String componentId = (String) attributeMap.get("cmp_id");
		final String index = String.valueOf(attributeMap.get("cmp_index"));

        /*
         * the way parameters are passed, only the from 'fromContentSlotId' is taken into account by AbstractLockingLiveEditCallbackEventHandler.onLockCallbackEvent()
         * One must then explicitly enforce control on the destination slot 'toContentSlotId'
         */
        boolean isDestinationSectionLocked = cmsLockingService.isSectionLocked(view, toContentSlotId);

        if (!isDestinationSectionLocked) {
            moveComponent(view, pageUid, fromContentSlotId, toContentSlotId, toPosition, componentId, index);
        }else{
            view.update();
        }
	}


	protected void moveComponent(final LiveEditView view, final String pageUid, final String fromSlotUid, final String toSlotUid,
			final String toPosition, final String componentUid, final String index) throws Exception {
		final AbstractCMSComponentModel component = getComponentForUid(componentUid, view);

        // only need to move within the same slot
        if (fromSlotUid.equals(toSlotUid)) {
            final ContentSlotModel contentSlot = getContentSlotForPreviewCatalogVersions(fromSlotUid, view);
            final List<AbstractCMSComponentModel> newOrder = new ArrayList(contentSlot.getCmsComponents());
            // remove our component from the list
            newOrder.remove(component);
            // add it again at the correct place
            newOrder.add(Integer.parseInt(index), component);
            contentSlot.setCmsComponents(newOrder);
            getModelService().save(contentSlot);
        } else
        // different slots
        {


            final AbstractPageModel page = getPageForPreviewCatalogVersions(pageUid, view);
            final ComposedTypeModel type = getTypeService().getComposedTypeForCode(component.getItemtype());

            //
            // Check that we're allowed to drop the component here
            //
            if (!isValidForContentSlot(type, toPosition, page)) {
                try {
                    // TODO add label if a properties file somewhere for localisation
                    final String message = "The component type " + (type.getName() == null ? type.getCode() : type.getName())
                            + " is not allowed in this slot";
                    Messagebox.show(message, Labels.getLabel("general.warning"), Messagebox.OK, Messagebox.EXCLAMATION);
                    view.update();
                    return;
                } catch (final InterruptedException e) {
                    // normalish behaviour
                }
            } else
                // no existing content slot so we need to create a new one
                if (StringUtils.isEmpty(toSlotUid)) {
                    // create the content slot
                    final ContentSlotModel contentSlotModel = getModelService().create(Cms2Constants.TC.CONTENTSLOT);
                    contentSlotModel.setCurrentPosition(toPosition);
                    contentSlotModel.setUid(getGenericRandomNameProducer().generateSequence(Cms2Constants.TC.CONTENTSLOT, "cs"));
                    contentSlotModel.setName(toPosition);
                    contentSlotModel.setCatalogVersion(component.getCatalogVersion());
                    contentSlotModel.setCmsComponents(Collections.singletonList(component));


                    // create the ContentSlotForPage
                    final ContentSlotForPageModel relation = new ContentSlotForPageModel();
                    relation.setCatalogVersion(contentSlotModel.getCatalogVersion());
                    relation.setContentSlot(contentSlotModel);
                    relation.setUid(getGenericRandomNameProducer().generateSequence(Cms2Constants.TC.CONTENTSLOT));
                    relation.setPage(page);
                    relation.setPosition(toPosition);
                    getModelService().save(contentSlotModel);
                    getModelService().save(relation);

                } else
                // have a content slot so add the component at the correct position
                {
                    final ContentSlotModel toContentSlot = getContentSlotForPreviewCatalogVersions(toSlotUid, view);
                    final List<AbstractCMSComponentModel> toOrder = new ArrayList(toContentSlot.getCmsComponents());
                    toOrder.add(Integer.parseInt(index), component);
                    toContentSlot.setCmsComponents(toOrder);
                    getModelService().save(toContentSlot);
                }

            // finally remove the component from the other slot
            final ContentSlotModel fromContentSlot = getContentSlotForPreviewCatalogVersions(fromSlotUid, view);
            final List<AbstractCMSComponentModel> fromOrder = new ArrayList(fromContentSlot.getCmsComponents());
            fromOrder.remove(component);
            fromContentSlot.setCmsComponents(fromOrder);
            getModelService().save(fromContentSlot);


        }

		view.update();
	}


	public TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
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
