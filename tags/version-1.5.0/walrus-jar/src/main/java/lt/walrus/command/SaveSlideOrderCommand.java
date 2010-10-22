package lt.walrus.command;

import java.util.HashMap;

import lt.walrus.model.Slide;
import lt.walrus.model.SlideshowBox;
import lt.walrus.service.BoxService;
import lt.walrus.service.SlideService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class SaveSlideOrderCommand extends SaveSlideBodyCommand {
	private static final long serialVersionUID = -8871951549778478975L;

	private SlideshowBox slideshow;
	private Slide partner;

	private BoxService boxService;
	
	public SaveSlideOrderCommand(SlideService slideService, BoxService boxService, SlideshowBox slideshow1, Slide context1, String text) {
		super(slideService, context1, text);
		slideshow = slideshow1;
		this.boxService = boxService;
	}

	protected String getPreviousValueFromContext(Slide context1) {
		return String.valueOf(context1.getOrderno());
	}

	@Override
	protected void setValueToContext(String val) {
		((Slide) context).setOrderno(Integer.parseInt(val));
	}
	
	@Override
	protected String getFieldName() {
		return "orderno";
	}
	
	@Override
	public AjaxResponse execute() {
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		
		int direction = Integer.parseInt(newValue);
		
		if (-1 == direction) {
			partner = slideshow.getPrevious((Slide) context);
		} else {
			partner = slideshow.getNext((Slide) context);
		}

		logger.debug("REORDER SLIDES: DIR: " + direction + " current: " + context + " partner: " + partner);
		
		int orderno = partner.getOrderno();
		partner.setOrderno(((Slide) context).getOrderno());
		((Slide) context).setOrderno(orderno);

		slideshow.sortSlides();
		boxService.save(slideshow);
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("slideshowBoxId", slideshow.getBoxId());
		r.addAction(new ExecuteJavascriptFunctionAction("reload", params));
		
		return r;
	}
	
	@Override
	public AjaxResponse undo() {
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		
		int orderno = partner.getOrderno();
		partner.setOrderno(((Slide) context).getOrderno());
		((Slide) context).setOrderno(orderno);

		slideshow.sortSlides();
		boxService.save(slideshow);
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("slideshowBoxId", slideshow.getBoxId());
		r.addAction(new ExecuteJavascriptFunctionAction("reloadSlides", params));
		
		return r;
	}
	
	@Override
	public String getExecuteMessage() {
		return "Pakeista skaidrės eilės tvarka";
	}
	
	@Override
	public String getUndoMessage() {
		return "Sugrąžinta skaidrės eilės tvarka";
	}

}
