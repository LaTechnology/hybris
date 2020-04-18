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
package de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement;

import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.util.StopWatch;

import java.util.Collections;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Window;



/**
 * 
 */
public class ProductImageWizard
{
	private final String componentURI = "/cmscockpit/manageProductImage.zul";
	public static final String WIZARD_ARG = "wizardModel";

	private final ProductImageManagementViewModel editWildWordsViewModel;

	public ProductImageWizard(final ProductModel previewProduct, final String serverPath,
			final Map<String, String> mediaFormatsMap, final String siteUid)
	{
		editWildWordsViewModel = new DefaultProductImageManagementViewModel();
		final StopWatch timer = new StopWatch("Initializing Product Image Management Wizard");
		try
		{
			editWildWordsViewModel.initModel(previewProduct, serverPath, mediaFormatsMap, siteUid);
		}
		finally
		{
			timer.stop();
		}
	}

	public void show(final LiveEditView liveEditView)
	{
		final Window wizardWindow = createFrameComponent(liveEditView);
		editWildWordsViewModel.fireInitEvents();
		wizardWindow.setPosition("center");
		wizardWindow.doHighlighted();
	}

	protected Window createFrameComponent(final LiveEditView liveEditView)
	{
		final Window ret = (Window) Executions.createComponents(getComponentURI(), null,
				Collections.singletonMap(WIZARD_ARG, editWildWordsViewModel));

		ret.applyProperties();
		new AnnotateDataBinder(ret).loadAll();
		ret.setParent(liveEditView.getViewComponent());

		ret.addEventListener(Events.ON_CLOSE, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception //NOPMD:ZK Specific
			{
				ret.setParent(null);
				liveEditView.update();
			}
		});
		return ret;
	}

	protected String getComponentURI()
	{
		return this.componentURI;
	}

}
