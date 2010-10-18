package lt.walrus.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlideshowBox extends Box {
	private static final long serialVersionUID = 6639606835588856112L;
	
	private List<Slide> slides;

	public SlideshowBox() {
		slides = new ArrayList<Slide>();
	}
	
	public void setSlides(List<Slide> slides) {
		this.slides = slides;
	}

	public List<Slide> getSlides() {
		return slides;
	}
	
	public SlideshowBox clone() {
		SlideshowBox b = new SlideshowBox();
		b.setBoxId(getBoxId());
		List<Slide> clonedSlides = new ArrayList<Slide>();
		for (Slide slide: slides) {
			clonedSlides.add(slide.clone());
		}
		b.setSlides(clonedSlides);
		return b;
	}
	
	public boolean hasSlide(Slide slide) {
		return slides.contains(slide);
	}
	
	public boolean hasSlide(long slideId) {
		return hasSlide(new Slide(slideId));
	}
	
	public void addSlide(Slide slide) {
		int newOrder = (slides.size() > 0) ? slides.get(slides.size() - 1).getOrderno() + 10 : 1; 
		slide.setOrderno(newOrder);
		slides.add(slide);
		sortSlides();
	}
	
	public void deleteSlide(Slide slide) {
		if (hasSlide(slide)) {
			slides.remove(slide);
			sortSlides();
		}
	}

	public Slide getSlide(long l) {
		for (Slide slide: slides) {
			if (l == slide.getId()) {
				return slide;
			}
		}
		
		return null;
	}
	
	public void sortSlides() {
		Collections.sort(slides);
	}

	public Slide getPrevious(Slide slide) {
		int slideIndex = slides.indexOf(slide);
		if (-1 == slideIndex || -1 == (slideIndex - 1)) {
			return null;
		} else {
			return slides.get(slideIndex - 1);
		}
	}

	public Slide getNext(Slide slide) {
		int slideIndex = slides.indexOf(slide);
		if (-1 == slideIndex || slides.size() == (slideIndex + 1)) {
			return null;
		} else {
			return slides.get(slideIndex + 1);
		}
	}
}
