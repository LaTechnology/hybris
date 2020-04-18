package com.hybris.addon.cockpits.liveedit.editor;

import de.hybris.platform.cockpit.components.editor.CockpitEditorContainer;
import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorHelper.LanguageAwareEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.AbstractPropertyDescriptor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.EditorRowRenderer;
import de.hybris.platform.cockpit.session.impl.EditorRowRenderer.SingleEditorRenderer;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Label;


/**
 * CockpitEditorContainer for wizard multitypes - supports source object as parameter and localized values from
 * container
 * 
 * @author aandone
 */
public class WizardCockpitEditorContainer extends CockpitEditorContainer
{

	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(WizardCockpitEditorContainer.class);

	@Override
	public void initialize()
	{
		super.applyProperties();

		final Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(getAttributes());

		final String propertyQualifier = getPropertyQualifier();

		if (propertyQualifier == null)
		{
			renderEditorContainer(params);
		}
		else
		{
			renderEditorContainer(params, propertyQualifier);
		}

	}

	private void renderEditorContainer(final Map<String, Object> params)
	{
		ObjectType valueType = null;
		final String editorType;
		final String valueTypeCode = getValueTypeCode();

		if (valueTypeCode == null)
		{
			return;
		}

		try
		{
			valueType = UISessionUtils.getCurrentSession().getTypeService().getObjectType(valueTypeCode);
		}
		catch (final UnknownIdentifierException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Retrieving composed type '' failed, using valueType directly as editor type.", e);
			}
		}

		if (valueType == null)
		{
			editorType = valueTypeCode;
		}
		else
		{
			editorType = PropertyDescriptor.REFERENCE;
		}

		final UIEditor uiEditor = createUIEditor(editorType);

		if (uiEditor instanceof ReferenceUIEditor)
		{
			((ReferenceUIEditor) uiEditor).setRootType(valueType);
			if (!StringUtils.isEmpty((String) params.get(ReferenceUIEditor.ALLOW_CREATE_PARAM_KEY)))
			{
				((ReferenceUIEditor) uiEditor).setAllowCreate(Boolean.valueOf((String) params
						.get(ReferenceUIEditor.ALLOW_CREATE_PARAM_KEY)));
			}
			else
			{
				((ReferenceUIEditor) uiEditor).setAllowCreate(Boolean.TRUE);
			}
		}

		if (uiEditor instanceof ListUIEditor)
		{
			((ListUIEditor) uiEditor).setAvailableValues(EditorHelper.getAvailableValues(params, null));
		}

		final Object editorValue = getEditorValue();
		if (isLocalized())
		{
			final List<String> availableLanguageIsos = new ArrayList<String>(UISessionUtils.getCurrentSession().getSystemService()
					.getAvailableLanguageIsos());
			EditorRowRenderer.renderLocalizedStructure(this, availableLanguageIsos, availableLanguageIsos,
					new SingleEditorRenderer()
					{
						@Override
						public void render(final Component parent, final String isoCode)
						{

							final HtmlBasedComponent viewComponent = uiEditor.createViewComponent(
									getLocalizedValueIfMap(editorValue, isoCode), params, new EditorListener()
									{
										@Override
										public void valueChanged(final Object value)
										{
											setEditorValue(value, isoCode);
										}

										@Override
										public void actionPerformed(final String actionCode)
										{
										}

									});
							parent.appendChild(viewComponent);
						}
					});

		}
		else
		{
			final HtmlBasedComponent viewComponent = uiEditor.createViewComponent(editorValue, params, new EditorListener()
			{
				@Override
				public void valueChanged(final Object value)
				{
					setEditorValue(value, null);
				}

				@Override
				public void actionPerformed(final String actionCode)
				{
				}
			});
			this.appendChild(viewComponent);
		}

	}

	private void renderEditorContainer(final Map<String, Object> params, final String propertyQualifier)
	{
		try
		{
			final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();

			final PropertyDescriptor propertyDescriptor = typeService.getPropertyDescriptor(propertyQualifier);
			final ObjectType valueType = typeService.getObjectTypeFromPropertyQualifier(propertyQualifier);

			ObjectValueContainer valueContainer = getValueContainer();
			if (valueContainer == null)
			{
				final Set<String> isos = UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos();
				valueContainer = new ObjectValueContainer(valueType, null);
				if (propertyDescriptor.isLocalized())
				{
					for (final String iso : isos)
					{
						valueContainer.addValue(propertyDescriptor, iso, null);
					}
				}
				else
				{
					valueContainer.addValue(propertyDescriptor, null, null);
				}

				setValueContainer(valueContainer);
			}

			final String editorCode = getEditorCode();
			final TypedObject item = typeService.wrapItem(valueContainer.getObject());
			if (propertyDescriptor.isLocalized())
			{

				EditorHelper.renderLocalizedEditor(item, propertyDescriptor, this, valueContainer, false, editorCode, params, false,
						createEditorListenerForLocalization(propertyDescriptor));
			}
			else
			{
				EditorHelper.renderSingleEditor(item, propertyDescriptor, this, valueContainer, false, editorCode, params, null,
						false, createEditorListener(propertyDescriptor));
			}
		}
		catch (final Exception e)
		{
			this.appendChild(new Label("[Could not render editor for '" + propertyQualifier + "']"));
			LOG.error("Could not render editor for '" + propertyQualifier + "', reason was: ", e);
		}
	}

	protected EditorListener createEditorListenerForLocalization(final PropertyDescriptor propertyDescriptor)
	{
		final EditorListener createEditorListener = super.createEditorListener(propertyDescriptor);

		if (!(createEditorListener instanceof LanguageAwareEditorListener))
		{
			return createEditorListener;
		}

		final LanguageAwareEditorListener editorListener = (LanguageAwareEditorListener) createEditorListener;

		return new LanguageAwareEditorListener()
		{

			@Override
			public void valueChanged(final Object value)
			{
				editorListener.valueChanged(value, UISessionUtils.getCurrentSession().getGlobalDataLanguageIso());

			}

			@Override
			public void actionPerformed(final String actionCode)
			{
				editorListener.actionPerformed(actionCode, UISessionUtils.getCurrentSession().getGlobalDataLanguageIso());
			}

			@Override
			public void actionPerformed(final String actionCode, final String langIso)
			{
				editorListener.actionPerformed(actionCode, langIso);

			}

			@Override
			public void valueChanged(final Object value, final String langIso)
			{
				editorListener.valueChanged(value, langIso);
			}
		};

	}

	private UIEditor createUIEditor(final String editorType)
	{
		return EditorHelper.getUIEditor(new AbstractPropertyDescriptor()
		{
			@Override
			public String getEditorType()
			{
				return editorType;
			}

			@Override
			public boolean isWritable()
			{
				return true;
			}

			@Override
			public boolean isReadable()
			{
				return true;
			}

			@Override
			public String getName(final String languageIso)
			{
				return null;
			}

			@Override
			public boolean isLocalized()
			{
				return isLocalized();
			}

		}, getEditorCode());
	}

}
