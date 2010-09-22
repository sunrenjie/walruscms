<#import "../paging.ftl" as p />
<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]/>

<script type="text/javascript" src="${model.contextPath}/resources/dojo/dojo.js"> </script>
<script type="text/javascript" src="${model.contextPath}/resources/spring/Spring.js" />"> </script>
<script type="text/javascript" src="${model.contextPath}/resources/spring/Spring-Dojo.js"> </script>

<div class="administrationPage">
        
<h1>Vartotojų administravimas</h1>

<@form.form modelAttribute="list" cssClass="itemList">
<p><a href="#" id="invite" onclick="Spring.remoting.submitForm('invite', 'list', {_eventId:'invite', fragments: 'content'}); return false;">kviesti vartotojus</a> | 
<a href="#" id="massMail" onclick="Spring.remoting.submitForm('massMail', 'list', {_eventId:'massMail', fragments: 'content'}); return false;">siųsti naujienlaiškį</a>
</p>
<table cellspacing="0" cellpadding="0">
	<#if valueListInfo?? && (valueListInfo.totalNumberOfEntries > 0)> 
		<tr>
	     	<@p.header title="Pavardė" property="lastName" valueListInfo=valueListInfo sortable="desc"/>
	     	<@p.header title="Vardas" property="firstName" valueListInfo=valueListInfo sortable="desc"/>
	     	<@p.header title="Organizacija" property="company" valueListInfo=valueListInfo sortable="desc"/>
	     	<@p.header title="Pareigos" property="position" valueListInfo=valueListInfo sortable="desc"/>
	     	<@p.header title="Veiksmai" property="" valueListInfo=valueListInfo/>
		</tr>
	
          <#if list??>
			<#list list as v>
				<#assign user=v[0]/>
				<#if userSearchCriteria.isSelected(user.id)>
					<#assign class="selected"/>
				<#else/>	
					<#assign class=""/>
				</#if>
				
				<tr class="${class}">
					<#if user.lastName??>
					 <@p.column property=user.lastName/>
					<#else/>
					 <@p.column property=user.email/>
					</#if>
					 <@p.column property=user.firstName/>
					 <@p.column property=user.company/>
					 <@p.column property=user.position/>
					 <td>
						<div class="actionPark">
							<#if !user.isRegistrationPending()>
								<a href="#" id="select_${user.id}" onclick="Spring.remoting.submitForm('', 'list', {id: '${user.id}', _eventId:'select'}); return false">žymėti</a> 
								<a href="#" id="view_${user.id}" onclick="Spring.remoting.submitForm('view_${user.id}', 'list', {id: '${user.id}', _eventId:'view'}); return false;">žiūrėti</a>
								<a href="#" id="changePassword_${user.id}" onclick="Spring.remoting.submitForm('changePassword_${user.id}', 'list', {id: '${user.id}', _eventId:'changePassword'}); return false;">keisti slaptažodį</a>
							</#if>
							<a href="#" id="delete_${user.id}" onclick="Spring.remoting.submitForm('delete_${user.id}', 'list', {id: '${user.id}', _eventId:'deleteUser', fragments: 'content'}); return false;">trinti</a>
						</div>
					</td>
				 </tr>
			</#list>
		</#if>
	<#else>
		<tr><th><h2>Nėra vartotojų</h2></th></tr>
	</#if>
</table>
<@p.drawPaging valueListInfo 10/>
</div>
</@form.form>