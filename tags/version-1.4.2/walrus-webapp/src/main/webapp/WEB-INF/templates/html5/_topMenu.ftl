<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]>
<#include "_menuMacros.ftl"/>

<#import "_macro.ftl" as walrus/>

            <nav id="menu">
                <div>

						<a href="${model.contextPath}/cms/index">Home</a>
						<@sec.authorize ifAllGranted="ROLE_ADMIN">
							<@walrus.addSubRubric model.site.rootRubric.children[0]/>
						</@sec.authorize>
						<#list model.site.rootRubric.children[0].children as rubric>
							<#if canDrawInMenu(rubric)>
								<@walrus.deleteRubric rubric/><@walrus.drawRubricLink rubric/> <#-- @parseSubrubrics rubric/-->
							</#if>
						</#list>
				</div>
			</nav>
