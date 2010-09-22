<#if banners?? && (banners?size>0)>
	<#list banners as banner>
		<div class="bannerEditor-banner" id="banner_${boxId}_${banner.id}">
			<img onclick="bannerUrlEditor(this);return false;" alt="${banner.url}" title="Koreguoti banerį - ${banner.url}" id="bannerimg_${boxId}_${banner.id}" src="${contextPath}${banner.banner}" class="banner" /><br/>
			<a class="bannerDelete" title="Trinti banerį" href="#" onclick="if(confirm('Ar tikrai?'))deleteBanner('${boxId}', '${banner.id}');return false;"><img src="../img/menu_handle.png" alt="Trinti banerį" title="Trinti banerį" style="width: 13px; height: 13px; margin: 0px;"/></a>
		</div>
	</#list>
</#if>