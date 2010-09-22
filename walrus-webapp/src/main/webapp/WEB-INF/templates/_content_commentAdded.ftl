<#include "_article.ftl"/>
<#include "_rubric.ftl"/>
<#include "_navpath.ftl"/>
<#import "_macro.ftl" as walrus/>

<div id="rubric">
	<span id="navpath"><a href="${model.contextPath}/cms/index"><@spring.message code="walrus.first.page"/></a> &gt; <a href="${model.contextPath}/cms/features<#if model.featureAllSites??>?allSites=yes</#if>">Naujienos</a></span>
	<p>&nbsp;</p>
	<h1>Comment is accepted</h1>
	<p>Thank you, your comment was published. <a href="index?rubricId=${comment.rubric.id}">Return</a></p>
	
</div>