<html>
	<head>  
	        <script type="text/javascript" src="../js/springxt.js"></script>
	        <script type="text/javascript" src="../js/mcebrowser.js"></script>
	        <script type="text/javascript" src="../js/tiny_mce/tiny_mce_popup.js"></script>
	        <link rel="stylesheet" href="../upload.css" type="text/css" media="screen"/>
	</head>
<body>
<div id ="uploadContainer">
<#assign type = model.type!"" />
<form method="post" name='upload' action="../cms/upload${type}" enctype="multipart/form-data">
        <input type="file" name="file" id="fileInput"/><input type="button" value="Upload" onclick="XT.doAjaxSubmit('uploadFile', this, null, {'formName' : 'upload', 'enableUpload' : true}); "/>
</form>

<#if model.showFiles?? && model.showFiles>
	<input type="button" value="Hide uploaded files" onclick="window.location.href='list<#if model.type??>?type=${model.type}</#if>'"/>
	<#if model.list??>
	        <#list model.list as file>
				<p><a href="#" onclick="if(confirm('This file might be used somewhere in content. By deleting it you can corrupt the posted content. \n\nAre you sure you want to delete this file?')) XT.doAjaxAction('deleteFile', this, {'fileName':'${file.name}', 'type':'${type}'});return false;"><img src="../img/menu_handle.png" border="0"/></a> <a href="" onclick="selectURL('../files/${file.name}');return false;">${file.name}</a></p>
	        </#list>
	<#else/>
	        <p>...nėra failų</p>
	</#if>
<#else/>
	<input type="button" value="Show uploaded files" onclick="window.location.href='list<#if model.type??>?type=${model.type}&<#else/>?</#if>full=true'" />
</#if>
</div>
</body>
</html>