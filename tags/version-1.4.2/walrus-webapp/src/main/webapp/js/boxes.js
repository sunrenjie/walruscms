function showNewBanner(arg) {
	if ("" == arg.link) {
		jQuery('#banner_' + arg.boxId).attr("src", '../img/sample_banner.jpg');
		jQuery('#link_' + arg.boxId).attr("href", "#");
	} else {
		jQuery('#banner_' + arg.boxId).attr("src", arg.banner);
		jQuery('#link_' + arg.boxId).attr("href", arg.link);
	}
	if ("" != arg.boxId) {
		jQuery("#allBanners_"+arg.boxId).load('bannerList?boxId=' + arg.boxId);
		document.forms['bannerEditor_' + arg.boxId].file.value='';
		document.forms['bannerEditor_' + arg.boxId].url.value='http://';
	}
}

function showNewImage(arg) {
	if ("" == arg.image) {
		jQuery('#banner_' + arg.boxId).attr("src", '../img/sample_image.jpg');
	} else {
		jQuery('#banner_' + arg.boxId).attr("src", arg.image);
	}
	if ("" != arg.boxId) {
		document.forms['imageEditor_' + arg.boxId].file.value='';
	}
}

function deleteBanner(boxId, bannerId) {
	XT.doAjaxAction('deleteBanner', this, {'boxId' : boxId, 'bannerId' : bannerId});
	jQuery("#banner_" + boxId +"_" + bannerId).hide();
}

(function($jq){
	$jq.fn.bannerEditor = function() {
		return this.each(function() {
			var link = jQuery('<div>' + $jq(this).html() + '</div>');
			
			link.css('cursor', 'pointer');
			
			var formId = this.id + "_form";
			var boxId = this.id;

			var allBanners = jQuery('<div id="allBanners_'+boxId+'"></div>');
			allBanners.load('bannerList?boxId=' + boxId);
			
			var submitButton = jQuery("<input type=\"submit\" value=\"submit\"/>")
			var cancelButton = jQuery("<input type=\"button\" value=\"cancel\"/>")
			
			var adder = jQuery('<form class="walrusDialog" name=\"bannerEditor_' + boxId + '\" method=\"POST\" enctype=\"multipart/form-data\" action=\"editbanner\" id=\"' +
			 		formId + '\"/>');
			adder.hide();
			
			adder.append(allBanners);
			
			adder.append(jQuery("<hr />"));
			adder.append(jQuery("<b>New banner</b>"));
			
			var input = jQuery("<p>Banner image: </p><p><input type=\"file\" name=\"file\"/></p>");
			adder.append(input);
			var url = jQuery('<p>Link image to: </p><p><input type="text" value="http://" name="url" /></p>');
			adder.append(url);
			adder.append(jQuery("<input type=\"hidden\" name=\"boxId\" value=\""+ boxId + "\"/>"));
			
			var p = jQuery("<p></p>");
			p.append(submitButton);
			p.append(cancelButton);
		
			adder.append(p);
			adder.append(jQuery("<p>&nbsp;</p>"))			
			link.click(function(e) {
				e.preventDefault();
				//link.hide();
				adder.show();
				input.focus();
			});

			cancelButton.click(function(e){
					adder.hide();
					//link.show();
			});
			
			input.keydown(function (e) {
				if (e.keyCode == 27) {
					e.preventDefault();
					adder.hide();
					//link.show();
				}
			});
			
			url.keydown(function (e) {
				if (e.keyCode == 27) {
					e.preventDefault();
					adder.hide();
					//link.show();
				}
			});			
		
			adder.submit(function (e){
				e.preventDefault();
				adder.hide();
				//link.show();
				XT.doAjaxSubmit('addBanner', this, {'boxId' : boxId}, {'formId' : formId, 'enableUpload' : true})
			});	
			
			$jq(this).html('');
			$jq(this).append(link);
			$jq(this).append(adder);
		});
	}
})(jQuery);

