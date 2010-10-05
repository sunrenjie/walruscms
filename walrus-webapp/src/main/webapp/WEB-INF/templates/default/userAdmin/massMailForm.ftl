<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]/>
<div class="administrationPage">
<h1>Naujienlaiškis</h1>
		<p>Nurodykite e-laiško tekstą.</p>
		<@form.form modelAttribute="massMailCommand">
			<#if (massMailCommand.users?size > 0)>
				<div id="field">
					<div class="label">
						<label>Vartotojų e-pašto adresai:</label>
					</div>
					<div class="input">
						<#list massMailCommand.users as user>
							${user.email}<#if user_has_next>,</#if>
						</#list>
					</div>
				</div>
			<#else/>
				<div id="field">
					<div class="label">
						Dėmesio: jūs nepasirinkote nei vieno vartotojo, dėl to naujienlaiškis bus siunčiamas <strong>VISIEMS</strong> vartotojams. Jei norite siųsti naujienlaiškį tik keliems vartotojams, spauskite mygtuką <em>Atšaukti</em>, ir pasirinkite vartotojus. Vartotojas pasirenkamas spragtelėjus ant jo pavardės pele.
					</div>
				</div>
			</#if>
			<div id="field">
				<div class="label">
					<label for="comment">Laiško vartotojui antraštė:</label>
				</div>
				<div class="input">
					<@form.input path="subject"/>
				</div>
			</div>
			<div id="field">
				<div class="label">
					<label for="comment">Laiško vartotojui tekstas:</label>
				</div>
				<div class="input">
					<@form.textarea path="text" cols="60" rows="10"/>
				</div>
			</div>
			
			<div id="messagesInsertionPoint"><@form.errors path="*"/></div>
			
			<div class="buttonGroup">
				<input type="submit" name="_eventId_submit" value="Siųsti"/>
				<input type="submit" name="_eventId_cancel" value="Atšaukti"/>
			</div>
			
		</@form.form>
</div>
