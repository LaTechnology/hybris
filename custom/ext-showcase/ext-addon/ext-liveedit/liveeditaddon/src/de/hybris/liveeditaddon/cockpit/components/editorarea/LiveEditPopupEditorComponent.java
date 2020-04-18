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
package de.hybris.liveeditaddon.cockpit.components.editorarea;


import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelEvent;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.liveeditaddon.cockpit.session.impl.LiveeditaddonPerspective;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;


/**
 *
 */
public class LiveEditPopupEditorComponent extends Div
{


	public LiveEditPopupEditorComponent()
	{
		super();


		final UIEditorArea editorArea = ((LiveeditaddonPerspective) UISessionUtils.getCurrentSession().getCurrentPerspective())
				.getLiveEditPopupEditorArea();


		setWidth("100%");
		setAlign("right");
		setHeight("100%");




		final SectionPanel sectionpanel = new SectionPanel();
		sectionpanel.setParent(this);
		sectionpanel.setWidth("100%");
		sectionpanel.setHeight("100%");
		//sectionpanel.setAlign("left");
		sectionpanel.setFlatSectionLayout(true);
		sectionpanel.setRowLabelWidth("11em");
		sectionpanel.setLazyLoad(true);
		sectionpanel.setModel(editorArea.getEditorAreaController().getSectionPanelModel());
		sectionpanel.setSectionRenderer(editorArea.getEditorAreaController().getSectionRenderer());
		sectionpanel.setSectionRowRenderer(editorArea.getEditorAreaController().getSectionRowRenderer());
		sectionpanel.setSectionPanelLabelRenderer(editorArea.getEditorAreaController().getSectionPanelLabelRenderer());
		sectionpanel.addEventListener("onAllSectionsShow", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				editorArea.getEditorAreaController().showAllSections();
			}
		});
		sectionpanel.addEventListener("onRowShow", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				editorArea.getEditorAreaController().onRowShow((SectionPanelEvent) event);
			}
		});
		sectionpanel.addEventListener("onRowHide", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				editorArea.getEditorAreaController().onRowHide((SectionPanelEvent) event);
			}
		});
		sectionpanel.addEventListener("onRowMoved", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				editorArea.getEditorAreaController().onRowMoved((SectionPanelEvent) event);
			}
		});
		sectionpanel.addEventListener("onSectionHide", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				editorArea.getEditorAreaController().onSectionHide((SectionPanelEvent) event);
			}
		});
		sectionpanel.addEventListener("onSectionShow", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				editorArea.getEditorAreaController().onSectionShow((SectionPanelEvent) event);
			}
		});
		sectionpanel.addEventListener("onMessageClicked", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				editorArea.getEditorAreaController().onMessageClicked((SectionPanelEvent) event);
			}
		});
		sectionpanel.addEventListener("onLater", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				editorArea.getEditorAreaController().onLater(event);
			}
		});

		sectionpanel.afterCompose();
	}
}
