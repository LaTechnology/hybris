/**
 *
 */
package com.hybris.addon.cmscockpits.components.editor.impl;

import de.hybris.platform.cockpit.model.editor.AdditionalReferenceEditorListener;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.collection.CollectionEditor;
import de.hybris.platform.cockpit.session.UISessionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;


/**
 * @author c.stefanache
 *
 */
public class CheckBoxCollectionEditor extends CollectionEditor
{
	private List<TypedObject> selectedElements = new ArrayList<TypedObject>();
	private static final Logger LOG = Logger.getLogger(CheckBoxCollectionEditor.class);

	public CheckBoxCollectionEditor(final EditorListener editorListener, final AdditionalReferenceEditorListener additionalListener)
	{
		super(editorListener, additionalListener);
	}

	public List<TypedObject> getSelectedElements()
	{
		return selectedElements;
	}

	public void setSelectedElements(final List<TypedObject> selectedElements)
	{
		this.selectedElements = selectedElements;
	}

	public void saveItems(final List<TypedObject> items)
	{
		editorListener.valueChanged(items);
		editorListener.actionPerformed(EditorListener.FORCE_SAVE_CLICKED);
	}

	@Override
	protected ListitemRenderer createCollectionItemListRenderer()
	{
		return new ListitemRenderer()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void render(final Listitem itemRow, final Object value) throws Exception //NOPMD: ZK specific
			{
				if (value == null || (value instanceof Collection && ((Collection) value).isEmpty()))
				{

					return;
				}

				final Listcell cellItem = new Listcell();
				cellItem.addEventListener(Events.ON_DROP, new EventListener()
				{
					@Override
					public void onEvent(final Event event) throws Exception //NOPMD: ZK specific
					{
						final Component dragged = ((DropEvent) event).getDragged();
						final int fromIndex = ((Listitem) ((Listcell) dragged).getParent()).getIndex();
						final int toIndex = ((Listitem) ((Listcell) event.getTarget()).getParent()).getIndex();
						LOG.info("[Reference Collection Editor] Fire move item ( from:" + fromIndex + " to:" + toIndex + " )");
						fireMoveCollectionItem(fromIndex, toIndex);
					}
				});

				final Label selectedLabel = new Label();
				selectedLabel.setSclass("selectedItemLabel");
				selectedLabel.setValue(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel((TypedObject) value));


				final Checkbox checkbox = new Checkbox();
				checkbox.setAttribute("value", value);
				checkbox.setChecked(getSelectedElements().contains(value));
				checkbox.addEventListener(Events.ON_CHECK, new EventListener()
				{
					@Override
					public void onEvent(final Event event) throws Exception
					{
						if (event.getTarget() instanceof Checkbox)
						{
							final TypedObject value = (TypedObject) ((Checkbox) event.getTarget()).getAttribute("value");
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

				final Hbox horizontalContatiner = new Hbox();
				horizontalContatiner.appendChild(checkbox);
				horizontalContatiner.appendChild(selectedLabel);

				cellItem.appendChild(horizontalContatiner);
				cellItem.setSclass("selectedCellItem");
				itemRow.appendChild(cellItem);
				cellItem.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener()
				{
					@Override
					public void onEvent(final Event event) throws Exception
					{
						if (value instanceof TypedObject)
						{
							doCollectionItemDoubleClicked((TypedObject) value);
						}
					}
				});
			}
		};
	}

}
