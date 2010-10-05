<#import "_macro.ftl" as walrus/>
<#include "_menuMacros.ftl"/>
<@compress single_line=true>
<ul id="leftMenu">
	<#if model.site.rootRubric.children[1]??>
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			<li><a href="#"><@walrus.addSubRubric model.site.rootRubric.children[1]/></a></li>
		</@sec.authorize>			
		<@drawMenuItems model.site.rootRubric.children[1].children/>		
	<#else>
		<#if model.isAdmin??><p>Jūs turbūt sutvarkėte rubrikų medį taip, kad pirmame lygyje liko tik vienas meniu punktas. Šis meniu vaizduoja pirmame lygyje esančio antro punkto papunkčius. Žodžiu, padarykite taip, kad pirmame lygyje būtų LYGIAI DU meniu punktai.</p></#if>
	</#if>
</ul>
</@compress>