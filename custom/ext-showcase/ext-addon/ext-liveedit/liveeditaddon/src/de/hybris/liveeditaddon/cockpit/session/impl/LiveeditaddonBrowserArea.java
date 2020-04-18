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
package de.hybris.liveeditaddon.cockpit.session.impl;

import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserArea;
import de.hybris.liveeditaddon.constants.LiveeditaddonConstants;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import org.zkoss.zk.ui.Component;

/**
 * Created by fcanteloup on 25/09/14.
 * Liveeditaddon specific implementation of LiveEditBrowserArea that embarks necessary info to enable lock control
 */
public class LiveeditaddonBrowserArea extends LiveEditBrowserArea{


    public void openInspectorInDiv(final TypedObject item, final String divId, final LiveEditView view, final String slotId)
    {
        final Component infoArea = getInfoArea(divId);
        infoArea.setAttribute(LiveeditaddonConstants.SLOT_ID, slotId);
        infoArea.setAttribute(LiveeditaddonConstants.VIEW_ATTRIBUTE, view);
        superOpenInspectorInArea(item, infoArea);
    }

    //for mock purposes
    protected void superOpenInspectorInArea(TypedObject item, Component infoArea) {
        openInspectorInArea(item, infoArea);
    }

}
