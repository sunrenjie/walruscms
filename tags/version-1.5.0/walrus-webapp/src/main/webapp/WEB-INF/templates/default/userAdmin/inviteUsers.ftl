<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]/>
<div class="administrationPage">
<h1>Vartotojų kvietimas</h1>
		<p>Nurodykite e-pašto adresų sąrašą ir siunčiamo e-laiško tekstą.</p>
		<@form.form modelAttribute="inviteCommand">
			<div id="field">
				<div class="label">
					<label for="emails">Kviečiamų vartotojų e-pašto adresai (atskirti tarpais):</label>
				</div>
				<div class="input">
					<@form.textarea path="emails" cols="60" rows="10"/>
				</div>
			</div>
			
			<div id="field">
				<div class="label">
					<label for="comment">Laiško vartotojui tekstas:</label>
				</div>
				<div class="input">
					<@form.textarea path="comment" cols="60" rows="10"/>
				</div>
			</div>
			
			<div id="messagesInsertionPoint"><@form.errors path="*"/></div>
			
			<div class="buttonGroup">
				<input type="submit" name="_eventId_submit" value="Siųsti"/>
				<input type="submit" name="_eventId_cancel" value="Atšaukti"/>
			</div>
			
		</@form.form>
</div>
