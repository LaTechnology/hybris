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
package com.hybris.addon.cmscockpits.components.editor.impl;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.impl.AbstractReferenceUIEditor;
import de.hybris.platform.cockpit.model.referenceeditor.impl.DefaultReferenceCollectionEditorModel;
import de.hybris.platform.cockpit.model.referenceeditor.impl.ReferenceCollectionEditorController;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;


/**
 * This is not final version of this editor!
 *
 * @author karol.walczak
 *
 */
public class CategoryVersionRadioEditor extends AbstractReferenceUIEditor
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(CategoryVersionRadioEditor.class);
	private ObjectType rootType;
	private ObjectType rootSearchType;

	private final DefaultReferenceCollectionEditorModel model;
	private CategoryVersionCheckboxComponent checkBoxComponent;
	private ReferenceCollectionEditorController collectionController;

	@SuppressWarnings("unused")
	//private Page page;
	public CategoryVersionRadioEditor()
	{
		this(null);
	}

	public CategoryVersionRadioEditor(final ObjectType rootType)
	{
		super();
		this.model = new DefaultReferenceCollectionEditorModel(rootType);
	}

	@Override
	public void setRootType(final ObjectType rootType)
	{
		if ((this.rootType == null && rootType != null) || (this.rootType != null && !this.rootType.equals(rootType)))
		{
			this.rootType = rootType;
			this.model.setRootType(rootType);
		}
	}

	@Override
	public void setRootSearchType(final ObjectType rootSearchType)
	{
		if ((this.rootSearchType == null && rootSearchType != null)
				|| (this.rootSearchType != null && !this.rootType.equals(rootSearchType)))
		{
			this.rootSearchType = rootSearchType;
		}
		this.model.setRootSearchType(this.rootSearchType);
	}

	@Override
	public HtmlBasedComponent createViewComponent(final Object initialValue, final Map<String, ? extends Object> parameters,
			final EditorListener listener)
	{
		final Object additionalListenerParam = parameters.get(AdditionalReferenceEditorListener.class.getName());
		AdditionalReferenceEditorListener additionalListener = null;
		if (additionalListenerParam instanceof AdditionalReferenceEditorListener)
		{
			additionalListener = (AdditionalReferenceEditorListener) additionalListenerParam;
		}

		model.setParameters(parameters);

		checkBoxComponent = new CategoryVersionCheckboxComponent(listener, additionalListener)
		{
			@Override
			public void saveItems(final java.util.List<TypedObject> items)
			{
				listener.valueChanged(items);
				listener.actionPerformed(EditorListener.FORCE_SAVE_CLICKED);
			}
		};

		checkBoxComponent.setDisabled(!isEditable());
		checkBoxComponent.setAllowCreate(isAllowCreate());


		final Object createContext = parameters.get("createContext");
		if (createContext instanceof CreateContext)
		{
			checkBoxComponent.setCreateContext((CreateContext) createContext);
		}


		if (initialValue != null)
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


						checkBoxComponent.setSelectedElements(new ArrayList<TypedObject>((Collection) initialValue));
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
		else
		{
			model.setCollectionItems(Collections.EMPTY_LIST);
		}
		if (parameters.containsKey("availableItems"))
		{

			final Collection<CatalogModel> available = new ArrayList<CatalogModel>();
			available.addAll((Collection<CatalogModel>) parameters.get("availableItems"));
			model.setCollectionItems(new ArrayList<Object>(UISessionUtils.getCurrentSession().getTypeService().wrapItems(available)));

		}
		else
		{

			model.setCollectionItems(Collections.EMPTY_LIST);
		}



		if (UISessionUtils.getCurrentSession().isUsingTestIDs())
		{
			String id = "ReferenceSelector_";
			String attQual = (String) parameters.get(AbstractUIEditor.ATTRIBUTE_QUALIFIER_PARAM);
			if (attQual != null)
			{
				attQual = attQual.replaceAll("\\W", "");
				id = id + attQual;
			}
			UITools.applyTestID(checkBoxComponent, id);
		}
		/*---------create test controller for Reference Selector---------- */
		if (collectionController != null)
		{
			collectionController.unregisterListeners();
		}
		collectionController = new ReferenceCollectionEditorController(checkBoxComponent, model, listener, rootType);
		collectionController.initialize();
		/*---------------------------------------------------------------- */

		checkBoxComponent.setModel(model);
		final CancelButtonContainer cancelButtonContainer = new CancelButtonContainer(listener, new CancelListener()
		{
			public void cancelPressed()
			{
				checkBoxComponent.fireCancel();
			}
		});
		checkBoxComponent.addEventCollectionEditorListener(Events.ON_FOCUS, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				cancelButtonContainer.showButton(Boolean.TRUE.booleanValue());
			}
		});
		checkBoxComponent.addEventCollectionEditorListener(Events.ON_CLOSE, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				cancelButtonContainer.showButton(Boolean.FALSE.booleanValue());
			}
		});
		cancelButtonContainer.setContent(checkBoxComponent);

		//		if (UISessionUtils.getCurrentSession().isUsingTestIDs())
		//		{
		//			String id = "ReferenceSelector_";
		//			String attQual = (String) parameters.get("attributeQualifier");
		//			if (attQual != null)
		//			{
		//				attQual = attQual.replaceAll("\\W", "");
		//				id = id + attQual;
		//			}
		//			UITools.applyTestID(collectionEditor, id);
		//		}
		return cancelButtonContainer;
	}

	@Override
	public Object getValue()
	{
		return model.getCollectionItems();
	}

	@Override
	public boolean isInline()
	{
		return true;
	}

	@Override
	public void setValue(final Object value)
	{
		this.model.setCollectionItems((List<Object>) value);
	}

	@Override
	public void setFocus(final HtmlBasedComponent rootEditorComponent, final boolean selectAll)
	{
		//todo pp (05.05.2009) : implement
	}

	@Override
	public ObjectType getRootSearchType()
	{
		return this.model.getRootSearchType();
	}

	@Override
	public ObjectType getRootType()
	{
		return this.model.getRootType();
	}

	protected DefaultReferenceCollectionEditorModel getModel()
	{
		return this.model;
	}
}
