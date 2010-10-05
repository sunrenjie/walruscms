<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>

<script type="text/javascript" src="${model.contextPath}/resources/dojo/dojo.js"> </script>
<script type="text/javascript" src="${model.contextPath}/resources/spring/Spring.js" />"> </script>
<script type="text/javascript" src="${model.contextPath}/resources/spring/Spring-Dojo.js"> </script>

<script type="text/javascript" src="${model.contextPath}/js/jquery-1.2.6.js"> </script>
<script type="text/javascript" src="${model.contextPath}/js/calendarDateInput.js"></script>

<div class="administrationPage">
	<h1><@tiles.insertAttribute name="title"/></h1>
	<@tiles.insertAttribute name="description"/>
	
	<@form.form modelAttribute="user" cssClass="itemList">
		<div class="field">
			<div class="label">
				<label>Vardas:</label>
			</div>
			<div class="input">
				<@form.input path="firstName" maxlength="40"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		<div class="field">
			<div class="label">
				<label>Pavardė:</label>
			</div>
			<div class="input">
				<@form.input path="lastName" maxlength="40"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		<div class="field">
			<div class="label">
				<label>Gimimo data:</label>
			</div>
			<div class="input">
				<script>
				DateInput('birthDay', true, 'YYYY-MM-DD'<#if user.birthDay?? && (user.birthDay?string('yyyy-MM-dd')?length==10)>, '${user.birthDay?string('yyyy-MM-dd')}'</#if>
				);
				</script>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		
		<div class="field">
			<div class="label">
				<label>Atstovaujama organizacija:</label>
			</div>
			<div class="input">
				<@form.input path="company" maxlength="255"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		<div class="field">
			<div class="label">
				<label>Pareigos organizacijoje:</label>
			</div>
			<div class="input">
				<@form.input path="position" maxlength="40"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		
		<div class="field">
			<div class="label">
				<label>Organizacijos adresas:</label>
			</div>
			<div class="input">
				<@form.input path="companyAddress" maxlength="255"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>

		<div class="field">
			<div class="label">
				<label>Telefono numeris:</label>
			</div>
			<div class="input">
				<@form.input path="phone" maxlength="40"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>


		<div class="field">
			<div class="label">
				<label for="email">E-pašto adresas:</label>
			</div>
			<div class="input">
				<@form.input path="email" maxlength="40"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		
		<@tiles.insertAttribute name="passwordFields"/>
				
		<div class="field">
			<div class="label">
				<label>Trumpas prisistatymas:</label>
			</div>
			<div class="input">
				<@form.textarea path="aboutMe"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		
	<@sec.authorize ifAllGranted="ROLE_ADMIN">	
		<div class="field">
			<div class="label">
				<label>Vartotojo rolė:</label>
			</div>
			<div class="input">
				<@form.select path="role">
					<@form.option value="ROLE_ADMIN" label="administratorius"/>
					<@form.option value="ROLE_USER" label="vartotojas"/>
				</@form.select>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
	</@sec.authorize>

		<div id="messagesInsertionPoint"><@form.errors path="*"/></div>
	
		<div class="buttonGroup">
			<@tiles.insertAttribute name="buttons"/>
		</div>
	</@form.form>
</div>