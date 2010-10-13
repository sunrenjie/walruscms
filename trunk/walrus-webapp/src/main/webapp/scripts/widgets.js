lib.widget({
    name: "heightFix",
    
    construct: function construct() {
	var stepHeight = parseInt(lib.dom.getStyle(document.body, "lineHeight")),
	    marginTop = parseInt(lib.dom.getStyle(this.element, "marginTop")),
	    marginBottom = parseInt(lib.dom.getStyle(this.element, "marginBottom")),
	 	elemHeight = this.element.scrollHeight,
        space = stepHeight - (elemHeight + marginTop) % stepHeight;
        this.element.style.marginBottom = marginBottom + space + "px";
        
    }
});
lib.widget.heightFix.run(lib.dom.byClass("height-fix-widget"));

lib.widget({
    name: "slider",
    
    current: 0,
    
    slides: [],
    actions: {
        prev: [],
        next: []
    },
    
    events: {},
    
    construct: function construct() {
        var slides = lib.dom.byClass("slider-widget-slides", this.element);
        if (slides && slides.length) this.slides = slides[0].children;
        else return;
        
        this.actions.prev = lib.dom.NodeList(lib.dom.byClass("slider-widget-prev", this.element));
        this.actions.next = lib.dom.NodeList(lib.dom.byClass("slider-widget-next", this.element));
        
        for (var i = 1, len = this.slides.length; i < len; i++) {
            lib.dom.addClass(this.slides[i], "hidden");
        }
        
        this.events.navigate = lib.bind(this.navigate, this);
        lib.event.add(this.element, "click", this.events.navigate);
    },
    
    destroy: function destroy() {
        lib.event.remove(this.element, "click", this.events.navigate);
    },
    
    navigate: function navigate(event) {
        if (!event.target.dataset || !event.target.dataset.nav) return;
        var nav = event.target.dataset.nav;
        
        if (nav == "next") {
            if (this.current == this.slides.length - 1) return;
            lib.dom.addClass(this.slides[this.current], "hidden");
            lib.dom.removeClass(this.slides[++this.current], "hidden");
        } else if (nav == "prev") {
            if (this.current == 0) return;
            lib.dom.addClass(this.slides[this.current], "hidden");
            lib.dom.removeClass(this.slides[--this.current], "hidden");
        }
        
        event.preventDefault();
    }
});
//lib.widget.slider.run(lib.dom.byClass("slider-widget"));


























