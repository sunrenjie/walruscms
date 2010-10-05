<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]/>
<div class="administrationPage">
	<h1>Vartotojo duomenys</h1>
	<@form.form cssClass="itemList">
		<div class="field">
			<div class="label">
				<label>Vardas:</label>
			</div>
			<div class="input">
				${user.firstName!''}
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		<div class="field">
			<div class="label">
				<label>Pavardė:</label>
			</div>
			<div class="input">
				${user.lastName!''}
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		<div class="field">
			<div class="label">
				<label>Gimimo data:</label>
			</div>
			<div class="input">
				<#if user.birthDay??>
					${user.birthDay?string('yyyy-MM-dd')}
				</#if>
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		
		<div class="field">
			<div class="label">
				<label>Atstovaujama organizacija:</label>
			</div>
			<div class="input">
				${user.company!''}
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		<div class="field">
			<div class="label">
				<label>Pareigos organizacijoje:</label>
			</div>
			<div class="input">
				${user.position!''}
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		
		<div class="field">
			<div class="label">
				<label>Organizacijos adresas:</label>
			</div>
			<div class="input">
				${user.companyAddress!''}
			</div>
			<div class="clear">&nbsp;</div>
		</div>

		<div class="field">
			<div class="label">
				<label>Telefono numeris:</label>
			</div>
			<div class="input">
				${user.phone!''}
			</div>
			<div class="clear">&nbsp;</div>
		</div>


		<div class="field">
			<div class="label">
				<label for="email">E-pašto adresas:</label>
			</div>
			<div class="input">
				${user.email}
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		
		<div class="field">
			<div class="label">
				<label>Trumpas prisistatymas:</label>
			</div>
			<div class="input">
				${user.aboutMe!''}
			</div>
			<div class="clear">&nbsp;</div>
		</div>
		<div class="buttonGroup">
			<input type="submit" value="Grįžti į sąrašą" name="_eventId_return"/>
			<input type="submit" value="Koreguoti" name="_eventId_edit"/>
		</div>
	</@form.form>
</div>