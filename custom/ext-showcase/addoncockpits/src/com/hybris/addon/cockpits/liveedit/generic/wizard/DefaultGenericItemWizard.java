package com.hybris.addon.cockpits.liveedit.generic.wizard;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor.Multiplicity;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer.ObjectValueHolder;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import de.hybris.platform.cockpit.wizards.generic.GenericItemWizard;
import de.hybris.platform.cockpit.wizards.impl.DefaultPage;
import de.hybris.platform.core.model.ItemModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;

import com.hybris.addon.cockpits.liveedit.generic.handlers.AfterSaveWizardActionHandler;
import com.hybris.addon.cockpits.liveedit.generic.handlers.BeforeSaveWizardActionHandler;
import com.hybris.addon.cockpits.liveedit.generic.wizard.pages.DefaultItemWizardPage;


/**
 * @author dmalachowski
 */

public class DefaultGenericItemWizard extends GenericItemWizard
{
	private static final String CLASSPATH_PREFIX = "classpath:";
	private static final Logger LOG = Logger.getLogger(DefaultGenericItemWizard.class);

	private final Map<String, ObjectValueContainer> wizardValueContainers = new HashMap<String, ObjectValueContainer>();
	private final Map<String, TypedObject> wizardTypeItems = new LinkedHashMap<String, TypedObject>();

	private List<AfterSaveWizardActionHandler> afterSaveActionHandlers;
	private List<BeforeSaveWizardActionHandler> beforeSaveActionHandlers;
	private Map<String, Object> wizardArguments;

	@Override
	protected Component createPageComponent(final Component parent, final WizardPage page, final WizardPageController controller)
	{
		Component ret = null;

		if (StringUtils.isNotBlank(page.getComponentURI()) && (page instanceof DefaultItemWizardPage))
		{
			ret = defaultCreatePageComponent(parent, page, controller, wizardArguments);
		}
		else
		{
			ret = super.createPageComponent(parent, page, controller);
		}

		return ret;
	}

	@Override
	public void setItem(final TypedObject item)
	{
		wizardTypeItems.put(getCurrentType().getCode(), item);
	}

	@Override
	public TypedObject getItem()
	{
		final String currentTypeCode = getCurrentType().getCode();
		if (wizardTypeItems.containsKey(currentTypeCode))
		{
			return wizardTypeItems.get(currentTypeCode);
		}
		else
		{
			return super.getItem();
		}
	}

	private Component defaultCreatePageComponent(final Component parent, final WizardPage page,
			final WizardPageController controller, final Map args)
	{
		parent.setVariable("pageBean", page, true);
		parent.setVariable("controllerBean", controller, true);
		parent.setVariable("wizardBean", this, true);

		Component ret;

		String pageCmpURI = page.getComponentURI();
		if (StringUtils.isBlank(pageCmpURI))
		{
			ret = new Div();
			if (page instanceof DefaultPage)
			{
				((DefaultPage) page).renderView(ret);
			}
		}
		else
		{
			if (StringUtils.isNotBlank(getPageRoot()) && pageCmpURI.charAt(0) != '/' && !pageCmpURI.startsWith(CLASSPATH_PREFIX))
			{
				pageCmpURI = getPageRoot() + "/" + pageCmpURI; // NOPMD
			}

			if (pageCmpURI.startsWith(CLASSPATH_PREFIX))
			{
				final String viewURI = pageCmpURI.replaceFirst(CLASSPATH_PREFIX, "");
				final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(viewURI);

				if (inputStream == null)
				{
					LOG.error("Could not open specified uri '" + viewURI + "'");
					ret = new Div();
				}
				else
				{
					try
					{
						ret = Executions.createComponentsDirectly(new InputStreamReader(inputStream), null, parent, args);
					}
					catch (final IOException e)
					{
						LOG.error("Could not create view with URI '" + viewURI + "', reason: ", e);
						ret = new Div();
					}
				}
			}
			else
			{
				controller.initPage(this, page);
				ret = Executions.createComponents(pageCmpURI, parent, args);
			}
		}
		ret.applyProperties();
		final List<Component> newones = parent.getChildren();
		new AnnotateDataBinder(newones.toArray(new Component[newones.size()])).loadAll();

		return ret;
	}

	public ObjectValueContainer getObjectValueContainer(final ItemModel model)
	{

		final UISession uiSession = UISessionUtils.getCurrentSession();
		final TypeService typeService = uiSession.getTypeService();

		final ObjectTemplate itemType = typeService.getObjectTemplate(model.getItemtype());
		final ObjectValueContainer objectValueContainer = new ObjectValueContainer(itemType, null);

		final ObjectType currentBaseType = itemType.getBaseType();

		final ObjectValueHandlerRegistry valueHandlerRegistry = uiSession.getValueHandlerRegistry();

		final TypedObject artificialTypedObject = typeService.wrapItem(model);

		objectValueContainer.setObject(model);

		for (final ObjectValueHandler valueHandler : valueHandlerRegistry.getValueHandlerChain(currentBaseType))
		{
			try
			{
				valueHandler.loadValues(objectValueContainer, currentBaseType, artificialTypedObject,
						currentBaseType.getPropertyDescriptors(), getLoadLanguages());
			}
			catch (final ValueHandlerPermissionException e)
			{
				//do nothing
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Not sufficient privilages!", e);
				}
			}
			catch (final ValueHandlerException e)
			{
				LOG.error("error loading object values", e);
			}
		}

