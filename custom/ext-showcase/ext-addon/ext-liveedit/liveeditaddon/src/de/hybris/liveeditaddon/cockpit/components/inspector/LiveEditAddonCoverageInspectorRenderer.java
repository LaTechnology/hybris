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
package de.hybris.liveeditaddon.cockpit.components.inspector;

import de.hybris.liveeditaddon.cockpit.service.CMSLockingService;
import de.hybris.liveeditaddon.constants.LiveeditaddonConstants;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cockpit.components.inspector.impl.DefaultCoverageInspectorRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.LayoutRegion;

import java.util.Iterator;

/**
 * Created by fcanteloup on 25/09/14.
 * Override of DefaultCoverageInspectorRenderer specific to Liveeditaddon that will enforce lock control on edit button
 */
public class LiveEditAddonCoverageInspectorRenderer extends DefaultCoverageInspectorRenderer {

    private CMSLockingService cmsLockingService;

    @Autowired
    public LiveEditAddonCoverageInspectorRenderer(@Qualifier("cmsLockingService") CMSLockingService cmsLockingService){
        this.cmsLockingService = cmsLockingService;
    }

    /**
     * The toobarDiv need to be passed the attributes of the infoArea component to enable action button to enforce locking control
     */
    @Override
    public void render(final Component parent, final TypedObject object) {
        superRenderer(parent, object);

        Component toolbarDiv = parent.getFirstChild().getLastChild();

        for (Object key : parent.getAttributes().keySet()) {
            toolbarDiv.setAttribute((String) key, parent.getAttributes().get((String) key));
        }
    }

    /**
     * this override will read view related properties to handle locked state
     */
    protected void prepareEditActionButton(final Component parent, final TypedObject item) {
        superPrepareEditActionButton(parent, item);

        final Component editBtnCnt = parent.getLastChild();
        Iterator iterator = editBtnCnt.getListenerIterator(Events.ON_CLICK);
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        editBtnCnt.addEventListener(Events.ON_CLICK, new EventListener() {
            @Override
            public void onEvent(final Event event) throws Exception // NOPMD
            {
                UICockpitPerspective currentPerspective = getCurrentPerspective();
                if (currentPerspective instanceof BaseUICockpitPerspective) {
                    //START OF ADDED SNIPPET WITH RESPECT TO DefaultCoverageInspectorRenderer
                    LiveEditView view = (LiveEditView) parent.getAttribute(LiveeditaddonConstants.VIEW_ATTRIBUTE);
                    String slotId = (String) parent.getAttribute(LiveeditaddonConstants.SLOT_ID);

                    if (!cmsLockingService.isSectionLocked(view, slotId)) {
                    //END OF ADDED SNIPPET WITH RESPECT TO DefaultCoverageInspectorRenderer
                        final LayoutRegion editorAreaComponent = ((BaseUICockpitPerspective) currentPerspective).getEditorAreaComponent();
                        if (!editorAreaComponent.isOpen() || !item.equals(currentPerspective.getActiveItem())) {
                            echoEvent(editBtnCnt);
                        }
                    }
                }
            }
        });
    }

    protected UICockpitPerspective getCurrentPerspective() {
        return UISessionUtils.getCurrentSession().getCurrentPerspective();
    }

    //all 3 follogn methods for test purposes until one has access to PowerMock
    protected void superRenderer(final Component parent, final TypedObject object){
        super.render(parent, object);
    }
    protected void superPrepareEditActionButton(final Component parent, final TypedObject object){
        super.prepareEditActionButton(parent, object);
    }
    protected void echoEvent(Component editBtnCnt) {
        Events.echoEvent("onCloseInspectorLater", editBtnCnt, null);
    }


}
