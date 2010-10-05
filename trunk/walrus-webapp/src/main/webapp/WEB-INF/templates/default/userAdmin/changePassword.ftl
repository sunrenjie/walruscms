<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]/>
<div class="administrationPage">
<h1>Slaptažodžio keitimas</h1>
		<p>Keičiamas vartotojo ${user.firstName!""} ${user.lastName!""} (${user.email}) slaptažodis.</p>
		<@form.form modelAttribute="passwordChangeCommand">
			<div id="field">
				<div class="label">
					<label for="password">Įveskite naują slaptažodį:</label>
				</div>
				<div class="input">
					<@form.password path="password"/>
				</div>
			</div>
			
			<div id="field">
				<div class="label">
					<label for="repeatPassword">Pakartokite naują slaptažodį:</label>
				</div>
				<div class="input">
					<@form.password path="repeatPassword"/>
				</div>
			</div>
			
			<div id="messagesInsertionPoint"><@form.errors path="*"/></div>
			
			<div class="buttonGroup">
				<input type="submit" name="_eventId_submit" value="Keisti slaptažodį"/>
				<input type="submit" name="_eventId_cancel" value="Atšaukti"/>
			</div>
			
		</@form.form>
</div>
