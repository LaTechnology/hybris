/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Footer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listgroup;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleGroupsModel;

import com.hybris.mediatags.model.MediaTagModel;
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
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;


/**
 * @author mkostic
 *
 */
public class TagMediaComposer extends GenericForwardComposer {
	
	private static final Logger LOG = Logger.getLogger(TagMediaComposer.class);
	
	private BulkMediaUploadViewModel viewModel;
	private Grid addMediaTagsGrid;
	private Footer newTagFooterMedia;
	private AbstractReferenceUIEditor mediaTagEditor;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		viewModel = (BulkMediaUploadViewModel) args.get(BulkMediaUploadWizard.WIZARD_ARG);
		viewModel.registerListener(getModelListener());
		
		TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
		composeMediaTagContainer(typeService);
	}

	private void composeMediaTagContainer(TypeService typeService) {
		mediaTagEditor = new DefaultSimpleReferenceUIEditor(typeService.getObjectType(MediaTagModel._TYPECODE));
		final Component mediaTagComponent = mediaTagEditor.createViewComponent(typeService.wrapItem(null),
				Collections.EMPTY_MAP, new EditorListener(){

					@Override
					public void actionPerformed(String arg0) {
						//
					}

					@Override
					public void valueChanged(Object value) {
						//
					}
			
		});
		mediaTagComponent.setParent(newTagFooterMedia);
	}

	private BulkMediaUploadModelListener getModelListener() {
		return new BulkMediaUploadModelListener() {
			
			@Override
			public void onChange() {
				addMediaTagsGrid.setModel(new ListModelList(viewModel.getExistingMediaTags()));
			}

			@Override
			public void onLoad() {
				addMediaTagsGrid.setModel(new ListModelList(viewModel.getExistingMediaTags()));
				addMediaTagsGrid.setRowRenderer(getRowRenderer());
			}

			@Override
			public void onSave(List<MediaModel> mediaModels) {
				//
			}
		};
	}
	
	protected RowRenderer getRowRenderer()
	{
		return new RowRenderer()
		{
			@Override
			public void render(final Row row, final Object data) throws Exception
			{
				final MediaTagModel val = (MediaTagModel) data;
				row.setValue(val);

				final Label tagText = new Label();
				tagText.setParent(row);
				tagText.setValue(val.getCode() + " - " + val.getDescription());

			}
		};
	}

	public void onClick$newTagButton(final ForwardEvent event)
	{
		addNewMediaTag();
	}
	
	private void addNewMediaTag()
	{
		final Object referenceEditorValue = mediaTagEditor.getValue();
		if (referenceEditorValue != null)
		{
			final Object valueObject = (referenceEditorValue instanceof TypedObject) ? ((TypedObject) referenceEditorValue)
					.getObject() : referenceEditorValue;
			viewModel.mediaTagAdded((MediaTagModel) valueObject);
			mediaTagEditor.setValue(null);
		}
		addMediaTagsGrid.getPaginal().setActivePage(addMediaTagsGrid.getPageCount() - 1);
	}

}
