<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]>

<#import "_macro.ftl" as walrus/>

<#macro drawSimpleList rubric>
	<#list rubric.children as rubric>
		<#if walrus.canShowInList(rubric, model.isArchive) >
			<@drawSimpleListEntry rubric />
		</#if>
	</#list>
</#macro>

<#macro drawSimpleListEntry rubric>
			<p class="hr">&nbsp;</p>
			<p class="date">${rubric.date!""}</p>
			<h2><@showArticleListEntry rubric model.isArchive /></h2>
</#macro>

<#macro drawSimpleFeatureEntry feature>
			<p class="hr">&nbsp;</p>
			<p class="date">${feature.rubric.date!""}</p>
			<h2><a href="http://${feature.site.host}${model.contextPath}/cms/index?rubricId=${feature.rubric.id}">${feature.rubric.title?html}<#if '' == feature.rubric.title><@spring.message code="walrus.noname" /></#if></a></h2>
</#macro>

<#macro drawFeatures features>
	<#list features as feature>
		<#if walrus.canShowInList(feature.rubric, model.isArchive) >
			<@drawSimpleFeatureEntry feature />
		</#if>
	</#list>
</#macro>

<#macro drawExpandedList rubric>
	<#list rubric.children as article>
		<#if walrus.canShowInList(article, model.isArchive) >
			<p class="hr">&nbsp;</p>
			<p class="date">${article.date!""}</p>
			<h2><@walrus.deleteRubric article/><@showArticleListEntry article model.isArchive /></h2>
			<#if article.abstr?? && article.abstr != "" || model.isAdmin??>
				<div id="rubric_abstract_${article.id}" onclick="convertToEditor(this); return null;">${article.abstr}<#if article.abstr == "" && model.isAdmin??>(Click here to enter an abstract)</#if></div>
			<#else/>
				<div id="rubric_abstract_${article.id}">&nbsp;</div>
			</#if>
		</#if>									
	</#list>
</#macro>

<#macro drawBlog rubric>
	<#list rubric.children as article>
		<#if walrus.canShowInList(article, model.isArchive)>
			<@drawArticle article />
		</#if>
	</#list>
</#macro>

<#macro drawRubricList rubric>
	<#if (rubric.children?size > 0)><#--  && rubric.body?length==0 && rubric.abstr?length==0 -->
		<#if rubric.mode == "SIMPLE_LIST">
			<@drawSimpleList rubric/>
		</#if>
		<#if rubric.mode == "EXPANDED_LIST">
			<@drawExpandedList rubric/>
		</#if>	
		<#-- if rubric.mode == "BLOG">
			<@drawBlog rubric/>
		</#if -->
		<#if rubric.mode == "NONE">
			<#if model.isAdmin?? >
				<@drawSimpleList rubric/>
			</#if>
		</#if>	
	</#if>
</#macro>

<#macro drawRubric rubric>
	<h1 class="edit" id="rubric_title_${rubric.id}">${rubric.title?html}<#if model.isArchive > (<@spring.message code="walrus.archive" />)</#if></h1>
	<p><span id="rubric_is_offline"><#if model.isAdmin?? && !rubric.online>[OFFLINE]</#if></span></p>
	
	<#if !model.isArchive>
		<#if (rubric.abstr?length>0) || (rubric.body?length>0) || model.isAdmin??>
			<#if model.isAdmin?? || (rubric.date?? && rubric.date?trim?length > 0) || (rubric.createdBy?? && rubric.createdBy.length > 0) || (rubric.contacts?? && rubric.contacts.length > 0)>
				<p class="articleMeta">
					<#if model.isAdmin?? || (rubric.date?? && rubric.date?trim?length > 0)>
						<span class="edit article_date" id="rubric_date_${rubric.id}">${rubric.date!""}</span></#if>
	
					<#-- if model.isAdmin?? || (rubric.createdBy?? && rubric.createdBy?trim?length > 0)>
						<span class="edit" id="rubric_createdBy_${rubric.id}">${rubric.createdBy!""}</span>
					</#if>
					
					<#if model.isAdmin?? || (rubric.contacts?? && rubric.contacts?trim?length > 0)>
						tel.: <span class="edit" id="rubric_contacts_${rubric.id}">${rubric.contacts!""}</span>
					</#if -->
				</p>
			</#if>
	
			<div class="article" id="rubric_abstract_${rubric.id}" <#if model.isAdmin??>onclick="convertToEditor(this);return null;"</#if>>${rubric.abstr}<#if rubric.abstr == "" && model.isAdmin??>(Click here to enter an abstract)</#if></div>
			<div class="article colorTables text" id="rubric_body_${rubric.id}" <#if model.isAdmin??>onclick="convertToEditor(this);return null;"</#if>>${rubric.body}<#if rubric.body == "" && model.isAdmin??>(Click here to enter rubric body)</#if></div>
		</#if>
	</#if>
	<span id="rubricList"><@drawRubricList rubric/></span>
	<#if rubric.hasArchive() && !model.isArchive >
		<p class="hr">&nbsp;</p>
		<p id="archiveLink"><a href="index?rubricId=${rubric.id}&archive=1"><@spring.message code="walrus.archive" /></a></p>
	</#if>
	<#if rubric.commentsAllowed>
		<#include "_comments.ftl" />
	</#if>
</#macro>