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
package de.hybris.liveeditaddon.cockpit.wizards;

import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cmscockpit.wizard.controller.CmsComponentController;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;


/**
 *
 */
public class LiveEditCmsComponentController extends CmsComponentController
{
	private BrowserModel browserModel;
	private LiveEditView liveEditView;

	private static final Logger LOG = Logger.getLogger(LiveEditCmsComponentController.class);

	private List<LiveEditControllerCallbackHandler> callbackHandlers;

	/**
	 * Override this method so we can refresh the live edit frame after the wizard completes. The standard controller
	 * will refresh the page view but we're in live edit here.
	 */
	@Override
	public void done(final Wizard wizard, final WizardPage page) throws WizardConfirmationException
	{
		final CmsWizard cmsWizard = (CmsWizard) wizard;

		final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
		//TypedObject newItem = null;
		final BrowserModel browserModel = cmsWizard.getBrowserModel();
		final BrowserSectionModel browserSectionModel = cmsWizard.getBrowserSectionModel();
		try
		{
			final PropertyDescriptor descriptor = typeService
					.getPropertyDescriptor(Cms2Constants.TC.ABSTRACTCMSCOMPONENT + ".slots");
			ObjectValueHolder holder = null;
			if (descriptor.isLocalized())
			{
				holder = ((CmsWizard) wizard).getObjectValueContainer().getValue(descriptor,
						UISessionUtils.getCurrentSession().getLanguageIso());
			}
			else
			{
				holder = ((CmsWizard) wizard).getObjectValueContainer().getValue(descriptor, null);
			}
			if (holder != null)
			{
				final List<ContentSlotModel> values = new ArrayList<ContentSlotModel>();
				holder.setLocalValue(values);
				if (browserSectionModel == null)
				{
					final ContentSlotModel contentSlotModel = ((CmsPageBrowserModel) browserModel)
							.createSlotContentForCurrentPage(getPosition());
					final ContentSlotForPageModel relation = new ContentSlotForPageModel();
					relation.setCatalogVersion(contentSlotModel.getCatalogVersion());
					relation.setContentSlot(contentSlotModel);
					relation.setUid(getGenericRandomNameProducer().generateSequence(Cms2Constants.TC.CONTENTSLOT));
					final Object object = ((CmsPageBrowserModel) browserModel).getCurrentPageObject().getObject();
					if (object instanceof AbstractPageModel)
					{
						final AbstractPageModel currentPage = (AbstractPageModel) object;
						relation.setPage(currentPage);
						relation.setPosition(contentSlotModel.getCurrentPosition());

						UISessionUtils.getCurrentSession().getModelService().save(contentSlotModel);
						UISessionUtils.getCurrentSession().getModelService().save(relation);
						values.add(contentSlotModel);
					}
				}
				else
				{
					values.add((ContentSlotModel) ((TypedObject) browserSectionModel.getRootItem()).getObject());

				}
				final ObjectTemplate template = UISessionUtils.getCurrentSession().getTypeService()
						.getObjectTemplate(((CmsWizard) wizard).getCurrentType().getCode());
				UISessionUtils.getCurrentSession().getNewItemService()
						.createNewItem(((CmsWizard) wizard).getObjectValueContainer(), template);
			}
		}
		catch (final Exception e)
		{
			final Message msg = new Message(Message.ERROR, Labels.getLabel("editorarea.persist.error", new String[]
			{ ": " + e.getMessage() }), null);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Could not create item.", e);
			}
			cmsWizard.addMessage(msg);
			//re-thrown  
			throw new WizardConfirmationException(e);
		}


		if (callbackHandlers != null && !callbackHandlers.isEmpty())
		{
			for (LiveEditControllerCallbackHandler handler : callbackHandlers)
			{
				handler.onDone(wizard, page);
			}
		}

		// refresh the live edit page
		if (liveEditView != null)
		{
			liveEditView.update();
		}
	}


	@Override
	public void cancel(final Wizard wizard, final WizardPage page)
	{
		if (callbackHandlers != null && !callbackHandlers.isEmpty())
		{
			for (LiveEditControllerCallbackHandler handler : callbackHandlers)
			{
				handler.onCancel(wizard, page);
			}
		}
	}

	public List<LiveEditControllerCallbackHandler> getCallbackHandlers()
	{
		if (callbackHandlers == null)
		{
			callbackHandlers = new LinkedList<LiveEditControllerCallbackHandler>();
		}
		return callbackHandlers;
	}

	public void addCallbackHandler(LiveEditControllerCallbackHandler handler)
	{
		getCallbackHandlers().add(handler);
	}

	public void removeCallbackHandler(LiveEditControllerCallbackHandler handler)
	{
		if (callbackHandlers != null && callbackHandlers.contains(handler))
		{
			callbackHandlers.remove(handler);
		}
	}

	public void removeCallbackHandler(int index)
	{
		if (callbackHandlers != null && callbackHandlers.size() > index)
		{
			callbackHandlers.remove(index);
		}
	}

	/**
	 * @return the browserModel
	 */
	public BrowserModel getBrowserModel()
	{
		return browserModel;
	}

	/**
	 * @param browserModel
	 *           the browserModel to set
	 */
	public void setBrowserModel(final BrowserModel browserModel)
	{
		this.browserModel = browserModel;
	}

	/**
	 * @param liveEditView
	 *           the liveEditView to set
	 */
	public void setLiveEditView(final LiveEditView liveEditView)
	{
		this.liveEditView = liveEditView;
	}

}
