<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]>

<#macro adminScripts>
<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<script src="${model.contextPath}/js/springxt.js"></script>
		<script src="${model.contextPath}/js/jquery-1.3.2.min.js"></script>
		<script src="${model.contextPath}/js/jquery.jeditable.js"></script>
		<script src="${model.contextPath}/js/bootstrap.js"></script>
		<script src="${model.contextPath}/js/boxes.js"></script>
		<script src="${model.contextPath}/js/jquery.date_input.js"></script>
		<script src="${model.contextPath}/js/functions.js"></script>
		<script src="${model.contextPath}/js/mcebrowser.js"></script>
		
		<script language="javascript" type="text/javascript" src="${model.contextPath}/js/tiny_mce/tiny_mce_src.js"></script>
		<#--script language="javascript" type="text/javascript" src="${model.contextPath}/js/tiny_mce/jquery.tinymce.js"></script-->
		<script language="javascript" type="text/javascript">
		
		tinyMCE.init({
			// Location of TinyMCE script
			script_url : '${model.contextPath}/js/tiny_mce/tiny_mce_src.js',
			 
			// General options
			theme : "advanced",
			plugins : "safari,table,advimage,advlink,preview,media,contextmenu,paste,visualchars,inlinepopups,xhtmlxtras",  
			mode : "none",
			relative_urls : 0,
			dialog_type: "modal",
			
			// Theme options
			theme_advanced_buttons1 : "cut,copy,paste,pastetext,pasteword,|,undo,redo,|,fontsizeselect,forecolor,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,charmap,media",
			theme_advanced_buttons2 : "link,unlink,anchor,image,|,tablecontrols,|,forecolorpicker,|,code,removeformat",
			theme_advanced_buttons3 : "",
			theme_advanced_buttons4 : "",
			theme_advanced_containers : "commandManager",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "none",
			theme_advanced_toolbar_location : "external",
			theme_advanced_resizing : true,
			
			extended_valid_elements : "a[name|href|target|title|onclick|rel],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],input[size|name|type|class|value|id],textarea[name|rows|cols],button[type],form[action|method|id|onsubmit|enctype],label,#td[*],object[classid|codebase|width|height|align|*],param[name|value],",
			invalid_elements : "font,p[style]",
			setupcontent_callback : "focusMCE",
			file_browser_callback: "fileBrowserCallback",
			 
			// Example content CSS (should be your site CSS)
			content_css : "${model.contextPath}/style.css"
		});		
		
	 	var globals = {
	 		currentRubricId : '${model.currentRubric.id}'
	 	};

		function reloadMenu(args) {
			var treeFlag = "";
			
			if(args && '1' == args.isTree) {
				treeFlag = "&isTree=1";
			}
			jQuery.get("index?menu=yo&rubricId=" + globals.currentRubricId + treeFlag, {}, function(html) {
				jQuery("*[id]", html).each(function() {
					jQuery("#"+this.id).html(jQuery(this).html());
				});
				dressRubricLinks();
			});
		}
		</script>

		<link rel="stylesheet" href="${model.contextPath}/date_input.css" type="text/css" media="screen" />
		<link rel="stylesheet"  href="${model.contextPath}/cm.css" type="text/css" media="screen"/>
</@sec.authorize>
</#macro>

<#macro loginLogout>
	<@sec.authorize ifNotGranted="ROLE_ADMIN, ROLE_USER">
		<a href="${model.contextPath}/cms/login" id="loginLink"><@spring.message code="walrus.login"/></a>
	</@sec.authorize>
	<@sec.authorize ifAnyGranted="ROLE_ADMIN, ROLE_USER">
		<#assign currentUrl = "${model.requestURL}" />
		<#if model.queryString?? >
			<#assign currentUrl = "${currentUrl}?${model.queryString}" />
		</#if>
		<a href="${model.contextPath}/cms/logout?logoutSuccessUrl=${currentUrl?url("UTF-8")}"><@spring.message code="walrus.logout"/></a>
	</@sec.authorize>
</#macro>

<#macro listTemplates>
	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		Change site theme to:
		<#list model.templateManager.templates as template>
			<#if model.site.templatePath?? && template == model.site.templatePath>
				<b>${template}</b>
			<#else/>
				<a href="switchTemplate?template=${template}">${template}</a>
			</#if>
		</#list>
			
	</@sec.authorize>
