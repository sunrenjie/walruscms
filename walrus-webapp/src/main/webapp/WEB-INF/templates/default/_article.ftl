<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>

<#import "_macro.ftl" as walrus/>

<#macro drawArticle article drawTitle=true>
		<#if drawTitle &&(model.isAdmin?? || (article.title?? && article.title?trim?length > 0))>
			<h2 class="edit" id="article_title_${article.id}">${article.title?html}</h2>
		</#if>
		<#if model.isAdmin??>
			<@walrus.articleControls article/>
		</#if>
	<#if model.isAdmin?? || (article.date?? && article.date?trim?length > 0)>
		<p class="edit article_date text" id="article_date_${article.id}">${article.date!""}</p>
	</#if>
	
	<div class="article text" id="article_body_${article.id}" <#if model.isAdmin??>onclick="convertToEditor(this);return null;"</#if>>${article.body}<#if article.body == ""><#if model.isAdmin??>(<@spring.message code="walrus.clickHere"/>)<#else>(<@spring.message code="walrus.noinfo"/>)</#if></#if></div>
</#macro>

<#macro showArticleListEntry article isArchive>
	<#local class=""/>
	<#if model.currentRubric?? && article.id == model.currentRubric.id>
		<#local class="current"/>
	</#if>
	<a class="${class}" href="index?rubricId=${article.id}">${article.title?html}<#if '' == article.title><@spring.message code="walrus.noname" /></#if></a>
	<#if model.isAdmin??>
		<#if !article.isOnline()> [OFFLINE]</#if>	
		<#if !isArchive>
			<#if article.isPending()> [PENDING]</#if>
		</#if>
	</#if>	
</#macro>