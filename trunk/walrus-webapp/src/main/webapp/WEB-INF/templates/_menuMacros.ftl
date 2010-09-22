<#import "_macro.ftl" as walrus/>

<#macro parseSubrubrics parent>
	<#if parent.children?? && (parent.children?size > 0)>
		<#list parent.children as rubric>
			<#if canDrawInMenu(rubric)>
				<#local drawSubMenu = true/>
			</#if>
		</#list>
		<#if drawSubMenu??>		
			<ul>
			<@drawMenuItems parent.children/>
			<#-- list parent.children as rubric>
				<#if (rubric.online || model.isAdmin??) && !rubric.leaf>
					<li <#if !rubric_has_next>class="last"</#if>>
					<@walrus.deleteRubric rubric/>
					<@walrus.drawRubricLink rubric/>
					<@parseSubrubrics rubric/>
					</li>
				</#if>
			</#list -->
			</ul>
		</#if>
	</#if>
</#macro>

<#function canDrawInMenu rubric>
	<#return walrus.canShowInList(rubric, false) && (!rubric.leaf || model.isAdmin??)/>
</#function>

<#macro drawMenuItems items>
	<#list items as rubric>
		<#if canDrawInMenu(rubric)>
			<li class="<#if !rubric_has_next>last </#if><@walrus.activeClass rubric />"><@walrus.deleteRubric rubric/><@walrus.drawRubricLink rubric/><@parseSubrubrics rubric/></li>
		</#if>
	</#list>
</#macro>
