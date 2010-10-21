package lt.walrus.ajax;

import lt.walrus.command.AddSlideCommand;
import lt.walrus.command.DeleteSlideCommand;
import lt.walrus.model.SlideshowBox;
import lt.walrus.service.BoxService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class SlideActionsHandler extends AbstractWalrusAjaxHandler {
	@Autowired
	private BoxService boxService;

	public AjaxResponse newSlide(AjaxEvent e) {
		AjaxResponse ret = new AjaxResponseImpl("UTF-8");
		
		String title = e.getParameters().get("title");
		if (! StringUtils.hasText(title)) {
			ret = makeErrorResponse("Nenurodytas skaidrės pavadinimas!");
			ret.addAction(new ExecuteJavascriptFunctionAction("commandExecuted", null));
		} else {
			
			String slideshowId = e.getParameters().get("slideshowId");
			if (! StringUtils.hasText(slideshowId)) {
				ret = makeErrorResponse("Nesuprantu, koks čia slideshow!");
			} else {
				SlideshowBox slideshow = (SlideshowBox) getSite(e).getBox(slideshowId);
				if (null == slideshow) {
					ret = makeErrorResponse("Slideshow '" + slideshowId + "' nerastas!");
				} else {
					ret = commandManager.execute(new AddSlideCommand(boxService, slideshow, title));
				}
			}
		}
		
		return ret;
	}
	
	public AjaxResponse deleteSlide(AjaxEvent e) {
		AjaxResponse ret = new AjaxResponseImpl("UTF-8");
		
		String slideId = e.getParameters().get("slideId");
		if (! StringUtils.hasText("slideId")) {
			ret = makeErrorResponse("Nenurodyta skaidrė");
		} else {
			String slideshowId = e.getParameters().get("slideshow");
			if (! StringUtils.hasText(slideshowId)) {
				ret = makeErrorResponse("Nesuprantu, koks čia slideshow!");
			} else {
				SlideshowBox slideshow = (SlideshowBox) getSite(e).getBox(slideshowId);
				if (null == slideshow) {
					ret = makeErrorResponse("Slideshow '" + slideshowId + "' nerastas!");
				} else {
					ret = commandManager.execute(new DeleteSlideCommand(boxService, slideshow, Long.valueOf(slideId)));
				}
			}
		}
		
		return ret;
	}

	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}

	public BoxService getBoxService() {
		return boxService;
	}
}
