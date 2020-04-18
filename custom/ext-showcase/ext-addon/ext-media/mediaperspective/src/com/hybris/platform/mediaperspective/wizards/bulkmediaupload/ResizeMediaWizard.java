/**
 * 
 */
package com.hybris.platform.mediaperspective.wizards.bulkmediaupload;

import java.util.Collections;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Window;

/**
 * @author mkostic
 *
 */
public class ResizeMediaWizard {
	
	public static final String WIZARD_ARG = "wizardModel";
	private final String componentURI = "/cmscockpit/resizeMedia.zul";
	
	private BulkMediaUploadViewModel viewModel;
	
	public ResizeMediaWizard(BulkMediaUploadViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	protected Window createWindow() {
		final Window ret = (Window) Executions.createComponents(getComponentURI(), null, Collections.singletonMap(WIZARD_ARG, viewModel));

		ret.applyProperties();
		new AnnotateDataBinder(ret).loadAll();
		return ret;
	}

	protected String getComponentURI() {
		return componentURI;
	}

	public void show() {
		final Window wizardWindow = createWindow();
		viewModel.fireInitEvents();

		wizardWindow.setPosition("center");
		wizardWindow.doHighlighted();
	}

}
