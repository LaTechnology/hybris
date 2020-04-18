/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;

import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.events.BulkMediaUploadModelListener;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.impl.AbstractReferenceUIEditor;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceUIEditor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;


/**
 * @author mkostic
 *
 */
public class EditMediaComposer extends GenericForwardComposer {
	
	private static final Logger LOG = Logger.getLogger(EditMediaComposer.class);
	
	private Div catalogVersionContainer;
	private Div mediaFormatContainer;
	private Div mediaContainerContainer;
	private BulkMediaUploadViewModel viewModel;
	private AbstractReferenceUIEditor catalogVersionEditor;
	private AbstractReferenceUIEditor mediaFormatEditor;
	private AbstractReferenceUIEditor mediaContainerEditor;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		viewModel = (BulkMediaUploadViewModel) args.get(BulkMediaUploadWizard.WIZARD_ARG);
		
		TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
		composeCatalogVersionContainer(typeService);
		composeMediaFormatContainer(typeService);
		composeMediaContainerContainer(typeService);
	}

	private void composeMediaContainerContainer(TypeService typeService) {
		mediaContainerEditor = new DefaultSimpleReferenceUIEditor(typeService.getObjectType(MediaContainerModel._TYPECODE));
		Map<String, Object> parameters = new HashMap<>();
		PropertyDescriptor catalogVersionPropertyDescriptor = typeService.getPropertyDescriptor("MediaContainer.catalogVersion");
		Map<PropertyDescriptor, Object> propertyDescriptors = new HashMap<>();
		propertyDescriptors.put(catalogVersionPropertyDescriptor, typeService.wrapItem(viewModel.getSelectedCatalogVersionModel()));
		parameters.put("predefinedPropertyValues", propertyDescriptors);
		final Component mediaContainerComponent = mediaContainerEditor.createViewComponent(typeService.wrapItem(viewModel.getSelectedMediaContainer()),
				parameters, new EditorListener(){
			
			@Override
			public void actionPerformed(String arg0) {
				//
			}
			
			@Override
			public void valueChanged(Object value) {
				if (value != null)
				{
					if (value instanceof TypedObject)
					{
						viewModel.mediaContainerChanged((MediaContainerModel) ((TypedObject) value).getObject());
					}
					else
					{
						throw new UnsupportedOperationException("Can't cast value to TypedObject");
					}
				}
				else
				{
					viewModel.mediaContainerChanged(null);
				}
			}
			
		});
		mediaContainerComponent.setParent(mediaContainerContainer);
	}


	private void composeMediaFormatContainer(TypeService typeService) {
		mediaFormatEditor = new DefaultSimpleReferenceUIEditor(typeService.getObjectType(MediaFormatModel._TYPECODE));
		final Component mediaFormatComponent = mediaFormatEditor.createViewComponent(typeService.wrapItem(viewModel.getSelectedMediaFormat()),
				Collections.EMPTY_MAP, new EditorListener(){
			
			@Override
			public void actionPerformed(String arg0) {
				//
			}
			
			@Override
			public void valueChanged(Object value) {
				if (value != null)
				{
					if (value instanceof TypedObject)
					{
						viewModel.mediaFormatChanged((MediaFormatModel) ((TypedObject) value).getObject());
					}
					else
					{
						throw new UnsupportedOperationException("Can't cast value to TypedObject");
					}
				}
				else
				{
					viewModel.mediaFormatChanged(null);
				}
			}
			
		});
		mediaFormatComponent.setParent(mediaFormatContainer);
		
	}

	protected void composeCatalogVersionContainer(TypeService typeService) {
		catalogVersionEditor = new DefaultSimpleReferenceUIEditor(
				typeService.getObjectType(CatalogVersionModel._TYPECODE));
		final Component catalogVersionComponent = catalogVersionEditor.createViewComponent(typeService.wrapItem(viewModel.getSelectedCatalogVersionModel()),
				Collections.EMPTY_MAP, new EditorListener()
				{
					@Override
					public void valueChanged(final Object value)
					{
						if (value != null)
						{
							if (value instanceof TypedObject)
							{
								viewModel.catalogVersionChanged((CatalogVersionModel) ((TypedObject) value).getObject());
							}
							else
							{
								throw new UnsupportedOperationException("Can't cast value to TypedObject");
							}
						}
						else
						{
							viewModel.catalogVersionChanged(null);
						}
					}

					@Override
					public void actionPerformed(final String actionCode)
					{
						//
					}
				});
		catalogVersionComponent.setParent(catalogVersionContainer);
	}
}
