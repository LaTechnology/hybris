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
package com.hybris.addon.cockpits.components.btg.listview;

import de.hybris.platform.btg.model.BTGRuleModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

import com.hybris.addon.cockpits.components.listview.AbstractConfigurableImageUriListViewAction;


/**
 * @author rmcotton
 * 
 */
public class ConfigurableBtgRuleIconAction extends AbstractConfigurableImageUriListViewAction
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.addon.cockpits.components.listview.AbstractConfigurableImageUriListViewAction#isValid(de.hybris.platform
	 * .cockpit.components.listview.ListViewAction.Context)
	 */
	@Override
	protected boolean isValid(final Context context)
	{
		return resolveTarget(context) instanceof BTGRuleModel;
	}

	protected Object resolveTarget(final Context context)
	{
		final TypedObject typedObject = context.getItem();
		return typedObject.getObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cockpit.components.listview.ListViewAction#getEventListener(de.hybris.platform.cockpit.components
	 * .listview.ListViewAction.Context)
	 */
	@Override
	public EventListener getEventListener(final Context context)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cockpit.components.listview.ListViewAction#getPopup(de.hybris.platform.cockpit.components.listview
	 * .ListViewAction.Context)
	 */
	@Override
	public Menupopup getPopup(final Context context)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cockpit.components.listview.ListViewAction#getContextPopup(de.hybris.platform.cockpit.components
	 * .listview.ListViewAction.Context)
	 */
	@Override
	public Menupopup getContextPopup(final Context context)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cockpit.components.listview.ListViewAction#getTooltip(de.hybris.platform.cockpit.components
	 * .listview.ListViewAction.Context)
	 */
	@Override
	public String getTooltip(final Context context)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.addon.cockpits.components.listview.AbstractConfigurableImageUriListViewAction#getKey(de.hybris.platform
	 * .cockpit.components.listview.ListViewAction.Context)
	 */
	@Override
	protected Object getKey(final Context context)
	{
		return ((BTGRuleModel) resolveTarget(context)).getRuleType();
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cockpit.components.listview.AbstractListViewAction#doCreateContext(de.hybris.platform.cockpit
	 * .components.listview.ListViewAction.Context)
	 */
	@Override
	protected void doCreateContext(final Context context)
	{
		// YTODO Auto-generated method stub

	}

}
