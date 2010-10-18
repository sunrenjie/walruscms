<#import "_macro.ftl" as walrus/>
<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>
<@sec.authorize ifAllGranted="ROLE_ADMIN">
	<script src="${model.contextPath}/js/interface-1.2.js"></script>
	<script src="${model.contextPath}/js/inestedsortable.js"></script>
	<script>
			jQuery.noConflict();
			jQuery( function($) {
			jQuery('#tree').NestedSortable(
				{
					accept: 'sortable',
					noNestingClass: "no-nesting",
					opacity: 0.8,
					helperclass: 'helper',
					onChange: function(serialized) {
						//pageTracker._trackEvent('Demo - rubrics', 'sortTree');
						//XT.doAjaxAction("saveSort", null, {'sorted' : serialized[0].o});
						XT.doAjaxSubmit("saveSort", null, {'sorted' : serialized[0].o}, {formId: 'sortForm'});
					},
					autoScroll: true,
					handle: '.sort-handle',
					fx: 400,
					serializeRegExp: /.*/
				}
				);
			});
	</script>
</@sec.authorize>
<div id="rubric">
	<@makeTree model.site.rootRubric.children, "1"/>
	<form id="sortForm" method="post">
	</form>
</div>

<#macro makeTree rubricList index>
		<ul <#if index == "1">id="tree"</#if> class="treeList">
		<#list rubricList as rubric>
			<#if rubric??>
				<#if model.isAdmin?? || rubric.online>
				<#assign id>r.${rubric.id}</#assign>
				<li id="${id}" class="sortable">
					<span class="sort-handle">
						<#if model.isAdmin??><#if rubric.online><img src="${model.contextPath}/img/article.gif"/><#else/><img src="${model.contextPath}/img/article_off.gif"/></#if> </#if>
					</span>
					<@walrus.drawRubricLink rubric/> <#if model.isAdmin??>&nbsp;</#if> <#if !rubric.online>[OFFLINE]</#if>
					<#if (rubric.children?size > 0)>
						<@makeTree rubric.children, id/>
					</#if>
				</li>
				</#if>
			<#else/>
			</#if>
		</#list>
		</ul>
</#macro>