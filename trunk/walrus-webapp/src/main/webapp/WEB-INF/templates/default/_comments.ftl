<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]>
	
	<#include "_commentForm.ftl" />
	<div id="comments">
		<#if (rubric.comments?? && rubric.comments?size > 0)>
			<a href="#" onclick="toggleComments(this, ${rubric.comments?size});return false;" title="Read comments">Comments (<span class="red">${rubric.comments?size}</span>)</a>
			<div class="list hidden" id="commentList">
				<#list rubric.comments as c>
							<div class="item<#if !c_has_next> last</#if>">
								<p class="header"><span class="commenter">${c.name?xhtml}</span> | ${c.date?string('yyyy-MM-dd HH:mm')}
								<@sec.authorize ifAllGranted="ROLE_ADMIN">
									| <a href="#" onclick="XT.doAjaxAction('deleteComment', this, {'commentId': '${c.id}'}); return false;">delete</a>
								</@sec.authorize>
								</p>
								<p>${c.body?xhtml}</p>				
							</div>
				</#list>
			</div>
		</#if>
	</div>