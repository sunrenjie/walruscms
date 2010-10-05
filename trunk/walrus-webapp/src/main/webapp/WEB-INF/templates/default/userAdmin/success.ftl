<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="administrationPage">
	<h1><@tiles.insertAttribute name="title"/></h1>
	<p><@tiles.insertAttribute name="message"/></p>
	<@form.form>
	<input type="submit" value="OK" name="_eventId_ok"/>
	</@form.form>
</div>