		uiSession.getNewItemService().setDefaultValues(objectValueContainer,
				uiSession.getTypeService().getObjectTemplate(currentBaseType.getCode()));

		wizardValueContainers.put(currentBaseType.getCode(), objectValueContainer);

		return objectValueContainer;
	}

	@Override
	public ObjectValueContainer getObjectValueContainer()
	{
		final TypedObject currentTypedObject = getItem();
		if (currentTypedObject != null)
		{
			final String currentTypedCode = currentTypedObject.getType().getCode();
			if (wizardValueContainers.containsKey(currentTypedCode))
			{
				return wizardValueContainers.get(currentTypedCode);
			}
			else
			{
				return getObjectValueContainer((ItemModel) getItem().getObject());
			}
		}
		else
		{
			return super.getObjectValueContainer();
		}
	}

	public Map<String, TypedObject> getWizardTypeItems()
	{
		return wizardTypeItems;
	}

	public Map<String, ObjectValueContainer> getWizardValueContainers()
	{
		return wizardValueContainers;
	}

	@Override
	public void setCurrentType(final ObjectType currentType)
	{
		String oldTypeCode = null;

		if (getCurrentType() != null)
		{
			oldTypeCode = getCurrentType().getCode();
		}

		super.setCurrentType(currentType);

		if (currentType != null && !currentType.getCode().equals(oldTypeCode))
		{
			if (getItem() != null && !getItem().getType().getCode().equals(currentType.getCode()))
			{
				setItem(null);
			}
		}
	}

	public void handleAdditionalValues(final Map<String, Object> additionalValues, final String type,
			final ObjectValueContainer valueContainer)
	{
		if (additionalValues == null)
		{
			return;
		}

		final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();

		for (final Entry<String, Object> entry : additionalValues.entrySet())
		{
			if (entry.getKey().startsWith(type + "."))
			{
				final PropertyDescriptor propertyDescriptor = typeService.getPropertyDescriptor(entry.getKey());

				final ObjectValueHolder holder = valueContainer.getValue(propertyDescriptor, null);
				final Multiplicity multiplicity = propertyDescriptor.getMultiplicity();

				switch (multiplicity)
				{
					case SINGLE:
						holder.setLocalValue(typeService.wrapItem(entry.getValue()));
						break;
					case LIST:
					{
						Collection<TypedObject> values = (Collection<TypedObject>) holder.getCurrentValue();
						if (values == null)
						{
							values = new ArrayList<TypedObject>();
						}
						else
						{
							values = new ArrayList<TypedObject>(values);
						}

						appendValues(entry.getValue(), values);
						holder.setLocalValue(values);
						break;
					}
					case SET:
					{
						Collection<TypedObject> values = (Collection<TypedObject>) holder.getCurrentValue();
						if (values == null)
						{
							values = new HashSet<TypedObject>();
						}
						else
						{
							values = new HashSet<TypedObject>(values);
						}

						appendValues(entry.getValue(), values);
						holder.setLocalValue(values);
						break;
					}
					default:
						throw new UnsupportedOperationException("multiplicity unsupported: " + multiplicity);
				}
				holder.setModified(true);
			}
		}
	}

	private void appendValues(final Object valueToAppend, final Collection<TypedObject> values)
	{
		final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
		if (valueToAppend instanceof Collection)
		{
			values.addAll(typeService.wrapItems((Collection<? extends Object>) valueToAppend));
		}
		else
		{
			values.add(typeService.wrapItem(valueToAppend));
		}
	}

	/**
	 * @return the wizardArguments
	 */
	public Map<String, Object> getWizardArguments()
	{
		return wizardArguments;
	}

	/**
	 * @param wizardArguments
	 *           the wizardArguments to set
	 */
	public void setWizardArguments(final Map<String, Object> wizardArguments)
	{
		this.wizardArguments = wizardArguments;
	}

	/**
	 * @return the afterSaveActionHandlers
	 */
	public List<AfterSaveWizardActionHandler> getAfterSaveActionHandlers()
	{
		return afterSaveActionHandlers;
	}

	/**
	 * @param afterSaveActionHandlers
	 *           the afterSaveActionHandlers to set
	 */
	@Required
	public void setAfterSaveActionHandlers(final List<AfterSaveWizardActionHandler> afterSaveActionHandlers)
	{
		this.afterSaveActionHandlers = afterSaveActionHandlers;
	}

	/**
	 * @return the beforeSaveActionHandlers
	 */
	public List<BeforeSaveWizardActionHandler> getBeforeSaveActionHandlers()
	{
		return beforeSaveActionHandlers;
	}

	/**
	 * @param beforeSaveActionHandlers
	 *           the beforeSaveActionHandlers to set
	 */
	@Required
	public void setBeforeSaveActionHandlers(final List<BeforeSaveWizardActionHandler> beforeSaveActionHandlers)
	{
		this.beforeSaveActionHandlers = beforeSaveActionHandlers;
	}

}
