package com.hybris.addon.cockpits.liveedit.generic.wizard.controllers;

import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.exception.WizardConfirmationException;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import de.hybris.platform.cockpit.wizards.impl.DefaultPageController;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

import com.hybris.addon.cockpits.liveedit.generic.handlers.AfterSaveWizardActionHandler;
import com.hybris.addon.cockpits.liveedit.generic.handlers.BeforeSaveWizardActionHandler;
import com.hybris.addon.cockpits.liveedit.generic.wizard.DefaultGenericItemWizard;
import com.hybris.addon.cockpits.liveedit.generic.wizard.pages.DefaultItemWizardPage;


/**
 * Default controller for multitype wizard
 *
 * @author aandone
 */
public class DefaultWizardPageController extends DefaultPageController
{

	private static final Logger LOG = Logger.getLogger(DefaultWizardPageController.class);

	private SessionService sessionService;
	private TypeService typeService;

	@Override
	public void initPage(final Wizard wizard, final WizardPage page)
	{
		if (page instanceof DefaultItemWizardPage)
		{
			final DefaultItemWizardPage wizardPage = (DefaultItemWizardPage) page;
			final String currentTypeString = wizardPage.getCurrentTypeString();

			final GenericItemWizard genericItemWizard = (GenericItemWizard) wizard;
			if (currentTypeString != null && StringUtils.isNotBlank(currentTypeString))
			{
				final ObjectType currentType = UISessionUtils.getCurrentSession().getTypeService().getObjectType(currentTypeString);
				genericItemWizard.setCurrentType(currentType);
				if (genericItemWizard.getItem() == null)
				{
					final UISession session = UISessionUtils.getCurrentSession();
					final ItemModel model = session.getModelService().create(currentType.getCode());
					genericItemWizard.setItem(session.getTypeService().wrapItem(model));
				}
			}
		}
	}

	@Override
	public void done(final Wizard wizard, final WizardPage page) throws WizardConfirmationException
	{

		final DefaultGenericItemWizard currentWizard = (DefaultGenericItemWizard) wizard;
		final Map<String, ObjectValueContainer> wizardValueContainers = currentWizard.getWizardValueContainers();
		final Map<String, TypedObject> wizardTypeItems = currentWizard.getWizardTypeItems();


		for (final Iterator<Entry<String, TypedObject>> iterator = wizardTypeItems.entrySet().iterator(); iterator.hasNext();)
		{
			final Entry<String, TypedObject> entry = iterator.next();

			final String type = entry.getKey();
			final TypedObject item = entry.getValue();

			boolean shouldSaveItem = true;
			for (final BeforeSaveWizardActionHandler actionHandler : currentWizard.getBeforeSaveActionHandlers())
			{
				if (!actionHandler.beforeSave(currentWizard, item))
				{
					shouldSaveItem = false;
				}
			}

			if (shouldSaveItem)
			{
				saveItem(currentWizard, item, wizardValueContainers.get(type));
			}
		}

		for (final AfterSaveWizardActionHandler actionHandler : currentWizard.getAfterSaveActionHandlers())
		{
			actionHandler.afterSave(currentWizard, page);
		}

		super.done(wizard, page);
	}


	private void saveItem(final Wizard wizard, TypedObject item, final ObjectValueContainer valueContainer)
	{
		final UISession uiSession = UISessionUtils.getCurrentSession();
		final ModelService modelService = uiSession.getModelService();
		try
		{
			if (modelService.isNew(item.getObject()))
			{
				item = createItem(item.getType(), valueContainer);
			}
			else
			{
				EditorHelper.persistValues(item, valueContainer);
			}
		}
		catch (final Exception e)
		{
			final Message msg = createErrorMessageForException(e);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Could not create item.", e);
			}
			wizard.addMessage(msg);
			//re-thrown
			throw new WizardConfirmationException(e);
		}
	}

	private TypedObject createItem(final ObjectType type, final ObjectValueContainer valueContainer) throws ValueHandlerException
	{
		final UISession session = UISessionUtils.getCurrentSession();
		final ObjectTemplate template = getTypeService().getObjectTemplate(type.getCode());
		final TypedObject newItem = session.getNewItemService().createNewItem(valueContainer, template);
		return newItem;
	}

	protected Message createErrorMessageForException(final Exception exception)
	{
		final String label = Labels.getLabel("editorarea.persist.error", new String[]
		{ ":" + exception.getMessage() });

		return new Message(Message.ERROR, label, null);
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the typeService
	 */
	public TypeService getTypeService()
	{
		return typeService;
	}

	/**
	 * @param typeService
	 *           the typeService to set
	 */
	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}
}