</#macro>

<#function canShowInList article isArchive>
	<#if article??>
        <#if !isArchive >
            <#return (article.isOnline() && article.isVisible()) || (model.isAdmin?? && model.isAdmin && (!article.isOnline() || article.isPending())) />
        <#else/>
            <#return (article.isOnline() && article.isArchived()) || (model.isAdmin?? && model.isAdmin && !article.isOnline() && article.isArchived()) />
        </#if>
	<#else/>
        <#return false />
	</#if>
</#function>

<#macro delLink rubric>
	<#if !(model.currentRubric?exists && model.currentRubric.id == rubric.id)>
		<#if model.isAdmin?exists>
			<img class="deleteRubric walrusControl" src="../img/menu_handle.png" onclick="if (confirm('Are you sure you want to delete this rubric?')) delRubric('${rubric.id}', '<#if model.currentRubric?exists>${model.currentRubric.id}</#if>'); return false;" />
		</#if>
	</#if>
</#macro>

<#macro deleteSite site>
	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<img class="deleteRubric walrusControl" src="../img/menu_handle.png" onclick="if (confirm('Are you sure you want to delete this site?')) delSite('${site.id}'); return false;" />
	</@sec.authorize>
</#macro>

<#macro addSubRubric rubric>
	<#if model.isAdmin?exists>
		<#if model.currentRubric?exists >
			<span id="${rubric.id}" class="addSubRubricLink walrusControl"><img src="../img/addrubric.gif" /></span>
		</#if>
	</#if>
</#macro>

<#macro addArticle rubric>
	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<p id="newArticleLink"><a href="#" onclick="XT.doAjaxAction('addArticle', this, {'rubricId': '${rubric.id}'}); return false;"><img src="../img/add_new_article_btn.png" alt="pridėti naują straipsnį" title="pridėti naują straipsnį" /></a></p>
	</@sec.authorize>
</#macro>

<#macro rubricModes rubric>
	<select id="dropdown" onchange="changeMode(this, '${rubric.id}')">
		<option <#if rubric.mode == "NONE">selected="selected"</#if>value="NONE">don't show subrubrics</option>
		<option <#if rubric.mode == "SIMPLE_LIST">selected="selected"</#if>value="SIMPLE_LIST">simple list</option>
		<option <#if rubric.mode == "EXPANDED_LIST">selected="selected"</#if>value="EXPANDED_LIST">expanded list</option>
		<option <#if rubric.mode == "BLOG">selected="selected"</#if>value="BLOG">blog</option>
	</select>
</#macro>

<#macro deleteRubric rubric><@sec.authorize ifAllGranted="ROLE_ADMIN"><img class="deleteRubric walrusControl" src="../img/menu_handle.png" onclick="if(confirm('Are you sure you want to delete this rubric?')) delRubric('${rubric.id}', '<#if model.currentRubric?exists>${model.currentRubric.id}</#if>');" /></@sec.authorize></#macro>

