/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;

import com.hybris.platform.mediaperspective.wizards.bulkmediaupload.events.BulkMediaUploadModelListener;

import de.hybris.platform.core.model.media.MediaModel;


/**
 * @author mkostic
 *
 */
public class BulkMediaUploadEditorComposer extends GenericForwardComposer {
	
	private static final Logger LOG = Logger.getLogger(BulkMediaUploadEditorComposer.class);
	
	private BulkMediaUploadViewModel viewModel;
	private Button btnUpload;
	private Label zipUploadLabel;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		viewModel = (BulkMediaUploadViewModel) args.get(BulkMediaUploadWizard.WIZARD_ARG);
		
		btnUpload.addEventListener(Events.ON_CLICK, new EventListener() {
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				Media uploadedMedia = Fileupload.get(Labels.getLabel("uploadmediawizard.fileupload.tips"), null);
				if (uploadedMedia != null && uploadedMedia.getFormat().equals("zip"))
				{
					final UploadedMediaBulk mediaBulk = convertFromUploadedMedia(uploadedMedia);
					zipUploadLabel.setValue(mediaBulk.getName());
					zipUploadLabel.setSclass("zip-upload");
					viewModel.mediaBulkUploaded(mediaBulk);
				}
			}
		});
	}

	private UploadedMediaBulk convertFromUploadedMedia(final Media uploadedMedia)
	{
		ArrayList<UploadedMedia> medias = new ArrayList<UploadedMedia>();
		String name = uploadedMedia.getName();
		final ZipInputStream zipInputStream = new ZipInputStream(uploadedMedia.getStreamData());
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			IOUtils.copy(zipInputStream, output);
			IOUtils.closeQuietly(output);
		} catch (IOException e1) {
			LOG.error("An error ocured while extracting byte stream from ZIP entries!");
		}
		try
		{
			// This positions the stream at the beginning of an entry.
			ZipEntry nextEntry = zipInputStream.getNextEntry();
			while (nextEntry != null)
			{
				final ByteArrayOutputStream entryOutput = new ByteArrayOutputStream();
				IOUtils.copy(zipInputStream, entryOutput);
				UploadedMedia media = new UploadedMedia(nextEntry.getName(), entryOutput.toByteArray());
				medias.add(media);
				IOUtils.closeQuietly(entryOutput);
				
				nextEntry = zipInputStream.getNextEntry();
			}
		}
		catch (final Exception e)
		{
			LOG.error("An error ocured while extracting byte stream from ZIP entries!");
		}
		finally
		{
			IOUtils.closeQuietly(zipInputStream);
		}
		UploadedMediaBulk uploadedMediaBulk = new UploadedMediaBulk(name, output.toByteArray(), medias);
		return uploadedMediaBulk;
	}

}
