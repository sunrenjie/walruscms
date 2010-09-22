<#include "_article.ftl"/>
<#include "_rubric.ftl"/>
<#include "_navpath.ftl"/>
<#import "_macro.ftl" as walrus/>

<div id="rubric">
	<span id="navpath"><a href="${model.contextPath}/cms/index"><@spring.message code="walrus.first.page"/></a> &gt; <a href="${model.contextPath}/cms/features<#if model.featureAllSites??>?allSites=yes</#if>">Naujienos</a></span>
	<p>&nbsp;</p>
	<h1>Naujienos<#if model.isArchive > (<@spring.message code="walrus.archive" />)</#if></h1>
	<@drawFeatures model.features />
	<#if !model.isArchive >
		<p class="hr">&nbsp;</p>
		<p id="archiveLink"><a href="features?<#if model.featureAllSites??>allSites=yes&</#if><#if model.main??>main=yes&</#if>archive=1"><@spring.message code="walrus.archive" /></a></p>
	</#if>
	
</div>