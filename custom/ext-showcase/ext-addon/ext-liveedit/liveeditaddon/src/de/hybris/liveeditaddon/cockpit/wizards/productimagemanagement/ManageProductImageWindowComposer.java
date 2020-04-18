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
package de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.impl.AbstractReferenceUIEditor;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceUIEditor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.liveeditaddon.cockpit.service.CockpitImageMediaService;
import de.hybris.liveeditaddon.cockpit.wizards.components.ImageTile;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.CockpiResizeableImageDropEventListener;
import de.hybris.liveeditaddon.cockpit.wizards.productimagemanagement.events.CockpitFormatImageDropEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;


/**
 * 
 */
public class ManageProductImageWindowComposer extends GenericForwardComposer
{
	private final String AUTO_RESIZE_FORMAT = "AutoResizeDrop";
	private final String AUTO_RESIZE_LABEL = "autoResizeDrop";

	private Window manageProductPictureWindow;
	private Component window;
	private Div primaryImage;
	private Div galleryImages;
	private Div autoResizeDropContainer;
	private Component btnDone;
	private Div mediaFormatContainer;
	private ImageTile autoResizeDrop;
	private Div mediaContainerContainer;
	private AbstractReferenceUIEditor mediaContainerEditor;

	private ProductImageManagementViewModel model;

	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		super.doAfterCompose(comp);

		window = comp;
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		model = (ProductImageManagementViewModel) args.get(ProductImageWizard.WIZARD_ARG);
		model.setWindowTitle(manageProductPictureWindow);
		model.composeMainPicture(primaryImage);
		model.composePictureGallery(galleryImages);
		model.setButton(btnDone);

		buildImageTiles(model.getMediaFormats());

		if (model.isResizePluginIncluded())
		{
			buildAutoResizeImageTile();
		}
		final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
		composeMediaContainerContainer(typeService);

	}

	private void buildImageTiles(final Map<String, String> mediaFormatMap)
	{
		final Iterator it = mediaFormatMap.entrySet().iterator();
		while (it.hasNext())
		{
			final Map.Entry<String, String> entry = (Map.Entry) it.next();
			final ImageTile imageTile = new ImageTile(entry.getKey(), entry.getValue());
			imageTile.addEventListener(Events.ON_CLICK, new CockpitFormatImageDropEventListener((CockpitImageMediaService) model));
			imageTile.setParent(mediaFormatContainer);
			model.registerTile(imageTile);
		}
	}

	private void buildAutoResizeImageTile()
	{
		autoResizeDropContainer.setVisible(model.isResizePluginIncluded());
		autoResizeDrop = new ImageTile(AUTO_RESIZE_LABEL, AUTO_RESIZE_FORMAT);
		autoResizeDrop.addEventListener(Events.ON_CLICK, new CockpiResizeableImageDropEventListener(
				(CockpitImageMediaService) model));
		autoResizeDrop.setParent(autoResizeDropContainer);
	}

	@SuppressWarnings("PMD")
	public void onClick$btnDone(final ForwardEvent event)
	{
		model.publish();
		Events.postEvent(new Event("onClose", window, null));
	}

	public void onClick$btnSave(final ForwardEvent event)
	{
		model.save();
	}

	private void composeMediaContainerContainer(final TypeService typeService)
	{
		mediaContainerEditor = new DefaultSimpleReferenceUIEditor(typeService.getObjectType(MediaContainerModel._TYPECODE));
		final Map<String, Object> parameters = new HashMap<>();
		final PropertyDescriptor catalogVersionPropertyDescriptor = typeService
				.getPropertyDescriptor("MediaContainer.catalogVersion");
		final Map<PropertyDescriptor, Object> propertyDescriptors = new HashMap<>();
		propertyDescriptors.put(catalogVersionPropertyDescriptor, typeService.wrapItem(model.getSelectedCatalogVersionModel()));
		parameters.put("predefinedPropertyValues", propertyDescriptors);
		final Component mediaContainerComponent = mediaContainerEditor.createViewComponent(typeService.wrapItem(null), parameters,
				new EditorListener()
				{

					@Override
					public void actionPerformed(final String arg0)
					{
						//
					}

					@Override
					public void valueChanged(final Object value)
					{
						if (value != null)
						{
							if (value instanceof TypedObject)
							{
								model.mediaContainerChanged((MediaContainerModel) ((TypedObject) value).getObject());
							}
							else
							{
								throw new UnsupportedOperationException("Can't cast value to TypedObject");
							}
						}
						else
						{
							model.mediaContainerChanged(null);
						}
					}

				});
		mediaContainerComponent.setParent(mediaContainerContainer);
	}
}
