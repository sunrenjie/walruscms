<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>
<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#import "_macro.ftl" as walrus />
<#include "_menuMacros.ftl"/>

<#function findBox list boxId>
	<#list list as box>
		<#if box.boxId == boxId>
			<#return box/>
		</#if>
	</#list>
</#function>

<#macro showNewsBox boxId showTitle=true>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)??>
			<#assign box = findBox(model.site.boxes, boxId)/> 
			<#if box?? && box.rubric??>
				<#if showTitle>
					<h1>${box.rubric.title?xhtml}</h1>
				</#if>
				<#if box.rubric.children??>
				<ul id="box_${boxId}">
					<@sec.authorize ifAllGranted="ROLE_ADMIN">
						<li><a href="#"><@walrus.addSubRubric box.rubric/></a></li>
					</@sec.authorize>			
					<@drawMenuItems box.rubric.children/>		
				</ul>
				</#if>
			</#if>
		</#if>
	</#if>
</#macro>

<#macro showBannerBox boxId>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)??>
			<#assign box = findBox(model.site.boxes, boxId) />
			
			<#if box??>
                <#if box.banners?? && (box.banners?size > 0)>
                	<#assign banner = box.randomBanner/>
                    <#if banner.url?? && '' != banner.url && banner.banner?? && '' != banner.banner>
						<div id="${boxId}" class="bannerBox">
	                    <a id="link_${boxId}" href="${banner.url}" <#if model.isAdmin??>onclick="return false;"</#if>><img alt="${banner.banner}" id="banner_${boxId}" src="${model.contextPath}${banner.banner}" class="banner" /></a>
						</div>
	                <#else/>
	                    <#if model.isAdmin??>
							<div id="${boxId}" class="bannerBox"><img id="banner_${boxId}" src="${model.contextPath}/img/sample_banner.jpg" class="banner" /></div>
	                    </#if>
                    </#if>                    
                <#else/>
                    <#if model.isAdmin??>
                    	<div id="${boxId}" class="bannerBox"><a href="#"><img id="banner_${boxId}" src="${model.contextPath}/img/sample_banner.jpg" class="banner" /></a></div>
                    </#if>
                </#if>
			</#if>
        <#else/>
          	<#if model.isAdmin??>DĖMESIO: nėra boxo su id ${boxId}</#if>
		</#if>
	</#if>
</#macro>

<#macro showImageBox boxId>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)??>
			<#assign box = findBox(model.site.boxes, boxId) />
			<div id="${boxId}" class="imageBox">
            <#if box.image?? && '' != box.image>
				<img alt="${box.image}" id="banner_${boxId}" src="${model.contextPath}${box.image}" class="banner" />
            <#else/>
	            <#if model.isAdmin??>
                <img id="banner_${boxId}" src="${model.contextPath}/img/sample_image.jpg" class="banner" />
                </#if>
            </#if>
        	</div>
        <#else/>
          	<#if model.isAdmin??>DĖMESIO: nėra boxo su id ${boxId}</#if>
		</#if>
	</#if>
</#macro>

<#macro showTextBox boxId showHeader=true>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)??>
			<#assign box = findBox(model.site.boxes, boxId)/>
			<#if showHeader> 
				<h1 class="height-fix-widget" class="edit" id="box_title_${boxId}">${box.title}</h1>
			</#if>
			<div id="box_body_${boxId}" class="line-height-oaah height-fix-widget textBoxBody" <#if model.isAdmin??>onclick="convertToEditor(this);return null;"</#if>>
				${box.body}<#if box.body?trim == "" && model.isAdmin??>(Click here)</#if>
			</div>
		</#if>
	</#if>
</#macro>

<#macro showFeatureBox boxId>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)??>
			<#assign box = findBox(model.site.boxes, boxId)/>
			<#if box??>
				<#if box.allSites?? && box.allSites>
					<#assign features = box.getFeatures(model.sites, false) />
				<#elseif box.main?? && box.main/>
					<#assign features = box.getMainFeatures(model.sites, false) />
				<#else/>
					<#assign features = box.getFeatures(model.site, false) />
				</#if>
				<#if features?? && (features?size > 0)>
					<#assign count = 0 />
					<#list features as feature>
						<#if walrus.canShowInList((feature.rubric), false)>
							<div class="item">
								<h1><a href="http://${feature.site.host}${model.contextPath}/cms/index?rubricId=${feature.rubric.id}">${feature.rubric.title}</a></h1>
								<#if feature.rubric.abstr?? && (feature.rubric.abstr?length > 0)>
									<div class="abstract">${feature.rubric.abstr}</div>
								</#if>
								<#assign count = count + 1 />
								<#-- cia galima isivesti i rubric boxa parametra, kiek jame rodoma max straipsniu... -->
							</div>
							<#if (count > 4) ><#break/></#if>
						</#if>
					</#list>
					<#if (features?size > 0)>
						<p id="moreNewsLink"><a href="features?<#if box.allSites?? && box.allSites>allSites=yes</#if><#if box.main?? && box.main>&main=yes</#if>"><@spring.message code="walrus.read.all.news"/></a></p>
					</#if>		
				</#if>
			</#if>
		</#if>
	</#if>
