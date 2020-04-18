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
package de.hybris.liveeditaddon.cockpit.service;

/**
 */

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.components.liveedit.RefreshContentHandler;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.liveeditaddon.cockpit.strategies.VariableViewportStrategy;
import de.hybris.liveeditaddon.enums.DeviceOrientation;
import de.hybris.liveeditaddon.model.DeviceSupportModel;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.HtmlBasedComponent;

import java.util.Map;


public abstract class AbstractViewportRefreshContentHandler implements RefreshContentHandler<LiveEditView>
{

	private static final String PIXEL_PREFIX = "px";
	private static final String FULL_HEIGHT = "100%";
	private final static String VERTICAL_SCLASS = "device-vertical";
	private final static String HORIZONTAL_SCLASS = "device-horizontal";
	private final static String DEVICE_PREFIX = "device-";
	private final static String PARENT_SCLASS = "liveEditWrapper";

	private VariableViewportStrategy variableViewportStrategy;

	abstract protected String getUiExperience();

	abstract protected String getUiExperienceSclass();

	abstract protected String getDefaultViewportWidth();

	private void setFrameSclass(final LiveEditView view, final String sclass)
	{
		HtmlBasedComponent parent = (HtmlBasedComponent) view.getContentFrame().getParent();
		parent.setSclass(PARENT_SCLASS + " " + getUiExperienceSclass() + " " + sclass);
	}

	@Override
	public void onRefresh(final LiveEditView view)
	{
		if (view.getModel() != null)
		{
			final PreviewDataModel previewDataModel = view.getModel().getCurrentPreviewData();
			if (previewDataModel != null && previewDataModel.getUiExperience() != null)
			{
				if (getVariableViewportStrategy().supportVariableViewport(previewDataModel, getUiExperience()))
				{
					String viewport = previewDataModel.getViewport();
					Map<String, String> viewportSupportMap = getVariableViewportStrategy().getViewports();

					DeviceSupportModel deviceSupport = previewDataModel.getDeviceSupport();

					if (deviceSupport != null && !deviceSupport.isDefault())
					{
						DeviceOrientation orientation = previewDataModel.getDeviceOrientation();
						if (deviceSupport.isOrientation() && orientation.equals(DeviceOrientation.HORIZONTAL))
						{
							view.getContentFrame().setWidth(deviceSupport.getHeight().toString() + PIXEL_PREFIX);
							view.getContentFrame().setHeight(deviceSupport.getWidth().toString() + PIXEL_PREFIX);
							setFrameSclass(view, HORIZONTAL_SCLASS + " " + DEVICE_PREFIX + deviceSupport.getCode());
						}
						else
						{
							view.getContentFrame().setWidth(deviceSupport.getWidth().toString() + PIXEL_PREFIX);
							view.getContentFrame().setHeight(deviceSupport.getHeight().toString() + PIXEL_PREFIX);
							setFrameSclass(view, VERTICAL_SCLASS + " " + DEVICE_PREFIX + deviceSupport.getCode());
						}
					}
					else if (viewport != null && viewportSupportMap.containsKey(viewport)
							&& (deviceSupport == null || deviceSupport.isDefault()))
					{
						String viewportWidth = viewportSupportMap.get(viewport);
						view.getContentFrame().setWidth(viewportWidth + PIXEL_PREFIX);
						view.getContentFrame().setHeight(FULL_HEIGHT);
						setFrameSclass(view, "");
					}
					else
					{
						view.getContentFrame().setWidth(getDefaultViewportWidth());
						view.getContentFrame().setHeight(FULL_HEIGHT);
						setFrameSclass(view, "");
					}
				}
			}
		}
	}

	public VariableViewportStrategy getVariableViewportStrategy()
	{
		return variableViewportStrategy;
	}

	@Required
	public void setVariableViewportStrategy(final VariableViewportStrategy variableViewportStrategy)
	{
		this.variableViewportStrategy = variableViewportStrategy;
	}

}
