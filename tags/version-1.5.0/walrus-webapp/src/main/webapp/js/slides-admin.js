Array.prototype.swap=function(a, b)
{
	var tmp=this[a];
	this[a]=this[b];
	this[b]=tmp;
}

//callbackui iš newSlide ajaxinio callo

function addSlide(args) {
	var newIndex = slides.length;
	slides[newIndex] = args.slideId;
	var newSlide=jQuery(
			"<div class=\"item\" id=\"slide_"+ args.slideId + "\">" +
				"<h2 class=\"edit\" id=\"slide_title_" + args.slideId + "\">" + args.title + "</h2>" +
				"<div id=\"slide_body_" + args.slideId + "\" onclick=\"convertToEditor(this, function(){jQuery('.slideshow .controls').hide()}, function(){jQuery('.slideshow .controls').show()}, function(){jQuery('.slideshow .controls').show()});return null;\">" +
				"<span class=\"slideadmin\">" +
					args.body +
				"</span>" +
				"</div>" +
			"</div>");				
	var newShortcut = jQuery("<span id=\"slide_link_" + args.slideId + "\" class=\"slideadmin\"><img class=\"deleteSlide\" src=\"../img/menu_handle.png\" onclick=\"if(confirm('Are you sure you want to delete this slide?')) delSlide('"+ args.slideId +"', '" + args.slideshow + "');\"> <a id=\"slide_shortcut_" + args.slideId + "\" onclick=\"skipToSlide("+newIndex+"); return false;\" class=\"shortcut\" href=\"#\">" + args.title + "</a> /</span>");
	jQuery("#"+args.slideshow + " .slides").append(newSlide);
	jQuery("#"+args.slideshow + " .shortcuts").append(newShortcut);
	jQuery(".loader").remove();
	jQuery(".slideshow .controls").removeClass('hidden');
	skipToSlide(newIndex);
	dressEditables();
}

function removeSlide(args) {
	deletedSlideIndex = findSlideIndex(args.slideId);
	if (-1 == deletedSlideIndex) {
		alert("NO SUCH SLIDE?!");
	} else {
		if (slideIndex == deletedSlideIndex) {
			nextSlide();
		}
		jQuery('#slide_' + args.slideId).remove();
		jQuery('#slide_link_' + args.slideId).remove();
		slides.splice(deletedSlideIndex, 1);
	}
	if (1 >= slides.length) {
		jQuery(".slideshow .controls").addClass('hidden');
	}
}

function findSlideIndex(slideId) {
	for (i = 0; i < slides.length; i++) {
		if (slideId == slides[i]) {
			return i;
		}
	}
	
	return -1;
}

function moveSlide(currIndex, direction) {
	if (0 == currIndex && -1 == direction) {
		return false;
	}
	
	if ((slides.length - 1) == currIndex && 1 == direction) {
		return false;
	}
	var edited = jQuery('#slide_shortcut_' + slides[currIndex])[0];
	XT.doAjaxAction('saveField', edited, {'text' : direction});
	return false;
}