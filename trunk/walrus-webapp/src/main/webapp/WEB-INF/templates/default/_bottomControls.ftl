<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#import "_macro.ftl" as walrus/>
	<div id="bottomControls">
		<p> <@walrus.listTemplates />
			<!-- | <a href="${model.contextPath}/cms/tree"><@spring.message code="walrus.tree"/></a>
			|<@walrus.loginLogout /> -->
		</p>
	</div>
