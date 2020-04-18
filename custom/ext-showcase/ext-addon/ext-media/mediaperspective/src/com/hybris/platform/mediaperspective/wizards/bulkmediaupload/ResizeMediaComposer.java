/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listgroup;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleGroupsModel;
import org.zkoss.zul.Textbox;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionGroupModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;


/**
 * @author mkostic
 *
 */
public class ResizeMediaComposer extends GenericForwardComposer {
	
	private static final Logger LOG = Logger.getLogger(ResizeMediaComposer.class);
	
	private BulkMediaUploadViewModel viewModel;
	private Listbox conversionGroupListBox;
	private Div selectedMediaContainer;
	private Image mediaImage;
	private Div containerCodeEditor;
	private Textbox containerCodeInput;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		viewModel = (BulkMediaUploadViewModel) args.get(BulkMediaUploadWizard.WIZARD_ARG);
		composeMediaConversionGroups();
		MediaModel selectedMedia = viewModel.getSelectedMedia();
		if (selectedMedia != null) {
			String mediaUrl = selectedMedia.getURL();
			mediaImage.setSrc("~" + mediaUrl);
			mediaImage.setParent(selectedMediaContainer);
			mediaImage.setVisible(true);
			containerCodeInput.addEventListener(Events.ON_CHANGE, new EventListener() {
				
				@Override
				public void onEvent(Event arg0) throws Exception {
					viewModel.resizedMediaContainerCodeChanged(containerCodeInput.getValue());
				}
			});
			containerCodeInput.setParent(containerCodeEditor);
		}
	}

	private void composeMediaConversionGroups() {
		List<ConversionGroupModel> existingConversionGroupModels = viewModel.getExistingConversionGroupModels();
		List<ConversionMediaFormatModel[]> lists = new ArrayList<>();
		ConversionGroupModel[] heads = new ConversionGroupModel[existingConversionGroupModels.size()];
		for (int i = 0; i < existingConversionGroupModels.size(); i++) {
			ConversionGroupModel conversionGroupModel = existingConversionGroupModels.get(i);
			Set<ConversionMediaFormatModel> supportedFormats = conversionGroupModel.getSupportedFormats();
			lists.add(supportedFormats.toArray(new ConversionMediaFormatModel[supportedFormats.size()]));
			heads[i] = conversionGroupModel;
		}
		ConversionMediaFormatModel[][] conversionGroups = lists.toArray( new ConversionMediaFormatModel[lists.size()][]);
		
		SimpleGroupsModel groupsModel = new SimpleGroupsModel(conversionGroups, heads);
		conversionGroupListBox.setModel(groupsModel);
		conversionGroupListBox.setItemRenderer(getListItemRenderer());
		
		List items = conversionGroupListBox.getItems();
		for (Object item : items) {
			
			if (item instanceof Listgroup) {
				Listgroup listgroup = (Listgroup) item;
				listgroup.addEventListener(Events.ON_CLICK, new EventListener() {
					
					@Override
					public void onEvent(Event event) throws Exception {
						Component target = event.getTarget();
						if (target instanceof Listgroup) {
							Listgroup listgroup = (Listgroup) target;
							List<Listitem> items = listgroup.getItems();
							boolean selected = listgroup.isSelected();
							for (Listitem listitem : items) {
								if (selected) {
									listgroup.getListbox().addItemToSelection(listitem);
									viewModel.addConversionFormatToSelection((ConversionMediaFormatModel)listitem.getValue());
								} else {
									listgroup.getListbox().removeItemFromSelection(listitem);
									viewModel.removeConversionFormatToSelection((ConversionMediaFormatModel)listitem.getValue());
								}
							}
						}
					}
				});
			} else if (item instanceof Listitem) {
				Listitem listitem = (Listitem) item;
				listitem.addEventListener(Events.ON_CLICK, new EventListener() {
					
					@Override
					public void onEvent(Event event) throws Exception {
						Listitem listitem = (Listitem) event.getTarget();
						boolean selected = listitem.isSelected();
						if (selected) {
							viewModel.addConversionFormatToSelection((ConversionMediaFormatModel)listitem.getValue());
						} else {
							viewModel.removeConversionFormatToSelection((ConversionMediaFormatModel)listitem.getValue());
						}
					}
				});
			}
			
		}
	}

	private ListitemRenderer getListItemRenderer() {
		return new ListitemRenderer() {
			
			@Override
			public void render(Listitem paramListitem, Object paramObject)
					throws Exception {
				if (paramListitem instanceof Listgroup) {
					ConversionGroupModel conversionGroupModel = (ConversionGroupModel) paramObject;
					String name = conversionGroupModel.getName();
					String code = conversionGroupModel.getCode();
					paramListitem.appendChild(new Listcell(StringUtils.isEmpty(name) ? code : name));
					paramListitem.setValue(paramObject);
					paramListitem.setSclass("conversionGroupListBoxGroup");
				} else {
					ConversionMediaFormatModel mediaFormatModel = (ConversionMediaFormatModel) paramObject;
					String name = mediaFormatModel.getName();
					String qualifier = mediaFormatModel.getQualifier();
					paramListitem.appendChild(new Listcell(StringUtils.isEmpty(name) ? qualifier : name));
					paramListitem.setValue(paramObject);
					paramListitem.setSclass("conversionGroupListBoxItem");
				}
			}
		};
	}

}
