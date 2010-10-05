<#include "_article.ftl"/>
<#include "_rubric.ftl"/>
<#include "_navpath.ftl"/>
<#import "_macro.ftl" as walrus/>

<div id="rubric">
	<@navpath model.currentRubric />

	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<#if !model.currentRubric.leaf && model.currentRubric.parent?? && (!model.currentRubric.parent.parent?? || !model.currentRubric.parent.parent.parent?? || !model.currentRubric.parent.parent.parent.parent??)>
			<@walrus.addSubRubric model.currentRubric/>
		</#if>
	</@sec.authorize>

	<#if model.isAdmin?? && (model.currentRubric != model.site.rootRubric)>
		<@walrus.rubricControls model.currentRubric/>
	</#if>

	<p>&nbsp;</p>
	
	<@drawRubric model.currentRubric />
</div>