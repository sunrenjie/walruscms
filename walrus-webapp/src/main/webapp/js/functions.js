
function dressRubricLinks() {
	jQuery('.addSubRubricLink').rubricAdder('newSubRubric', globals.currentRubricId);
}

(function($jq){
	$jq.fn.picChanger = function(ajaxAction) {
		return this.each(function() {
			var formId = this.id + "_form";
			var link = jQuery('<div>' + $jq(this).html() + '</div>');
			link.css('cursor', 'pointer');
			var adder = jQuery('<form name=\"picUpload\" method=\"POST\" enctype=\"multipart/form-data\" action=\"upload\" id=\"' +
			 		formId + '\"/>');
			adder.hide();
			var input = jQuery("<input type=\"file\" name=\"file\"/>");
			adder.append(input);
			adder.append(jQuery("<input type=\"hidden\" name=\"articleId\" value=\""+ this.id.substring(12) + "\"/>"));
			var submitButton = jQuery("<input type=\"submit\" value=\"send\"/>")
			var cancelButton = jQuery("<input type=\"button\" value=\"cancel\"/>")
			
			adder.append(submitButton);
			adder.append(cancelButton);
			
			link.click(function(e){
				link.hide();
				adder.show();
				input.focus();
				return false;
			});
			
			submitButton.click(function(e){
				adder.submit();
				return false;
			});
			
			cancelButton.click(function(e){
					adder.hide();
					link.show();
					return false;
			});
			
			input.keydown(function (e) {
				if (e.keyCode == 27) {
					e.preventDefault();
					adder.hide();
					link.show();
					return false;
				}
			});
			
			adder.submit(function (e){
				e.preventDefault();
				adder.hide();
				link.show();
				XT.doAjaxSubmit(ajaxAction, this, null, {'formId' : formId, 'enableUpload' : true})
				return false;
			});	
			
			$jq(this).html('');
			$jq(this).append(link);
			$jq(this).append(adder);
		});
	}
})(jQuery);

(function($jq) {
	$jq.fn.rubricAdder = function(ajaxAction) {
		return this.each(function() {
			
			var self = this;
			var currentRubricId = this.id;
			//var link = jQuery('<span>' + $jq(this).html() + '</span>');
			var link = jQuery('<span><img src="../img/addrubric.gif"></span>');
			link.css('cursor', 'pointer');
			
			var adder = jQuery('<form style="display: inline;"/>');
			adder.hide();
			var input = jQuery("<input type=\"text\" class=\"input\" name=\"data\"/>");
			adder.append(input);
			var ok = jQuery("<input type=\"submit\" value=\"OK\"/>");
			adder.append(ok);
			var cancel = jQuery("<input type=\"button\" value=\"Cancel\"/>");
			adder.append(cancel);
			
			link.click(function(e){
				link.hide();
				adder.show();
				input.focus();
				return false;
			});
			
			input.keydown(function (e) {
				if (e.keyCode == 27) {
					e.preventDefault();
					adder.hide();
					link.show();
					return false;
				}
			});
/*			
			input.blur(function(e) {
				adder.hide();
				link.show();
				return false;
			});
*/
			
			ok.click(function(e){
				adder.submit();
				return false;
			});
			
			cancel.click(function(e){
				adder.hide();
				link.show();
				return false;
			});			
			
			
			adder.submit(function (e){
				e.preventDefault();
				if(null != currentRubricId){
					XT.doAjaxAction(ajaxAction, this, {'text' : input.val(), 'currentRubricId' : currentRubricId});
					jQuery(this).html("<img src=\"../img/ajax-loader.gif\"/>Saving...");
				} else {
					alert('no no no: ' + currentRubricId);
				}
				return false;
			});	
			
			$jq(this).html('');
			$jq(this).append(link);
			$jq(this).append(adder);
		});
	};
})(jQuery);


(function($jq) {
	$jq.fn.slideAdder = function(ajaxAction) {
		return this.each(function() {
			
			var self = this;
			var slideshowId = this.parentNode.parentNode.id;
			var link = jQuery('<span>' + $jq(this).html() + '</span>');
			link.css('cursor', 'pointer');
			
			var adder = jQuery('<form style="display: inline; margin: 0px; padding: 0px;"/>');
			adder.hide();
			var input = jQuery("<input type=\"text\" class=\"input\" name=\"data\"/>");
			adder.append(input);
			var ok = jQuery("<input type=\"submit\" value=\"OK\"/>");
			adder.append(ok);
			var cancel = jQuery("<input type=\"button\" value=\"Cancel\"/>");
			adder.append(cancel);
			
			link.click(function(e){
				link.hide();
				adder.show();
				input.focus();
				return false;
			});
			
			input.keydown(function (e) {
				if (e.keyCode == 27) {
					e.preventDefault();
					adder.hide();
					link.show();
					return false;
				}
			});

			ok.click(function(e){
				adder.submit();
				return false;
			});
			
			cancel.click(function(e){
				adder.hide();
				link.show();
				return false;
			});			
			
			
			adder.submit(function (e){
				e.preventDefault();
				if(null != slideshowId){
					XT.doAjaxAction(ajaxAction, this, {'title' : input.val(), 'slideshowId' : slideshowId});
					jQuery(this.parentNode).after("<span class=\"loadingIndicator loader\"><img src=\"../img/ajax-loader.gif\"/>Saving...</span>");
					input.val('');
					adder.hide();
					link.show();
				} else {
					alert('no no no: ' + slideshowId);
				}
				return false;
			});	
			$jq(this).html('');
			$jq(this).append(link);
			$jq(this).append(adder);
		});
	};
})(jQuery);