function bannerUrlEditor(obj) {
			jQuery(obj).css('cursor', 'pointer');
			
			var formId = obj.id + "_form";
			var boxId = (obj.id).substring((obj.id).indexOf('_')+1, (obj.id).lastIndexOf('_'));
			var bannerId = (obj.id).substring((obj.id).lastIndexOf('_')+1);
			
			var submitButton = jQuery("<input type=\"submit\" value=\"submit\"/>")
			var cancelButton = jQuery("<input type=\"button\" value=\"cancel\"/>")
			
			var adder = jQuery(	'<form class="walrusDialog" ' +
								'name=\"bannerUrlEditor_' + boxId + '_' + bannerId +
								'\" method=\"POST\" enctype=\"multipart/form-data\" ' +
								'action=\"editbannerurl\" id=\"' + formId + '\"/>');
			adder.show();
			
			var file = jQuery('<p><input type="file" name="file" /></p>');
			var url = jQuery('<p><input type="text" value="'+jQuery(obj).attr('alt')+'" name="url" /></p>');
			adder.append(file);
			adder.append(url);
			adder.append(jQuery("<input type=\"hidden\" name=\"boxId\" value=\""+ boxId + "\"/>"));
			adder.append(jQuery("<input type=\"hidden\" name=\"bannerId\" value=\""+ bannerId + "\"/>"));
			
			var p = jQuery("<p></p>");
			p.append(submitButton);
			p.append(cancelButton);
		
			adder.append(p);
			
			//jQuery(obj).hide();
			adder.show();
			url.focus();
			//jQuery(obj).hide();

			cancelButton.click(function(e){
					adder.hide();
					//jQuery(obj).show();
			});
			
			url.keydown(function (e) {
				if (e.keyCode == 27) {
					e.preventDefault();
					adder.hide();
					//jQuery(obj).show();
				}
			});			
		
			adder.submit(function (e){
				e.preventDefault();
				adder.hide();
				//jQuery(obj).show();
				XT.doAjaxSubmit('updateBannerUrl', obj, {'boxId' : boxId}, {'formId' : formId, 'enableUpload' : true})
			});	
			
			jQuery(obj.parentNode).append(adder);
}


(function($jq){
	$jq.fn.imageEditor = function() {
		return this.each(function() {
		
			var link = jQuery('<div>' + $jq(this).html() + '</div>');
			
			link.css('cursor', 'pointer');
			
			var formId = this.id + "_form";
			var boxId = this.id;

			var submitButton = jQuery("<input type=\"submit\" value=\"submit\"/>")
			var cancelButton = jQuery("<input type=\"button\" value=\"cancel\"/>")
			
			var adder = jQuery('<form class="walrusDialog" name=\"imageEditor_' + boxId + '\" method=\"POST\" enctype=\"multipart/form-data\" action=\"editimage\" id=\"' +	formId + '\"/>');
			adder.hide();
			
			adder.append(jQuery("<h2>New image</h2>"));
			
			var input = jQuery("<p><input type=\"file\" name=\"file\"/></p>");
			adder.append(input);
			adder.append(jQuery("<input type=\"hidden\" name=\"boxId\" value=\""+ boxId + "\"/>"));
			
			var p = jQuery("<p></p>");
			p.append(submitButton);
			p.append(cancelButton);
		
			adder.append(p);
			
			link.click(function(e) {
				e.preventDefault();
				//link.hide();
				adder.show();
				input.focus();
			});

			cancelButton.click(function(e){
					adder.hide();
					//link.show();
			});
			
			input.keydown(function (e) {
				if (e.keyCode == 27) {
					e.preventDefault();
					adder.hide();
					//link.show();
				}
			});
			
			adder.submit(function (e){
				e.preventDefault();
				adder.hide();
				link.show();
				XT.doAjaxSubmit('updateImage', this, {'boxId' : boxId}, {'formId' : formId, 'enableUpload' : true})
			});	
			
			$jq(this).html('');
			$jq(this).append(link);
			$jq(this).append(adder);
		});
	}
})(jQuery);