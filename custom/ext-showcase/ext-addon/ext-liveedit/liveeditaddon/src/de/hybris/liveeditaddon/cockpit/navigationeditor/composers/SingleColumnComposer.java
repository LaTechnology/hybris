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
package de.hybris.liveeditaddon.cockpit.navigationeditor.composers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import de.hybris.liveeditaddon.cockpit.navigationeditor.model.ColumnCollectionViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationColumnViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.model.NavigationLinkViewModel;
import de.hybris.liveeditaddon.cockpit.navigationeditor.wizard.NavigationEditorWizard;
import de.hybris.liveeditaddon.cockpit.util.NavigationPackHelper;


/**
 * 
 */
public class SingleColumnComposer extends GenericForwardComposer
{
	public static final String ALL_NAMES = "all_names";
	private Label columnLink;
	private Listbox columnList;
	private ColumnCollectionViewModel model;
	private NavigationColumnViewModel columnModel;

	private Image removeIcon;
	private Div columnContainer;

	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		model = (ColumnCollectionViewModel) args.get(NavigationEditorWizard.WIZARD_ARG);

		handleEvents(comp, columnLink);
		render(args);
	}

	private void handleEvents(final Component comp, final Label columnLink)
	{
		columnContainer.setAction("onmouseover:var actionDiv=this.getElementsByClassName('actionContainer')[0];"
				+ "actionDiv.style.visibility='visible';"
				+ "onmouseout:var actionDiv=this.getElementsByClassName('actionContainer')[0];"
				+ "actionDiv.style.visibility='hidden';");

		columnLink.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				model.setCurrentColumn(columnModel, columnLink);
			}
		});
		removeIcon.addEventListener(Events.ON_CLICK, new EventListener()
		{

			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				if (columnModel != null)
				{
					if (Messagebox.show(Labels.getLabel("general.confirmation"), Labels.getLabel("general.confirm"), Messagebox.YES
							| Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES)
					{
						columnModel.delete();
					}
				}
				comp.detach();
			}
		});

		//drop link into column
		columnList.addEventListener(Events.ON_DROP, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final DropEvent de = (DropEvent) arg0;

				NavigationLinkViewModel link = null;
				if (de.getDragged() instanceof Listitem)
				{
					final Listitem item = (Listitem) de.getDragged();
					link = (NavigationLinkViewModel) item.getValue();
				}
				if (de.getDragged() instanceof Treerow)
				{
					final Treerow treerow = (Treerow) de.getDragged();
					final Treeitem item = (Treeitem) treerow.getParent();
					link = (NavigationLinkViewModel) item.getValue();
					//we don't handle that on zscript, so we add Listitem here
					createNewListitemFromModelLink(link);
				}
				if (de.getDragged().getAttribute("link_value") != null)
				{
					link = (NavigationLinkViewModel) de.getDragged().getAttribute("link_value");
					//we don't handle that on zscript, so we add Listitem here
					NavigationPackHelper.cleanFacetValueName(link);
					createNewListitemFromModelLink(link);
				}

				link.setColumn(columnModel);
				handleActions(link);
			}

			private void handleActions(final NavigationLinkViewModel link)
			{
				if (columnModel != null && columnModel.getNavigationLinks() != null)
				{
					//remove dragged link from every column
					if (columnModel.getParentNavigationNode().getNavigationNodeColumns() != null)
					{
						for (final NavigationColumnViewModel column : columnModel.getParentNavigationNode().getNavigationNodeColumns())
						{
							if (column.getNavigationLinks().contains(link))
							{
								final LinkedList<NavigationLinkViewModel> tempLinks = new LinkedList<NavigationLinkViewModel>(column
										.getNavigationLinks());
								tempLinks.remove(link);
								column.setNavigationLinks(tempLinks);
							}
						}
					}
					if (!columnModel.getNavigationLinks().contains(link))
					{
						//add to current column
						final LinkedList<NavigationLinkViewModel> links = new LinkedList<NavigationLinkViewModel>(columnModel
								.getNavigationLinks());
						links.add(link);
						columnModel.setNavigationLinks(links);
					}
				}
			}
		});
		columnList.addEventListener(Events.ON_SELECT, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				model.setCurrentColumnLink(columnList.getSelectedItem());
			}
		});

		//dragging beetween columns
		columnContainer.addEventListener(Events.ON_DROP, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final DropEvent de = (DropEvent) arg0;
				if (de.getDragged() instanceof Div)
				{
					final Div dragged = (Div) de.getDragged();
					for (final NavigationColumnViewModel column : model.getNode().getNavigationNodeColumns())
					{
						if (column.getComponent().getChildren().contains(dragged))
						{
							//column is dragged model here
							final ArrayList<NavigationColumnViewModel> navigationNodeColumns = new ArrayList<NavigationColumnViewModel>(
									model.getNode().getNavigationNodeColumns());
							int thisColumn = navigationNodeColumns.indexOf(columnModel);
							--thisColumn;
							if (thisColumn < 0)
							{
								thisColumn = 0;
							}
							if (navigationNodeColumns.contains(column))
							{
								navigationNodeColumns.remove(column);
							}
							navigationNodeColumns.add(thisColumn, column);

							model.getNode().setNavigationNodeColumns(navigationNodeColumns);
						}
					}
				}
			}
		});
	}

	private void render(final Map<String, Object> args)
	{
		if (args.get(ColumnCollectionComposer.COLUMN_MODEL) == null)
		{
			return;
		}
		columnModel = (NavigationColumnViewModel) args.get(ColumnCollectionComposer.COLUMN_MODEL);

		if (columnModel.getName() != null)
		{
			columnLink.setValue("column '" + columnModel.getName() + "'");
			columnLink.setAttribute(ALL_NAMES, columnModel.getNames());
		}

		for (final NavigationLinkViewModel link : columnModel.getNavigationLinks())
		{
			createNewListitemFromModelLink(link);
		}
	}

	private void createNewListitemFromModelLink(final NavigationLinkViewModel link)
	{
		final Listitem item = new Listitem(link.getNames().get(getCurrentLanguageIsoCode()), link);
		item.setDraggable("link");
		item.setDroppable("link");
		columnList.appendChild(item);
		item.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				model.setCurrentColumnLink(item);
			}
		});
		//dragging between links in the same column
		item.addEventListener(Events.ON_DROP, new EventListener()
		{
			@Override
			public void onEvent(final Event arg0) throws Exception
			{
				final DropEvent de = (DropEvent) arg0;
				final Component parentOfDraggedComponent = de.getDragged().getParent().getParent();
				final Component parentOfTargetComponent = de.getTarget().getParent().getParent();

				//the same parent==the same column
				if (parentOfDraggedComponent.equals(parentOfTargetComponent))
				{
					if (parentOfDraggedComponent instanceof Div)
					{
						for (final NavigationColumnViewModel column : model.getNode().getNavigationNodeColumns())
						{
							if (column.getComponent().getChildren().contains(parentOfDraggedComponent))
							{
								insertBefore(de, column);
								redraw();
							}
						}
					}
				}
			}

			private void insertBefore(final DropEvent de, final NavigationColumnViewModel column)
			{
				final LinkedList<NavigationLinkViewModel> links = new LinkedList<NavigationLinkViewModel>(column.getNavigationLinks());

				final NavigationLinkViewModel dragged = getLinkModel(de.getDragged());
				final NavigationLinkViewModel target = getLinkModel(de.getTarget());
				int indexOfTarget = links.indexOf(target);
				if (indexOfTarget < 0)
				{
					indexOfTarget = 0;
				}
				links.remove(dragged);
				links.add(indexOfTarget, dragged);

				column.setNavigationLinks(links);
			}

			private void redraw()
			{
				columnList.getChildren().clear();
				for (final NavigationLinkViewModel link : columnModel.getNavigationLinks())
				{
					createNewListitemFromModelLink(link);
				}
			}

			private NavigationLinkViewModel getLinkModel(final Component comp)
			{
				final Listitem draggedItem = (Listitem) comp;
				return (NavigationLinkViewModel) draggedItem.getValue();
			}
		});
	}

	private String getCurrentLanguageIsoCode()
	{
		return columnModel.getParentNavigationNode().getLiveEditView().getCurrentPreviewData().getLanguage().getIsocode();
	}
}