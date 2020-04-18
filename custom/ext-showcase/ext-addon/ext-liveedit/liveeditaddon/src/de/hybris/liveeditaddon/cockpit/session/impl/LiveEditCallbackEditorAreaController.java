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

import de.hybris.platform.cmscockpit.session.impl.CmsPopupEditorAreaControllerImpl;
import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelLabelRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.liveeditaddon.cockpit.components.sectionpanel.LiveEditCallbackEditorSectionPanelRenderer;


/**
 * 
 */
public class LiveEditCallbackEditorAreaController extends CmsPopupEditorAreaControllerImpl
{

	@Override
	protected SectionPanelLabelRenderer createSectionPanelLabelRenderer()
	{
		return new LiveEditCallbackEditorSectionPanelRenderer(this.getModel());
	}

	@Override
	public void updateLabel(final SectionPanelModel sectionPanelModel)
	{
		if (sectionPanelModel instanceof AbstractSectionPanelModel)
		{
			final TypedObject currentObject = getModel().getCurrentObject();
			if (currentObject != null)
			{
				final LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
				((AbstractSectionPanelModel) sectionPanelModel)
						.setLabel(labelService.getObjectTextLabelForTypedObject(currentObject));
				((LiveEditCallbackEditorSectionPanelRenderer) getSectionPanelLabelRenderer()).setCurrentObject(UISessionUtils
						.getCurrentSession().getTypeService().wrapItem(currentObject.getObject()));
			}

			((AbstractSectionPanelModel) sectionPanelModel).refreshInfoContainer();
		}
	}

	@Override
	public void updateEditorRequest(final TypedObject typedObject, final PropertyDescriptor descriptor)
	{
		super.updateEditorRequest(typedObject, descriptor);
		final TypedObject current = getModel().getCurrentObject();
		if (current != null && current.getObject() instanceof SolrFacetSearchConfigModel)
		{
			final SectionPanelModel mod = getSectionPanelModel();
			((AbstractSectionPanelModel) mod).refreshInfoContainer();
		}
	}
}
