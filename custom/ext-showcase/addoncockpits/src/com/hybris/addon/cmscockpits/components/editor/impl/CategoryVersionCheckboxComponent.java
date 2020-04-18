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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.impl.ReferenceCollectionEditor;
import de.hybris.platform.cockpit.session.UISessionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;


/**
 * @author karol.walczak
 *
 */
public class CategoryVersionCheckboxComponent extends ReferenceCollectionEditor
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CategoryVersionCheckboxComponent.class);
	private List<TypedObject> selectedElements = new ArrayList<TypedObject>();



	/**
	 * @return the selectedElementshy
	 */
	public List<TypedObject> getSelectedElements()
	{
		return selectedElements;
	}



	/**
	 * @param selectedElements
	 *           the selectedElements to set
	 */
	public void setSelectedElements(final List<TypedObject> selectedElements)
	{
		this.selectedElements = selectedElements;
	}



	public CategoryVersionCheckboxComponent(final EditorListener editorListener,
			final AdditionalReferenceEditorListener additionalListener)
	{
		super(editorListener, additionalListener);
	}

	public void saveItems(final List<TypedObject> items)
	{

		//nop
	}

	@Override
	protected ListitemRenderer createCollectionItemListRenderer()
	{
		return new ListitemRenderer()
		{
			@Override
			public void render(final Listitem itemRow, final Object value) throws Exception //NOPMD: ZK specific
			{
				if (value == null || (value instanceof Collection && ((Collection) value).isEmpty()))
				{
					return;
				}
				else
				{
					final Listcell cellItem = new Listcell();
					cellItem.setDroppable(DROP_DRAG_ID);
					cellItem.setDraggable(DROP_DRAG_ID);
					final Radiogroup versionRadioGroup = new Radiogroup();
					versionRadioGroup.addEventListener(Events.ON_CHECK, new EventListener()
					{

						@Override
						public void onEvent(final Event event) throws Exception
						{
							// YTODO Auto-generated method stub
							if (event instanceof CheckEvent)
							{

								//final CheckEvent checkEvent = (CheckEvent) event;
								//nop
							}

							if (event.getTarget() instanceof Radio)
							{
								final TypedObject value = (TypedObject) ((Radio) event.getTarget()).getAttribute("value");
								final Radiogroup radioGroup = ((Radio) event.getTarget()).getRadiogroup();
								for (final Object radio : radioGroup.getChildren())
								{

									if (radio instanceof Radio && ((Radio) radio).getAttribute("value") != null)
									{

										//nop
										selectedElements.remove(((Radio) radio).getAttribute("value"));

									}
								}
								if (selectedElements.contains(value))
								{
									selectedElements.remove(value);
								}
								else
								{
									selectedElements.add(value);
								}

								saveItems(new ArrayList<TypedObject>(getSelectedElements()));
							}

						}
					});
					versionRadioGroup.setOrient("vertical");
					final TypedObject wrappedCatalog = (TypedObject) value;
					final CatalogModel catalogModel = (CatalogModel) wrappedCatalog.getObject();
					for (final CatalogVersionModel catalogVersionModel : catalogModel.getCatalogVersions())
					{

						final TypedObject wrappedCatalogVersion = UISessionUtils.getCurrentSession().getTypeService()
								.wrapItem(catalogVersionModel);
						final Radio singleRadio = new Radio(UISessionUtils.getCurrentSession().getLabelService()
								.getObjectTextLabel(wrappedCatalogVersion));
						singleRadio.setAttribute("value", wrappedCatalogVersion);
						if (getSelectedElements().contains(wrappedCatalogVersion))
						{
							singleRadio.setSelected(true);
						}
						versionRadioGroup.appendChild(singleRadio);

					}
					cellItem.appendChild(versionRadioGroup);
					cellItem.setSclass("selectedCellItem");
					itemRow.appendChild(cellItem);
				}
			}
		};
	}



	@Override
	public boolean initialize()
	{
		final boolean ret = super.initialize();
		referenceSelector.setVisible(false);
		setSclass("categoryCheckboxChooser");
		//hyfinal Component parent = referenceSelector.getParent();

		final Button saveBtn = new Button(Labels.getLabel("general.save"));
		saveBtn.addEventListener(Events.ON_CLICK, new EventListener()
		{

			@Override
			public void onEvent(final Event event) throws Exception
			{
				// YTODO Auto-generated method stub
				saveItems(new ArrayList((getSelectedElements())));
			}
		}); //disabled right now

		//		if (!getModel().getCollectionItems().isEmpty() && false)
		//		{
		//			saveBtn.setParent(parent);
		//		}
		return ret;

	}
}
