<html>
<head>
		<script src="../js/springxt.js"></script>
</head>
<body>
<#if model?? && model.tooBig?? && model.tooBig>
<div id="tooBigError">Failas per didelis</div>
</#if>
Adresas: <input id="url" name="url" type="text" />
<form method="post" action="../cms/upload" enctype="multipart/form-data">
<div id="info"></div>
Nusiųsti failą: <input type="file" name="file"/><input type="button" value="Upload" onclick="XT.doAjaxSubmit('uploadFile', this, null, {'enableUpload' : true});return false; "/>
</form>
</body>
</html>