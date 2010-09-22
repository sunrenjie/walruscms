package lt.walrus.command;

import java.util.HashMap;

import lt.walrus.model.Slide;
import lt.walrus.model.SlideshowBox;
import lt.walrus.service.WalrusService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class DeleteSlideCommand extends Command {
	private static final long serialVersionUID = -4346829962456987295L;
	
	private SlideshowBox slideshow;
	private Slide slide;
	private String slideId;
	
	protected WalrusService service;
	
	public DeleteSlideCommand(WalrusService service2, SlideshowBox slideshow2, String slideId2) {
		service = service2;
		slideshow = slideshow2;
		setSlideId(slideId2);
		slide = slideshow.getSlide(slideId);
	}

	public AjaxResponse execute() {
		slideshow.deleteSlide(slide);
		service.save(slideshow);
		service.delete(slide);
		
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("slideshow", slideshow.getBoxId());
		params.put("slideId", slide.getId());
		r.addAction(new ExecuteJavascriptFunctionAction("removeSlide", params));
		return r;
	}

	public AjaxResponse redo() {
		slideshow.deleteSlide(slide);
		service.save(slideshow);
		service.delete(slide);
		
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("slideshow", slideshow.getBoxId());
		params.put("slideId", slide.getId());
		r.addAction(new ExecuteJavascriptFunctionAction("removeSlide", params));
		return r;
	}

	public AjaxResponse undo() {
		slideshow.addSlide(slide);
		service.save(slide);
		service.save(slideshow);
		
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("slideId", slide.getId());
		params.put("title", slide.getTitle());
		params.put("slideshow", slideshow.getBoxId());
		params.put("body", slide.getBody());
		r.addAction(new ExecuteJavascriptFunctionAction("addSlide", params));
		return r;
	}

	public void setSlideshow(SlideshowBox slideshow) {
		this.slideshow = slideshow;
	}

	public SlideshowBox getSlideshow() {
		return slideshow;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}

	public Slide getSlide() {
		return slide;
	}

	public void setSlideId(String slideId) {
		this.slideId = slideId;
	}

	public String getSlideId() {
		return slideId;
	}

}
