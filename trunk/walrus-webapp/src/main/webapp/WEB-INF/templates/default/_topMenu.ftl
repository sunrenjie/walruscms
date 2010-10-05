<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]>
<#include "_menuMacros.ftl"/>

<#import "_macro.ftl" as walrus/>

<ul id="menu" class="clearfix">
						<li><a href="${model.contextPath}/cms/index">Home</a></li>
						<@sec.authorize ifAllGranted="ROLE_ADMIN">
							<li><@walrus.addSubRubric model.site.rootRubric.children[0]/></li>
						</@sec.authorize>
						<#list model.site.rootRubric.children[0].children as rubric>
							<#if canDrawInMenu(rubric)>
								<li><@walrus.deleteRubric rubric/><@walrus.drawRubricLink rubric/><#-- @parseSubrubrics rubric/--></li>
							</#if>
						</#list>
						<#-- @sec.authorize ifAllGranted="ROLE_ADMIN">
							<li><a href="${model.contextPath}/cms/usageReport">Redagavimo statistika</a></li>
							<li><a href="${model.contextPath}/cms/users">Vartotojų administravimas</a></li>
						</@sec.authorize -->	
</ul>

