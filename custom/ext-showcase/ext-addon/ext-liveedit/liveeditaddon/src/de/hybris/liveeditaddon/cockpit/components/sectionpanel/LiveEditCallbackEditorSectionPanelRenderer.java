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
package de.hybris.liveeditaddon.cockpit.components.sectionpanel;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelLabelRenderer;
import de.hybris.platform.cockpit.constants.ImageUrls;
import de.hybris.platform.cockpit.model.gridview.impl.GridValueHolder;
import de.hybris.platform.cockpit.model.gridview.impl.GridView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.ListActionHelper;
import de.hybris.platform.cockpit.util.UITools;

import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;


/**
 * 
 */
public class LiveEditCallbackEditorSectionPanelRenderer implements SectionPanelLabelRenderer
{
	private static final String EDITOR_AREA_ACTIONS_DIV = "editorAreaLiveEditActionsDiv";
	private static final String EDITOR_AREA_STATUS_DIV = "editorAreaStatusDiv";

	private final UIEditorArea editorArea;
	private TypedObject currentObject;


	public LiveEditCallbackEditorSectionPanelRenderer(final UIEditorArea editorArea)
	{
		this.editorArea = editorArea;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.cockpit.components.sectionpanel.SectionPanelLabelRenderer#render(java.lang.String,
	 * java.lang.String, org.zkoss.zk.ui.Component)
	 */
	@Override
	public void render(final String label, final String imageUrl, final Component parent)
	{
		final String name = label;

		final Div box = new Div();
		UITools.maximize(box);

		final Div labelDiv = new Div();
		labelDiv.setSclass("labelContainerAction");
		labelDiv.appendChild(new Label(name));
		box.appendChild(labelDiv);

		final Div descriptionContainer = new Div();
		descriptionContainer.setSclass("descriptionContainer");
		descriptionContainer.appendChild(labelDiv);


		final String displayImgUrl;
		if (imageUrl != null)
		{
			displayImgUrl = imageUrl;
		}
		else
		{
			final GridViewConfiguration uiConfig = getUiConfiguration(getCurrentObject());
			final GridValueHolder holder = new GridValueHolder(uiConfig, getCurrentObject());
			if (holder.getImageURL() != null)
			{
				displayImgUrl = UITools.getAdjustedUrl(holder.getImageURL());
			}
			else
			{
				displayImgUrl = ImageUrls.STOP;
			}
		}
		final Div imgDiv = new Div();
		imgDiv.setSclass("section-label-image");
		final Image img = new Image(displayImgUrl);
		img.setSclass("section-label-image");
		imgDiv.appendChild(img);

		final Popup imgPopup = new Popup();
		final Image tooltipImg = new Image(displayImgUrl);
		imgPopup.appendChild(tooltipImg);
		imgDiv.appendChild(imgPopup);
		img.setTooltip(imgPopup);
		box.appendChild(imgDiv);

		final Div firstRow = new Div();
		firstRow.setSclass("statusActionContainer");
		descriptionContainer.appendChild(firstRow);

		final Div stausDiv = new Div();
		stausDiv.setClass(EDITOR_AREA_STATUS_DIV);
		firstRow.appendChild(stausDiv);

		if (getCurrentObject() != null)
		{
			final Div actionsDiv = new Div();
			actionsDiv.setClass(EDITOR_AREA_ACTIONS_DIV);
			renderActions(actionsDiv, getCurrentObject());
			//firstRow.appendChild(actionsDiv);
			firstRow.appendChild(actionsDiv);
		}

		box.appendChild(descriptionContainer);
		parent.appendChild(box);
	}

	protected GridViewConfiguration getUiConfiguration(final TypedObject item)
	{
		final ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService().getBestTemplate(item);
		return UISessionUtils.getCurrentSession().getUiConfigurationService()
				.getComponentConfiguration(template, GridView.DEFAULT_GRIDVIEW_CONF, GridViewConfiguration.class);
	}

	/**
	 * Render actions related icons
	 * 
	 * @param parent
	 * @param item
	 */

	protected void renderActions(final Component parent, final TypedObject item)

	{
		final GridViewConfiguration config = getUiConfiguration(item);
		final ActionColumnConfiguration actionConfiguration = getActionConfiguration(config);
		if (actionConfiguration != null)
		{
			ListActionHelper.renderActions(parent, item, actionConfiguration, "editorAreaLiveEditActionImg");
		}
	}


	public ActionColumnConfiguration getActionConfiguration(final GridViewConfiguration config)
	{
		if (config != null)
		{
			final String actionSpringBeanID = config.getActionSpringBeanID();
			if (actionSpringBeanID != null)
			{
				return (ActionColumnConfiguration) SpringUtil.getBean(actionSpringBeanID);

			}
		}
		return null;
	}

	protected UIEditorArea getEditorArea()
	{
		return this.editorArea;
	}

	/**
	 * @return the currentObject
	 */
	public TypedObject getCurrentObject()
	{
		return currentObject;
	}

	/**
	 * @param currentObject
	 *           the currentObject to set
	 */
	public void setCurrentObject(final TypedObject currentObject)
	{
		this.currentObject = currentObject;
	}

}
