<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]/><#import "_macro.ftl" as walrus/><?xml version="1.0" encoding="utf-8"?>
<rss version="2.0">
<#if model.currentRubric == model.site.rootRubric>
	<#assign 	rssRubric=model.site.rootRubric/>
<#else/>
	<#assign 	rssRubric=model.currentRubric/>
</#if>
	<channel>
		<title>Walrus - ${rssRubric.title}</title>
		<link>${model.fullContextPath}/cms/index<#if rssRubric??>?rubricId=${rssRubric.id}</#if></link>
		<description>Walrus<#if rssRubric??>, rubrika - ${rssRubric.title}</#if></description>
		<language>lt-lt</language>
		<generator>Walrus CMS - http://www.walrus.lt</generator>
		<#if rssRubric??>
			<#list rssRubric.children as rubric>
				<item>
					<pubDate>${rubric.date}</pubDate>
					<title>${rubric.title}</title>
					<link><#if rubric.url?? && (rubric.url?length>0)>${model.fullContextPath}${model.staticServletPath}/${rubric.url}<#else/>${model.fullContextPath}/cms/index?rubricId=${rubric.id}</#if></link>
					<guid><#if rubric.url?? && (rubric.url?length>0)>${model.fullContextPath}${model.staticServletPath}/${rubric.url}<#else/>${model.fullContextPath}/cms/index?rubricId=${rubric.id}</#if></guid>
					<description>${rubric.abstr!""?html()}</description>
				</item>
			</#list>
		</#if>
</channel>
</rss>