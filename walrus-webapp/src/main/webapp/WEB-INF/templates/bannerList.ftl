<#if banners?? && (banners?size>0)>
	<#list banners as banner>
		<div class="bannerEditor-banner" id="banner_${boxId}_${banner.id}">
			<img onclick="bannerUrlEditor(this);return false;" alt="${banner.url}" title="Edit banner - ${banner.url}" id="bannerimg_${boxId}_${banner.id}" src="${contextPath}${banner.banner}" class="banner" /><br/>
			<a class="bannerDelete" title="Delete banner" href="#" onclick="if(confirm('Are you sure?'))deleteBanner('${boxId}', '${banner.id}');return false;"><img src="../img/menu_handle.png" alt="Delete banner" title="Delete banner" style="width: 13px; height: 13px; margin: 0px;"/></a>
		</div>
	</#list>
</#if>