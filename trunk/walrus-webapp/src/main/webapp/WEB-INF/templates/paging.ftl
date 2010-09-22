<#macro drawPaging valueListInfo pagesToScroll>
	<#if valueListInfo?? && (valueListInfo.totalNumberOfEntries > 0)> 
<p class="common-paging">
	Puslapiai: <@paging valueListInfo pagesToScroll/>
</p>
</#if>
</#macro>

<#macro header title property valueListInfo sortable="not_defined" attr="">
		<th ${attr} nowrap="true">${title?html}&nbsp;
			<#if sortable?? && sortable!="not_defined">
			
				<#assign currentSortDirectionImgCode = "sort.gif"/>
				<#if sortable=="desc">
					<#assign sortDirection = "-1"/>
				<#else/>
					<#assign sortDirection = "1"/>
				</#if>
				
				<#if valueListInfo.sortingColumn?? && 
					valueListInfo.sortingColumn == property>
					<#if valueListInfo.sortingDirection == 1>
						<#assign sortDirection = "-1"/>
						<#assign currentSortDirectionImgCode = "sort_up.gif"/>
					<#else/>
						<#assign sortDirection = "1"/>
						<#assign currentSortDirectionImgCode = "sort_down.gif"/>
					</#if>
				</#if>
				<a id="sort_${property}" href="#" 
					onclick="Spring.remoting.submitForm('sort_${property}', 'list', {
						_eventId:'sort', 
						pagingNumberPer: '${valueListInfo.pagingNumberPer}', 
						pagingPage: '1', 
						sortColumn: '${property}', 
						sortDirection: '${sortDirection}'
					}); return false;"><img src="../img/${currentSortDirectionImgCode}" border="0" /></a>&nbsp;
			</#if>
		&nbsp;</th>
</#macro>

<#macro column property="~" class="classicLook0">
	<td class=${class}>${property?html}</td>
</#macro>

<#macro paging valueListInfo pages=5>
	<#if (valueListInfo.pagingPage != 1)>
		<a href="#" 
					onclick="Spring.remoting.submitForm(this, 'list', {
						_eventId:'getPage', 
						pagingNumberPer: '${valueListInfo.pagingNumberPer}', 
						pagingPage: '1', 
					}); return false;
			">&lt;&lt;Pirmas</a>
	</#if>
	
	<#if (valueListInfo.pagingPage > 1)>
		<#assign __page = valueListInfo.pagingPage-1/>
		<a href="#" 
					onclick="Spring.remoting.submitForm(this, 'list', {
						_eventId:'getPage', 
						pagingNumberPer: '${valueListInfo.pagingNumberPer}', 
						pagingPage: '${__page}', 
					}); return false;
			">&lt;Atgal</a>
	</#if>
	
	<#assign __currentPage=valueListInfo.pagingPage - (pages / 2)?int/> 
	<#if (__currentPage < 1)>
		<#assign __currentPage=1/> 
	</#if>
	
	<#assign __maxPage=(__currentPage - 1) + pages/> 
	<#if ((__maxPage > valueListInfo.totalNumberOfPages))>
		<#assign __currentPage=__currentPage - (__maxPage - valueListInfo.totalNumberOfPages)/> 
		<#assign __maxPage=valueListInfo.totalNumberOfPages/> 
	</#if>
		
	<#if (__maxPage < 2)>
		<#assign __maxPage=0/> 
	</#if>
	
	<#if (__currentPage < 1)>
		<#assign __currentPage=1/> 
	</#if>
	<#if __maxPage==0>
		 ${__currentPage}&nbsp;
	<#else/>
		<#list __currentPage..__maxPage as p>
			 <#assign __page = p/>
			 <#if p == valueListInfo.pagingPage>
				 ${p}&nbsp;
			 <#else>	
				<a href="#" 
							onclick="Spring.remoting.submitForm(this, 'list', {
								_eventId:'getPage', 
								pagingNumberPer: '${valueListInfo.pagingNumberPer}', 
								pagingPage: '${__page}'
							}); return false;
					">${p}</a>
			</#if>
		</#list>
	</#if>
	
	<#if (valueListInfo.pagingPage < valueListInfo.totalNumberOfPages)>
		 <#assign __page = valueListInfo.pagingPage+1/>
				<a href="#" 
							onclick="Spring.remoting.submitForm(this, 'list', {
								_eventId:'getPage', 
								pagingNumberPer: '${valueListInfo.pagingNumberPer}', 
								pagingPage: '${__page}'
							}); return false;
					">Pirmyn&gt;</a>
	</#if>
	
	<#if (valueListInfo.pagingPage != valueListInfo.totalNumberOfPages)>
				<a id="lastPage" href="#" 
							onclick="Spring.remoting.submitForm(this, 'list', {
								_eventId:'getPage', 
								pagingNumberPer: '${valueListInfo.pagingNumberPer}', 
								pagingPage: '${valueListInfo.totalNumberOfPages}'
							}); return false;
					">Paskutinis&gt;&gt;</a>
	</#if>
</#macro>

<#function makeUrl valueListInfo paramHash={}>
	<#assign url>
TODO	</#assign>
	<#return url>
</#function>

<#macro makePortletParam name value paramHash={}>
	<#if paramHash[name]??>
		TODO
	<#else/>
		TODO
	</#if>
</#macro>	

<#macro drawListNumberColumn valueListInfo listNr class>
	<#assign nr = valueListInfo.pagingNumberPer*(valueListInfo.pagingPage-1) + listNr + 1>
	<@column property=nr class=class/>
</#macro>