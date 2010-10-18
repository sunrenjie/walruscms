<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#import "_macro.ftl" as walrus/>

<#macro navpath rubric>
	<span id="navpath"><@concatNavPath rubric /></span>
</#macro>

<#macro concatNavPath rubric>
	<#if rubric?? >
		 <#if rubric.parent?? >
		 	<#if rubric.parent.parent?? > <#-- neminim ROOT rubrikos, nes ji bus ne į temą -->
		 		<@concatNavPath rubric.parent />
		 	<#else/>
		 		<a href="${model.contextPath}/cms/index"><@spring.message code="walrus.first.page"/></a>
		 	</#if>
		 </#if> &gt; <@walrus.drawRubricLink rubric/>
	<#else/>
		&nbsp;
	</#if>
</#macro>