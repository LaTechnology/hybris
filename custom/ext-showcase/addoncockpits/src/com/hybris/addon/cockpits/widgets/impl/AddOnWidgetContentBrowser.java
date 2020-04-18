/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 * 
 */
package com.hybris.addon.cockpits.widgets.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractSimpleContentBrowser;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetContainer;
import de.hybris.platform.cockpit.widgets.WidgetFactory;
import de.hybris.platform.cockpit.widgets.browsers.WidgetBrowserModel;
import de.hybris.platform.cockpit.widgets.events.WidgetFocusEvent;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetContainer;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Style;

import com.hybris.addon.cockpits.widgets.AddOnWidgetConfig;


/**
 * Content Browser that support a basic Slot concept for appending additional widgets.
 * 
 * @author rmcotton
 * 
 */
public class AddOnWidgetContentBrowser extends AbstractSimpleContentBrowser implements CockpitEventAcceptor
{
	private static final Logger LOG = Logger.getLogger(AddOnWidgetContentBrowser.class);

	private WidgetContainer widgetContainer;

	@Override
	public void setModel(final BrowserModel model)
	{
		if (model instanceof WidgetBrowserModel)
		{
			super.setModel(model);
		}
		else
		{
			throw new IllegalArgumentException("Only browsers of type " + WidgetBrowserModel.class.getCanonicalName()
					+ " supported.");
		}
	}

	@Override
	public WidgetBrowserModel getModel()
	{
		return (WidgetBrowserModel) super.getModel();
	}

	@Override
	protected boolean initialize()
	{
		this.initialized = false;
		if (this.getModel() != null)
		{
			//			this.frameMap.clear();
			this.getChildren().clear();

			this.setWidth("100%");
			this.setHeight("100%");

			this.widgetContainer = new DefaultWidgetContainer(getModel().getWidgetFactory());
			final Map<String, Widget> widgets = this.widgetContainer.initialize(getModel().getWidgetMap());

			final String viewTemplateURI = getViewTemplateURI();
			if (StringUtils.isBlank(viewTemplateURI))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("No view layout template found. Adding widgets directly.");
				}
				final Div mainDiv = new Div();
				mainDiv.setParent(this);
				mainDiv.setHeight("100%");
				mainDiv.setWidth("100%");
				mainDiv.setSclass("widgetContentBrowser");

				createFrame(mainDiv, widgets);
			}
			else
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("View template defined. Trying to load template with URI '" + viewTemplateURI + "'.");
				}
				final Component widgetContainerView = Executions.createComponents(viewTemplateURI, this, Collections.EMPTY_MAP);
				for (final String widgetCode : widgets.keySet())
				{
					try
					{
						final Component widgetParent = widgetContainerView.getFellowIfAny(widgetCode);
						if (widgetParent == null)
						{
							final Widget widget = widgets.get(widgetCode);
							if (getModel().getWidgetMap().get(widgetCode) instanceof AddOnWidgetConfig)
							{
								final AddOnWidgetConfig widgetConfig = (AddOnWidgetConfig) getModel().getWidgetMap().get(widgetCode);
								final Component widgetSlot = widgetContainerView.getFellowIfAny(widgetConfig.getSlotId());
								if (widgetSlot != null)
								{
									final Div div = new Div();
									div.setId(widget.getWidgetCode());
									div.setSclass(widget.getWidgetCode());
									div.setParent(widgetSlot);
									widget.setParent(div);

									if (widgetConfig.getCssPath() != null)
									{
										final Style style = new Style();
										style.setSrc(widgetConfig.getCssPath());
										style.setParent(widgetContainerView);
									}
								}
								else
								{
									LOG.info("Widget with code '" + widgetCode + "' mapped but slot " + widgetConfig.getSlotId()
											+ " not available in layout template. Ignoring...");
								}
							}
							else
							{
								LOG.info("Widget with code '" + widgetCode + "' mapped but not available in layout template. Ignoring...");

							}
						}
						else
						{
							final Widget widget = widgets.get(widgetCode);
							widget.setParent(widgetParent);
						}
					}
					catch (final Exception e)
					{
						LOG.error("Error occurred while getting widget parent from template", e);
					}

				}
			}

			registerListeners();
			this.initialized = true;
		}
		return this.initialized;
	}

	protected String getViewTemplateURI()
	{
		return getModel().getViewTemplateURI();
	}

	protected WidgetContainer getWidgetContainer()
	{
		return this.widgetContainer;
	}

	@Override
	protected void cleanup()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cleaning up widget content browser...");
		}
		super.cleanup();

		unregisterListeners();

		// widget cleanup
		if (getWidgetContainer() != null)
		{
			getWidgetContainer().cleanup();
		}
	}

	protected void registerListeners()
	{
		if (getModel() != null)
		{
			getModel().addCockpitEventAcceptor(this);
		}
		if (this.getWidgetContainer() != null)
		{
			getWidgetContainer().addCockpitEventAcceptor(this);
		}
	}

	protected void unregisterListeners()
	{
		if (getModel() != null)
		{
			getModel().removeCockpitEventAcceptor(this);
		}
		if (getWidgetContainer() != null)
		{
			getWidgetContainer().removeCockpitEventAcceptor(this);
		}
	}

	protected void createFrame(final HtmlBasedComponent parent, final Map<String, Widget> widgets)
	{
		if (widgets != null && !widgets.isEmpty())
		{
			for (final Widget widget : widgets.values())
			{
				widget.setParent(parent);
			}
		}
	}

	@Override
	public void onCockpitEvent(final CockpitEvent event)
	{
		if (event instanceof WidgetFocusEvent)
		{
			final String focusedWidgetCode = ((WidgetFocusEvent) event).getWidgetCode();

			getWidgetContainer().focusWidget(focusedWidgetCode);

			// make sure to update model
			getModel().focusWidget(focusedWidgetCode);

			UISessionUtils.getCurrentSession().getCurrentPerspective().onCockpitEvent(event);
		}
	}

	protected WidgetFactory getWidgetFactory()
	{
		return this.getModel().getWidgetFactory();
	}
}
