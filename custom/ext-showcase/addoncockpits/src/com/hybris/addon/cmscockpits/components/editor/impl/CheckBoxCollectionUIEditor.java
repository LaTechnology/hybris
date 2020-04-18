/**
 *
 */
package com.hybris.addon.cmscockpits.components.editor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.collection.AbstractCollectionEditor;
import de.hybris.platform.cockpit.model.referenceeditor.collection.CollectionEditor;
import de.hybris.platform.cockpit.model.referenceeditor.collection.CollectionUIEditor;
import de.hybris.platform.cockpit.model.referenceeditor.collection.controller.CollectionUIEditorController;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.CollectionEditorModelListener;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.DefaultCollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.DefaultCollectionEditorModelListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.generic.strategies.PredefinedValuesStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.HtmlBasedComponent;


/**
 * @author c.stefanache
 *
 */
public class CheckBoxCollectionUIEditor extends CollectionUIEditor
{

	public static final String PREDEFINED_VALUES_STRATEGY = "predefinedValuesStrategy";

	@Override
	public HtmlBasedComponent createViewComponent(final Object initialValue, final Map<String, ? extends Object> parameters,
			final EditorListener listener)
	{
		// ProxyListener ignores any Escape buttons to Prevent Valuereset
		final EditorListener proxyListener = new EditorListener()
		{
			@Override
			public void valueChanged(final Object value)
			{
				listener.valueChanged(value);
			}

			@Override
			public void actionPerformed(final String actionCode)
			{
				// Do not reset the value of editor when escape is placed. We do want to lose the selected items
				if (!ESCAPE_PRESSED.equals(actionCode))
				{
					listener.actionPerformed(actionCode);
				}
			}
		};

		final Object additionalListenerParam = parameters.get(AdditionalReferenceEditorListener.class.getName());
		AdditionalReferenceEditorListener additionalListener = null;
		if (additionalListenerParam instanceof AdditionalReferenceEditorListener)
		{
			additionalListener = (AdditionalReferenceEditorListener) additionalListenerParam;
		}

		final Integer maxAC = findMaxAutocompleteSearchResults(parameters);
		if (maxAC != null && maxAC.intValue() > 0)
		{
			model.getSimpleReferenceSelectorModel().setMaxAutoCompleteResultSize(maxAC.intValue());
		}
		model.setParameters(parameters);

		//collection editor setup
		collectionEditor = createCollectionEditor(proxyListener, additionalListener);
		final Object createContext = parameters.get("createContext");
		if (createContext instanceof CreateContext)
		{
			collectionEditor.setCreateContext((CreateContext) createContext);
		}
		collectionEditor.setDisabled(!isEditable());
		collectionEditor.setAllowCreate(isAllowCreate());
		collectionEditor.setParameters(parameters);

		assignedPassedValue(initialValue);
		appendDefaultValues(parameters);

		collectionEditor.setModel(model);
		applyTestUserID(parameters);

		//collection controller setup
		if (collectionController != null)
		{
			collectionController.unregisterListeners();
		}
		collectionController = createCollectionController(collectionEditor, model, proxyListener, getRootType());
		collectionController.initialize();

		//container setup
		final CancelButtonContainer cancelButtonContainer = new CancelButtonContainer(proxyListener, new CancelListener()
		{
			@Override
			public void cancelPressed()
			{
				collectionEditor.fireCancel();
			}
		});

		registerEventListeners(cancelButtonContainer);
		cancelButtonContainer.setSclass("collectionReferenceEditor");
		cancelButtonContainer.setContent(collectionEditor);
		return cancelButtonContainer;
	}

	protected void applyTestUserID(final Map<String, ? extends Object> parameters)
	{
		if (UISessionUtils.getCurrentSession().isUsingTestIDs())
		{
			String id = "ReferenceSelector_";
			String attQual = (String) parameters.get(AbstractUIEditor.ATTRIBUTE_QUALIFIER_PARAM);
			if (attQual != null)
			{
				attQual = attQual.replaceAll("\\W", "");
				id = id + attQual;
			}
			UITools.applyTestID(collectionEditor, id);
		}
	}

