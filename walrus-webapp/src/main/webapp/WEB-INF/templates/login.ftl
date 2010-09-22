<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<title>Login</title>
		<meta content="text/html; charset=utf-8" http-equiv="content-type" />
		<script src="../js/formfocus.js"></script>
		<#if login_error??>
			<script src="../js/jquery-1.3.2.min.js"></script>
			<script src="../js/springxt.js"></script>
			<script>
				jQuery.noConflict();
				function passwordReset() {
					jQuery('#remindForm').hide();
					jQuery('#error').hide();  
					jQuery('#passwordReset').show();
					jQuery('#loginForm').show(); 
				}
				function passwordResetError(p) {
					jQuery('#error').text(p.message); 
				}
			</script>
		</#if>
		<link rel="stylesheet" href="../login.css" type="text/css" media="screen"/>	
		<link rel="stylesheet" href="../error.css" type="text/css" media="screen"/>							
	</head>
<body onload="setFocus();">
	<div id="login">
		<div id="loginFormBlock">
		<p id="passwordReset" style="display:none;">
			Slaptažodis buvo sėkmingai pakeistas ir išsiųstas jums.
		</p> 
		<#if login_error??>
			<p id="error" class="errorBox">Nurodydas neteisingas slaptažodis arba e-pašto adresas. <br/><a href="" onclick="jQuery('#loginForm').hide(); jQuery('#remindForm').show(); return false;">Pamiršau slaptažodį</a></p>
		</#if>
		<form id="loginForm" action="loginProcess" method="POST">
			<p><label>E-paštas: </label><input type="text" name="j_username" onfocus="bUsernameHasFocus = true;" onblur="bUsernameHasFocus = false;" id="username" value="" maxlength="256"/></p>
			<p><label>Slaptažodis:</label> <input type="password" name="j_password" onfocus="bPasswordHasFocus = true;" onblur="bPasswordHasFocus = false;" id="password" value="" maxlength="256"/></p>
			<p><label class="inline">Prisimink mane:</label><input id="remember_me" type="checkbox" name="_spring_security_remember_me" class="inline" onfocus="bRememberHasFocus = true;" onblur="bRememberHasFocus = false;" /></p>
			<p><input type="image"  class="imageButton" src="../img/login_btn.gif" alt="" value="prisijungti..."/></p>
		</form>
		<div id="remindForm" style="display:none;">
			<p><label>E-paštas: </label><input type="text" name="email" id="email" value="" maxlength="256"/></p>
			<p><input type="button" value="Priminti man slaptažodį" onclick="XT.doAjaxAction('resetPassword', this, {email: jQuery('#email').val()}); return false;"/></p>
		</div>
		</div>
	</div>
</body>
</html>