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
public class BulkMediaUploadWindowComposer extends GenericForwardComposer {
	
	private static final Logger LOG = Logger.getLogger(BulkMediaUploadWindowComposer.class);
	
	private Component componentWindow;
	private BulkMediaUploadViewModel viewModel;
	private Button btnDone;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		viewModel = (BulkMediaUploadViewModel) args.get(BulkMediaUploadWizard.WIZARD_ARG);
		viewModel.registerListener(getModelListener());
		componentWindow = comp;
	}

	
	private BulkMediaUploadModelListener getModelListener() {
		return new BulkMediaUploadModelListener() {
			
			@Override
			public void onChange() {
				btnDone.setVisible(viewModel.isValid());
			}

			@Override
			public void onLoad() {
				//
			}

			@Override
			public void onSave(List<MediaModel> mediaModels) {
				//
			}
		};
	}
	
	public void onClick$btnCancel(final ForwardEvent event)
	{
		Events.postEvent(new Event("onClose", componentWindow, null));
	}
	
	public void onClick$btnDone(final ForwardEvent event)
	{
		if (viewModel.save()) {
			Events.postEvent(new Event("onClose", componentWindow, null));
		}
	}

}