<#macro rubricControls rubric>
	<div id="articleControlsHeader"><img src="${model.contextPath}/img/arrow.gif"/><span>Rubric settings</span></div>
	<div id="articleControls">
		<p>
			<b>Static url:</b> ${model.fullContextPath}${model.staticServletPath}/<span class="edit help-staticAddress" id="rubric_url_${rubric.id}">${rubric.url!''}</span>
		</p>
		<p>
			<b>Comments:</b>
				<input type="checkbox" class="help-leaf"
					onclick="
						XT.doAjaxAction('setCommentsAllowed', this, {'rubricId': '${rubric.id}', 'commentsAllowed' : this.checked});
						jQuery(this).after('<span class=\'loadingIndicator\'><img src=\'../img/ajax-loader.gif\'/><b>Saving</b></span>');
						if(this.checked){
							jQuery('#commentsDescriotion_isAllowed').show();
							jQuery('#commentsDescriotion_isNotAllowed').hide();
						}else {
							jQuery('#commentsDescriotion_isAllowed').hide();
							jQuery('#commentsDescriotion_isNotAllowed').show();
						}
						" 
					<#if rubric.commentsAllowed>checked='checked'</#if> 
				/> 
				<span class="description" id="commentsDescriotion_isAllowed" <#if !rubric.commentsAllowed>style="display:none;"</#if>>(Comments are allowed)</span>
				<span class="description" id="commentsDescriotion_isNotAllowed" <#if rubric.commentsAllowed>style="display:none;"</#if>>(Comments are disabled)</span>
		</p>			
		
		<p>
			<b>Published: </b>Visible forever: 
			<input type="checkbox" class="help-visibleForever"
				onclick="XT.doAjaxAction('setVisibleForever', this, {'rubricId': '${rubric.id}', 'visible' : this.checked}); jQuery(this).after('<span class=\'loadingIndicator\'><img src=\'../img/ajax-loader.gif\'/><b>Saving</b></span>');if (this.checked) { jQuery('#rubricDates_${rubric.id}').hide() } else { jQuery('#rubricDates_${rubric.id}').show() };" 
				<#if rubric.visibleForever>checked='checked'</#if> 
			/>
			<span id="rubricDates_${rubric.id}" class="rubricDates" <#if rubric.visibleForever>style="display:none"</#if>>
				Visible from:
				<input id="visibleFrom_${rubric.id}" value="${rubric.visibleFrom?string("yyyy-MM-dd")}" class="date_input" type="text" size="10"
					onchange="XT.doAjaxAction('setVisibleFrom', this, {'rubricId': '${rubric.id}', 'date' : this.value});jQuery(this).after('<span class=\'loadingIndicator\'><img src=\'../img/ajax-loader.gif\'/><b>Saving</b></span>');" 
				/>
				to:
				<input id="visibleTo_${rubric.id}" value="${rubric.visibleTo?string("yyyy-MM-dd")}"class="date_input" type="text" size="10"
					onchange="XT.doAjaxAction('setVisibleTo', this, {'rubricId': '${rubric.id}', 'date' : this.value});jQuery(this).after('<span class=\'loadingIndicator\'><img src=\'../img/ajax-loader.gif\'/><b>Saving</b></span>');"				 
				/>
			</span>
		</p>
		<#-- p>
			<b>Featured on first page: </b>
			<input type="checkbox" class="help-feature"
				onclick="
					XT.doAjaxAction('setFeature', this, {'rubricId': '${rubric.id}', 'feature' : this.checked}); 
					jQuery(this).after('<span class=\'loadingIndicator\'><img src=\'../img/ajax-loader.gif\'/><b>Saving</b></span>');
					if (this.checked) {
						jQuery('#featureDescription_isNotFeature').hide();
						jQuery('#featureDescription_isFeature').show();
					} else {
						jQuery('#featureDescription_isNotFeature').show();
						jQuery('#featureDescription_isFeature').hide();					
					}
				"
				<#if rubric.feature>checked</#if>
			/>
			<span class="description" id="featureDescription_isNotFeature" <#if rubric.feature>style="display:none;"</#if>>(Not featured on first page)</span>
			<span class="description" id="featureDescription_isFeature" <#if !rubric.feature>style="display:none;"</#if>>(Featured on first page)</span>		
		</p -->
		<#-- p>
			<b>Featured on MAIN first page: </b>
			<input type="checkbox" class="help-feature"
				onclick="
					XT.doAjaxAction('setFeatureMain', this, {'rubricId': '${rubric.id}', 'featureMain' : this.checked}); 
					jQuery(this).after('<span class=\'loadingIndicator\'><img src=\'../img/ajax-loader.gif\'/><b>Saving</b></span>');
					if (this.checked) {
						jQuery('#featureMainDescription_isNotFeature').hide();
						jQuery('#featureMainDescription_isFeature').show();
					} else {
						jQuery('#featureMainDescription_isNotFeature').show();
						jQuery('#featureMainDescription_isFeature').hide();					
					}
				"
				<#if rubric.featureMain>checked</#if>
			/>
			<span class="description" id="featureMainDescription_isNotFeature" <#if rubric.feature>style="display:none;"</#if>>(Not featured on first MAIN page)</span>
			<span class="description" id="featureMainDescription_isFeature" <#if !rubric.feature>style="display:none;"</#if>>(Featured on first MAIN page)</span>		
		</p -->
		<#--if !rubric.children?? || rubric.children?size==0-->
			<p>
				<b>Included in main menu:</b>
				<input type="checkbox" class="help-leaf"
					onclick="
						try{
							pageTracker._trackEvent('Demo - rubrics', 'setLeaf');
						} catch(e){}
						XT.doAjaxAction('setLeaf', this, {'rubricId': '${rubric.id}', 'leaf' : !this.checked});
						jQuery(this).after('<span class=\'loadingIndicator\'><img src=\'../img/ajax-loader.gif\'/><b>Saving</b></span>');
						if(this.checked){
							jQuery('#leafDescription_isNotLeaf').show();
							jQuery('#leafDescription_IsLeaf').hide();
						}else {
							jQuery('#leafDescription_isNotLeaf').hide();
							jQuery('#leafDescription_IsLeaf').show();
						}
						" 
					<#if !rubric.leaf>checked='checked'</#if> 
				/> 
				<span class="description" id="leafDescription_isNotLeaf" <#if rubric.leaf>style="display:none;"</#if>>(Rubric is included in main menu)</span>
				<span class="description" id="leafDescription_IsLeaf" <#if !rubric.leaf>style="display:none;"</#if>>(Rubric is not included in main menu, but is accessible from parent rubric)</span>
			</p>
		<#--else/-->
			<p>
				<b>Subrubrics mode:</b> <@walrus.rubricModes rubric/>		
			</p>
		<#--if-->
		<p class="last">
			<b>Online status:</b>
			<input type="checkbox" class="help-leaf"
					onclick="
						jQuery(this).after('<span class=\'loadingIndicator\'><img src=\'../img/ajax-loader.gif\'/><b>Saving</b></span>');
						if(this.checked){
							XT.doAjaxAction('publishArticle', this, {'rubricId': '${rubric.id}'});
							jQuery('#onlineDescriotion_isOnline').show();
							jQuery('#onlineDescriotion_isOffline').hide();
							jQuery('#rubric_is_offline').hide();
						}else {
							XT.doAjaxAction('unpublishArticle', this, {'rubricId': '${rubric.id}'});
							jQuery('#onlineDescriotion_isOnline').hide();
							jQuery('#onlineDescriotion_isOffline').show();
							jQuery('#rubric_is_offline').show();
						}
						" 
					<#if rubric.online>checked='checked'</#if> 
				/> 
			<span class="description" id="onlineDescriotion_isOnline" <#if !rubric.online>style="display:none;"</#if>>(Rubric is online)</span>
			<span class="description" id="onlineDescriotion_isOffline" <#if rubric.online>style="display:none;"</#if>>(Rubric is accessibel to admins only.)</span>
		</p>			
		
	</div>
