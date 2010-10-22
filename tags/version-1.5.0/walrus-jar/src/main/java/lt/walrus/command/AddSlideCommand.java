package lt.walrus.command;

import java.util.HashMap;

import lt.walrus.model.Slide;
import lt.walrus.model.SlideshowBox;
import lt.walrus.service.BoxService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

// TODO reafactor: use existing infrastructure
public class AddSlideCommand extends Command {
	private static final long serialVersionUID = -4346829962319216295L;
	
	private SlideshowBox slideshow;
	private Slide newSlide;
	private String newSlideTitle;
	
	protected BoxService service;
	
	public AddSlideCommand(BoxService service2, SlideshowBox slideshow2, String slideTitle) {
		service = service2;
		slideshow = slideshow2;
		
		newSlideTitle = slideTitle;
	}

	public AjaxResponse execute() {
		newSlide = new Slide();
		newSlide.setTitle(newSlideTitle);
		
		slideshow.addSlide(newSlide);
		service.save(slideshow);
		// service.save(newSlide);
		
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("slideId", newSlide.getId());
		params.put("title", newSlide.getTitle());
		params.put("slideshow", slideshow.getBoxId());
		params.put("body", newSlide.getBody());
		r.addAction(new ExecuteJavascriptFunctionAction("addSlide", params));
		return r;
	}

	public AjaxResponse redo() {
		slideshow.addSlide(newSlide);
		service.save(slideshow);
		// service.save(newSlide);
		
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		HashMap<String, Object> params = new HashMap<String, Object>();
		r.addAction(new ExecuteJavascriptFunctionAction("removeSlide", params));
		return r;
	}

	public AjaxResponse undo() {
		slideshow.deleteSlide(newSlide);
		service.save(slideshow);
		service.deleteSlide(newSlide);
		
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		HashMap<String, Object> params = new HashMap<String, Object>();
		r.addAction(new ExecuteJavascriptFunctionAction("addSlide", params));
		return r;
	}

	public void setNewSlide(Slide newSlide) {
		this.newSlide = newSlide;
	}

	public Slide getNewSlide() {
		return newSlide;
	}

	public void setSlideshow(SlideshowBox slideshow) {
		this.slideshow = slideshow;
	}

	public SlideshowBox getSlideshow() {
		return slideshow;
	}
	
	public String getExecuteMessage() {
		return "Pridėta skaidrė '" + newSlideTitle + "'";
	}

	public String getUndoMessage() {
		return "Pašalinta skaidrė '" + newSlideTitle + "'";
	}
	
	public String getRedoMessage() {
		return "Atkurta skaidrė '" + newSlideTitle + "'";
	}
}