function delSlide(slideId, slideshowId) {
	XT.doAjaxAction('deleteSlide', this, {'slideId' : slideId, 'slideshow' : slideshowId});
	return false;
}

function setRubricMode(rubricId, mode) {
	XT.doAjaxAction('setRubricMode', this, {'currentRubricId' : rubricId, 'mode' : mode});
	return false;
}

function delRubric(deleteRubricId, currRubricId) {
	XT.doAjaxAction('deleteRubric', this, {'currentRubricId' : currRubricId, 'deleteRubricId' : deleteRubricId});
	return false;
}

function delSite(siteId) {
	XT.doAjaxAction('deleteSite', this, {'siteId' : siteId});
}

function siteDeleted() {
	reload();
}

function siteRestored() {
	reload();
}

function reload() {
	window.location.reload();
}

function saveRubricTitleCallback(args) {
	reloadMenu(args);
	reloadNavPath();
	return false;
}

function reloadMenu(args) {
	if(args) {
		if(args.isTree) {
			jQuery("#menuDiv").load("index?menu=yo&rubricId=" + globals.currentRubricId + "&isTree=1", function(){dressRubricLinks(); return false;});
		} else {
			jQuery("#menuDiv").load("index?menu=yo&rubricId=" + globals.currentRubricId, function(){dressRubricLinks(); return false;});
		}
	} else {
		jQuery("#menuDiv").load("index?menu=yo&rubricId=" + globals.currentRubricId, function(){dressRubricLinks(); return false;});
	}
	return false;
}

function reloadRubricList() {
	if(document.getElementById("rubricList")) {
		jQuery("#rubricList").load("index?rubricList=yo&rubricId=" + globals.currentRubricId);
	}
	return false;
}

function reloadNavPath() {
	if(document.getElementById("navpath")) {
		jQuery("#navpath").load("index?navpathreload=yo&rubricId=" + globals.currentRubricId);
	}
	return false;
}

function noop() {
	return false;
}

function undo() {
	XT.doAjaxAction('undo', this, {});
	return false;
}

function redo() {
	XT.doAjaxAction('redo', this, {});
	return false;
}

function setCommandMsg(o){
	jQuery("#commandMsg").text(o.msg);
	commandExecuted();
	return false;
}

function reloadCommandManager() {
	jQuery.get("commandManager", {'when' : new Date().getTime()}, function(data){jQuery("#commandManager").replaceWith(data);return false;});
	//jQuery("#commandManager").load("commandManager");
	return false;
}

function commandExecuted() {
	jQuery(".loadingIndicator").remove();
	return false;
}

function convertToEditor(el, beforeconvert, onsubmit, oncancel){
	var body = jQuery(el);
	var formId = el.id + '_form';
	var adder = jQuery('<form name=\"bodySaver\" method=\"POST\" action=\"saveField\" id=\"' +
	 		formId + '\"/>');
	var input = jQuery("<input type=\"hidden\" name=\"text\"/>");
	input.val(body.html());
	adder.append(input);
	var submitButton = jQuery("<input type=\"image\" class=\"imgInput\" src=\"../img/save_btn.png\" value=\"save\"/>")
	var cancelButton = jQuery("<input type=\"image\" class=\"imgInput\" src=\"../img/cancel_btn.png\" value=\"cancel\"/>")
	var originalParentHeight = body.parent()[0].clientHeight;

	adder.append(submitButton);
	adder.append(cancelButton);
	
	cancelButton.click(function(e){
					//jQuery(el).tinymce().execCommand('mceCleanup');
					tinyMCE.execInstanceCommand(el.id, 'mceCleanup');
					//jQuery(el).tinymce().hide();
					tinyMCE.execCommand('mceRemoveControl',false,el.id);
					body.html(input.val());
					body.parent().css("height", originalParentHeight);
					adder.remove();
					if(oncancel) {
						oncancel();
					}
					return false;
	});

	submitButton.click(function (e){
				e.preventDefault();
				//jQuery(el).tinymce().execCommand('mceCleanup');
				tinyMCE.execInstanceCommand(el.id, 'mceCleanup');
				//jQuery(el).tinymce().hide();
				tinyMCE.execCommand('mceRemoveControl',false,el.id);
				//input.val(jQuery(el).tinymce().html());
				input.val(body.html());
				XT.doAjaxSubmit('saveBody', el, null, {'formId' : formId, 'enableUpload' : true})
				
				//body.parent().css("height", originalParentHeight);
				body.parent()[0].style.height = originalParentHeight;
				adder.hide();
				if(jQuery.trim(body.html()) == '') {
					body.text('(Click here to edit)');
				}
				if(onsubmit){
					onsubmit();
				}
				return false;
				//adder.remove();
	});	

	
	body.after(adder);
	body.parent().css("height", "auto");
	if(beforeconvert){
		beforeconvert();
	}
	
	//jQuery(el).tinymce().show();
	tinyMCE.execCommand('mceAddControl',false,el.id);
}

function focusMCE(editor_id) {
	//tinyMCE.execCommand('mceFocus', false, editor_id);
}

function updateArticlePic(params) {
	var a = jQuery('#article_pic_' + params.articleId);
	var el = jQuery('#article_pic_' + params.articleId + ' img');
	el.attr('src',params.thumb);
}

function displayError(params) {
	jQuery('#error').html(params.msg);
	jQuery('#errorBox').show();
	return false;
}

function restoreValue(params){
	jQuery('#'+params.elementId).text(params.value);
	return false;
}

function changeMode(select, rubricId) {
	setRubricMode(rubricId, select.options[select.selectedIndex].value);
	return false;
}

function restoreDate(args) {
	jQuery('#' + args.fieldId).val(args.date);
	return false;
}