</#macro>


<#macro prepareSlideshow boxId>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)?? >
			<#assign slideshow = findBox(model.site.boxes, boxId) />
			
			<#if slideshow??>
				var slides = new Array();
				var slideIndex = 0;
				<#if slideshow.slides?? && 0 < slideshow.slides?size>
					<#list slideshow.slides as slide>
						slides.push('${slide.id}');
					</#list>
				</#if>
			</#if>
		</#if>
	</#if>
</#macro>

<#macro drawSlideshow boxId>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)?? >
			<#assign slideshow = findBox(model.site.boxes, boxId) />
		
			<#if slideshow??>
				<div id="${boxId}" class="mb slider-widget height-fix-widget slideshow">
					<section>
                        <header>
								<@drawSlideLinks slideshow boxId />
								<@sec.authorize ifAllGranted="ROLE_ADMIN">
									<a href="javascript:" class="slideAdder">[ + Add new slide]</a>
								</@sec.authorize>
                        </header>
						<div id="slider-slides" class="slider-widget-slides">
							<@drawSlides slideshow boxId />
						</div>
					</section>
				</div>
			<#else/>
				&nbsp;
				<@sec.authorize ifAllGranted="ROLE_ADMIN">
					NO SLIDESHOW FOR ${boxId}!!!
				</@sec.authorize>
			</#if>
		<#else/>
			&nbsp;
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				NO SUCH BOX: ${boxId}
			</@sec.authorize>
		</#if>
	<#else/>
		&nbsp;
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			NO SLIDESHOW BOX ID!!!
		</@sec.authorize>
	</#if>
</#macro>

<#macro drawSlides slideshow boxId >
	<#if slideshow.slides?? && 0 < slideshow.slides?size>
		<#assign count = 0 />
		<#list slideshow.slides as slide>
			<@drawSlide slide count />
			<#assign count = count + 1 />
		</#list>
	<#else/>
		&nbsp;
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			NO SLIDES IN SLIDESHOW ${boxId}!
		</@sec.authorize>
	</#if>	
</#macro>

<#macro drawSlideLinks slideshow boxId >
	<#if slideshow.slides?? && 0 < slideshow.slides?size>
		<#assign count = 0 />
		<#list slideshow.slides as slide>
			<@drawSlideLink slideshow slide count slideshow.slides?size />
			<#assign count = count + 1 />
		</#list>
	</#if>	
</#macro>

<#macro drawSlide slide count>
	<#if slide??>
		<div id="slide_${slide.id}" class="item <#if count?? && 0 == count>firstSlide</#if>">
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			<h1 class="edit height-fix-widget" id="slide_title_${slide.id}">${slide.title?xhtml}</h1>
			<div id="slide_body_${slide.id}" class="content" title="${slide.title?xhtml}" onclick="convertToEditor(this, function(){jQuery('.slideshow .controls').hide()}, function(){jQuery('.slideshow .controls').show()}, function(){jQuery('.slideshow .controls').show()});return null;">
		</@sec.authorize>
		<@sec.authorize ifNotGranted="ROLE_ADMIN">
			<div id="slide_body_${slide.id}" title="${slide.title?xhtml}" class="content">
		</@sec.authorize>	
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				<span class="slideadmin">
			</@sec.authorize>			
				${slide.body}
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				</span>
			</@sec.authorize>			
			</div>
		</div>		
	<#else/>
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			NO SLIDE!!!
		</@sec.authorize>
	</#if>
</#macro>

<#macro drawSlideLink slideshow slide count max>
	<#if slide??>
		<span id="slide_link_${slide.id}" <@sec.authorize ifAllGranted="ROLE_ADMIN">class="slideadmin"</@sec.authorize>>
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				<#if count?? && 0 < count>
					<a href="javascript:" onclick="moveSlide(${count}, -1);return false;"><img src="../img/left.png" alt="left"></a>
				</#if>
				<img class="deleteSlide" src="../img/menu_handle.png" onclick="if(confirm('Are you sure you want to delete this slide?')) delSlide('${slide.id}', '${slideshow.boxId}');">
			</@sec.authorize>
			<a id="slide_shortcut_${slide.id}" onclick="skipToSlide(${count}); return false;" class="shortcut <#if count?? && 0 == count>current</#if>" href="#">${slide.title}</a>
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				<#if count?? && count < (max - 1)>
					<a href="javascript:" onclick="moveSlide(${count}, 1);return false;"><img src="../img/right.png" alt="right"></a>
				</#if>
			</@sec.authorize>
			 
		</span>
		/
	<#else/>
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			NO SLIDE TO DRAW LINK TO!!!
		</@sec.authorize>
	</#if> 
</#macro>