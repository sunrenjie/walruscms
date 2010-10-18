function showSlide(index) {
	jQuery('#slide_'+slides[index]).show();
	jQuery('#slide_shortcut_'+slides[index]).addClass('current');
}

function hideSlide(index){
	jQuery('#slide_'+slides[index]).hide();
	jQuery('#slide_shortcut_'+slides[index]).removeClass('current');
}

function nextSlide() {
	hideSlide(slideIndex);

	if(slideIndex < slides.length-1) {
		slideIndex++;
	} else {
		slideIndex=0;
	}
	showSlide(slideIndex);
	//pageTracker._trackEvent('WalrusHP', 'viewSlide', 'slide', slides[slideIndex]);
}

function previousSlide() {
	hideSlide(slideIndex);
	if(slideIndex > 0) {
		slideIndex --;
	} else {
		slideIndex = slides.length-1;
	}
	showSlide(slideIndex);
	//pageTracker._trackEvent('WalrusHP', 'viewSlide', 'slide', slides[slideIndex]);
}

function skipToSlide(index) {
	hideSlide(slideIndex);
	showSlide(index);
	slideIndex=index;
} 	