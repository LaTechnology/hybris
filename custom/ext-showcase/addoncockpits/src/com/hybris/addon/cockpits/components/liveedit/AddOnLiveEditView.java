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
package com.hybris.addon.cockpits.components.liveedit;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmscockpit.components.liveedit.RefreshContentHandler;
import de.hybris.platform.cmscockpit.components.liveedit.impl.DefaultLiveEditViewModel;
import de.hybris.platform.cmscockpit.components.liveedit.impl.LiveEditPopupEditDialog;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.yacceleratorcockpits.components.liveedit.DefaultLiveEditPopupEditDialog;
import de.hybris.platform.yacceleratorcockpits.components.liveedit.DefaultLiveEditView;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;


/**
 * @author rmcotton
 *
 */
public class AddOnLiveEditView<M extends DefaultLiveEditViewModel, D extends DefaultLiveEditPopupEditDialog> extends
		DefaultLiveEditView
{
	private static final Logger LOG = Logger.getLogger(AddOnLiveEditView.class);
	private static final String COMPONENT_ID = "cmp_id";

	/**
	 * @param model
	 */
	public AddOnLiveEditView(final M model)
	{
		super(model);
	}

	public AddOnLiveEditView(final M model, final Div welcomePanel)
	{
		super(model, welcomePanel);
	}

	public static class MobileRefreshContentHandler implements RefreshContentHandler<AddOnLiveEditView>
	{
		@Override
		public void onRefresh(final AddOnLiveEditView view)
		{
			if (view.getModel() != null)
			{
				final PreviewDataModel previewDataModel = view.getModel().getCurrentPreviewData();
				if (previewDataModel != null && previewDataModel.getUiExperience() != null)
				{
					if (UiExperienceLevel.MOBILE.getCode().equalsIgnoreCase(previewDataModel.getUiExperience().getCode()))
					{
						view.getContentFrame().setWidth("320px%");
						view.getContentFrame().setHeight("100%");
						final HtmlBasedComponent parent = (HtmlBasedComponent) view.getContentFrame().getParent();
						parent.setSclass("liveEditWrapper liveEditBrowser-mobile");
					}
				}
			}
		}
	}

	@Override
	protected EventListener getUserEventListener()
	{
		return new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD:ZK specific
			{
				//early exit when prerequisites aren't matched
				final String passedAttributes[];
				if (!(event.getData() instanceof String[]))
				{
					passedAttributes = event.getData() == null ? null : new String[]
					{ String.valueOf(event.getData()) };
				}
				else
				{
					passedAttributes = (String[]) event.getData();
				}


				final CallbackEventHandlerRegistry eventHandlers = (CallbackEventHandlerRegistry) SpringUtil.getBean(
						"liveEditCallbackEventHandlerRegistry", CallbackEventHandlerRegistry.class);

				final CallbackEventHandler<AddOnLiveEditView> handler = eventHandlers.getHandlerById(passedAttributes[0]);
				if (handler == null)
				{
					throw new IllegalStateException("unexepected event type [" + passedAttributes[0]
							+ "]. Please ensure a CallbackEventHandler has been configured");
				}

				if (handler instanceof LockAwareEventHandler)
				{
					((LockAwareEventHandler) handler).onLockCallbackEvent(AddOnLiveEditView.this, passedAttributes);
				}
				else
				{
					handler.onCallbackEvent(AddOnLiveEditView.this, passedAttributes);
				}
			}
		};

	}

	public void onSimpleEditCallbackEvent(final String[] attributes) throws InterruptedException
	{
		final Map<String, Object> components = new HashMap<String, Object>();
		components.put(COMPONENT_ID, attributes[0]);
		final LiveEditPopupEditDialog popupEditorDialog = createLiveEditPopupDialog(components);
		getViewComponent().appendChild(popupEditorDialog);
	}

	public static class Click2EditCallbackEventHandler implements CallbackEventHandler<AddOnLiveEditView>
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hybris.addon.cockpits.components.liveedit.CallbackEventHandler#getEventId()
		 */
		@Override
		public String getEventId()
		{
			return CALLBACK_EVENT;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.hybris.addon.cockpits.components.liveedit.CallbackEventHandler#onCallbackEvent(de.hybris.platform.cmscockpit
		 * .components.liveedit.LiveEditView, java.lang.String[])
		 */
		@Override
		public void onCallbackEvent(final AddOnLiveEditView view, final String[] passedAttributes) throws InterruptedException
		{
			view.onSimpleEditCallbackEvent(passedAttributes);
		}

	}

}
