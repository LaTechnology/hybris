//package com.hybris.addon.cockpits.components.liveedit;
//
//import de.hybris.platform.cmscockpit.components.liveedit.LiveEditView;
//import de.hybris.platform.cmscockpit.components.liveedit.RefreshContentHandler;
//
//import org.springframework.beans.factory.annotation.Required;
//
//import java.util.List;
//
//
///**
// * author: dariusz.malachowski
// */
//public class DefaultRefreshContentHandlerRegistry implements RefreshContentHandlerRegistry<LiveEditView>
//{
//
//	private List<RefreshContentHandler<LiveEditView>> handlers;
//
//	@Override
//	public List<RefreshContentHandler<LiveEditView>> getRefreshContentHandlers()
//	{
//		return handlers;
//	}
//
//	@Required
//	public void setHandlers(final List<RefreshContentHandler<LiveEditView>> handlers)
//	{
//		this.handlers = handlers;
//	}
//}