	@Override
	public CollectionEditor createCollectionEditor(final EditorListener listener,
			final AdditionalReferenceEditorListener additionalListener)
	{
		return new CheckBoxCollectionEditor(listener, additionalListener);
	}


	public CollectionUIEditorController createCollectionController(final AbstractCollectionEditor collectionEditor,
			final DefaultCollectionEditorModel model, final EditorListener editorListener, final ObjectType rootType)
	{
		return new CollectionUIEditorController(collectionEditor, model, editorListener, rootType)
		{
			@Override
			protected CollectionEditorModelListener createCollectionEditorModelListener()
			{
				return new DefaultCollectionEditorModelListener(model, collectionEditor, editorListener)
				{
					@Override
					public void collectionItemsChanged()
					{
						//we are just updating the list of collection items using to represent the list view, we are not saving in the database
						collectionEditor.updateCollectionItems();
					}
				};
			}
		};
	}

	@Override
	protected void assignedPassedValue(final Object initialValue)
	{
		if (initialValue == null)
		{
			model.setCollectionItems(Collections.EMPTY_LIST);
		}
		else
		{
			if (initialValue instanceof Collection)
			{
				if (!((Collection) initialValue).isEmpty())
				{
					final Object firstItem = ((Collection) initialValue).iterator().next();
					if (firstItem instanceof TypedObject // only typed object allowed
							&& UISessionUtils.getCurrentSession().getTypeService().getObjectType(this.model.getRootType().getCode())
									.isAssignableFrom(((TypedObject) firstItem).getType())) // root type has to be super type of initial value's type
					{
						model.setCollectionItems(new ArrayList<Object>((Collection) initialValue));
						if (collectionEditor instanceof CheckBoxCollectionEditor)
						{
							((CheckBoxCollectionEditor) collectionEditor).setSelectedElements(new ArrayList<TypedObject>(
									(Collection) initialValue));
						}
					}
					else
					{
						throw new IllegalArgumentException("Initial value '" + initialValue + "' can not be assigned to root type '"
								+ this.model.getRootType() + "'");
					}
				}
			}
			else if (initialValue instanceof TypedObject)
			{
				model.setCollectionItems(Collections.singletonList(initialValue));
			}
			else
			{
				throw new IllegalArgumentException("Initial value '" + initialValue + "' not a typed object.");
			}
		}
	}

	private void appendDefaultValues(final Map<String, ? extends Object> params)
	{
		final Set<Object> predefinedValues = new HashSet<Object>(getPredefinedValues(params, collectionEditor).values());
		for (final Object obj : model.getCollectionItems())
		{
			if (obj instanceof TypedObject)
			{
				final Object internalObj = ((TypedObject) obj).getObject();
				predefinedValues.add(internalObj);
			}
		}
		model.setCollectionItems(predefinedValues);
	}

	public Map<String, Object> getPredefinedValues(final Map<String, ? extends Object> parameters,
			final CollectionEditor collectionEditor)
	{
		final Map<String, Object> predefinedValue = new HashMap<String, Object>();
		final String predefinedValuesStrategy = (String) parameters.get(PREDEFINED_VALUES_STRATEGY);

		if (StringUtils.isNotBlank(predefinedValuesStrategy))
		{
			final PredefinedValuesStrategy strategy = (PredefinedValuesStrategy) SpringUtil.getBean(predefinedValuesStrategy);
			if (strategy != null)
			{
				predefinedValue.putAll(strategy.getPredefinedValues(getCreateContext(collectionEditor)));
			}
		}
		return predefinedValue;
	}

	public CreateContext getCreateContext(final CollectionEditor collectionEditor)
	{
		final ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService()
				.getObjectTemplate(getModel().getRootType().getCode());
		final CreateContext createContext = collectionEditor.getCreateContext() == null ? new CreateContext(
				objectTemplate.getBaseType(), null, (PropertyDescriptor) model.getSimpleReferenceSelectorModel().getParameters()
						.get(AbstractUIEditor.PROPERTY_DESCRIPTOR_PARAM), null) : collectionEditor.getCreateContext();
		return createContext;
	}
}