</#macro>



<#macro activeClass rubric>
	<#if (model.currentRubric?exists && model.currentRubric.id == rubric.id)>active</#if>
</#macro>

<#macro drawRubricLink rubric><@compress single_line=true>
	<#if "" == rubric.title && model.isAdmin??>
		<#local rtitle="[BEVARDIS]"/>
	<#else>
		<#local rtitle=rubric.title?html/>
	</#if>
	<#if !model.isAdmin?? && rubric.url?? && (rubric.url?length>0)>
		<#assign url>${model.contextPath}${model.staticServletPath}/${rubric.url}</#assign>
	<#else/>
		<#assign url>${model.contextPath}/cms/index?rubricId=${rubric.id}</#assign>
	</#if>
	<a href="${url}" class="rubric_link<#if !rubric.online || rubric.leaf> offline</#if> <@activeClass rubric/>">${rtitle}</a>
</@compress></#macro>

<#macro addSubRubricLink>
	<div id="addSubRubricLink" class="label walrusControl">
		<img class="help-addSubrubric" src="../img/addrubric.gif" />
	</div>
</#macro>

<#function isRubricAttachedToBox arubric>
	<#list model.site.boxes as box>
		<#if box?? && box.rubric??>
			<#if arubric.id == box.rubric.id>
				<#return true/>
			</#if>
		</#if>
	</#list>
	<#return false/>
</#function>