<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]/>

		<div class="field">
			<div class="label">
				<label>Slaptažodis:</label>
			</div>
			<div class="input">
				<@form.password path="password"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		<div class="field">
			<div class="label">
				<label>Pakartokite slaptažodį:</label>
			</div>
			<div class="input">
				<@form.password path="passwordRepeat"/>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		