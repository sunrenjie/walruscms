<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>
<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]/>
<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>

<#import "_article.ftl" as article />
<#include "_navpath.ftl" />
<#import "_box.ftl" as box/>
<#import "_macro.ftl" as walrus/>
<#include "_menuMacros.ftl"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <!--[if IE]><![endif]-->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    
    <title></title>
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0;">

    
    <link rel="shortcut icon" href="/favicon.ico">
    <link rel="apple-touch-icon" href="/apple-touch-icon.png">
    
    <link rel="stylesheet" href="${model.contextPath}/styles/defaults.css?v=1">
    <link rel="stylesheet" href="${model.contextPath}/styles/styles.css?v=1">
    <link rel="stylesheet" media="handheld" href="${model.contextPath}/styles/handheld.css?v=1">
 
    <script src="${model.contextPath}/scripts/modernizr-1.5.js"></script>
    <script src="${model.contextPath}/scripts/lib.js"></script>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Walrus CMS <@tiles.insertAttribute name="title"/></title>
	<script src="${model.contextPath}/js/jquery-1.3.2.min.js"></script>
	
	<@walrus.adminScripts />
	
	<#if model.isAdmin??>
		<script src="${model.contextPath}/js/slides-admin.js"></script>
	</#if>

	<#if model.currentRubric == model.site.rootRubric>
		<script>
			//<![CDATA[
				<@box.prepareSlideshow 'indexSlides' />
			//]]>
		</script>	
		<script src="${model.contextPath}/js/slides.js"></script>
		<#if model.isAdmin??>
			<script src="${model.contextPath}/js/slides-admin.js"></script>
		</#if>
	</#if>

	<link rel="alternate" type="application/rss+xml" title="Walrus - RSS" href="${model.contextPath}/cms/index?rss&rubricId=${model.currentRubric.id}" />
		
	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<link rel="stylesheet"  href="${model.contextPath}/userAdmin.css" type="text/css" media="screen"/>
	</@sec.authorize>
</head>

<!--[if lt IE 7 ]> <body class="ie6"> <![endif]-->
<!--[if IE 7 ]>    <body class="ie7"> <![endif]-->
<!--[if IE 8 ]>    <body class="ie8"> <![endif]-->
<!--[if IE 9 ]>    <body class="ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <body> <!--<![endif]-->

	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<@tiles.insertAttribute name="commandManager"/>
	</@sec.authorize>
	
<div id="wrapper" class="clearfix">
        <header id="header" class="clearfix">
            <h1>WalrusCMS</h1>
					<#include "_topMenu.ftl"/>
        </header>
		<div id="main" class="clearfix">
            <div id="sidebar-left" class="sidebar clearfix">
                <section>
					<ul>
						<li><a href="${model.contextPath}/cms/index?rss" class="ico rss">Get updates by RSS</a></li>
						<li><a href="mailto:info@walrus.lt" class="ico email">Write us!</a></li>
					</ul>
                </section>
                <section>
					<@box.showNewsBox "menuBox" />
                </section>
                <section>
					<@box.showNewsBox "newsBox" />
                </section>
            </div>
            <div id="content" class="clearfix">
                <@tiles.insertAttribute name="content"/>
            </div>

            <div id="sidebar-right" class="sidebar clearfix">
                <section>
                	<@box.showNewsBox "cultureBox" true/>
                </section>
                <section>
					<@box.showTextBox "ultraTextBox2" true/>
                </section>
                <section>
					<@box.showBannerBox "rightBanner1"/>
					<@box.showBannerBox "rightBanner2"/>
					<@box.showBannerBox "rightBanner3"/>
                </section>
            </div>
		</div>
		<footer class="clearfix">
			Walrus was started in dark labs of <a href="http://www.megalogika.lt">MEGALOGIKA</a>,
			now walrus is free software. <a href="http://www.walrus.lt">walrus.lt</a>
			<@walrus.loginLogout /> 
				<@sec.authorize ifAllGranted="ROLE_ADMIN">	
					| <a href="${model.contextPath}/cms/tree"><@spring.message code="walrus.tree"/></a>
				</@sec.authorize>
			
        </footer>
    </div>
	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<div id="errorBox">
			<h2>You've made an error</h2>
			<p id="error"></p>
			<p><a href="#" onclick="jQuery('#errorBox').hide(); return false;">OK</a></p>
		</div>
	</@sec.authorize>
			
<@sec.authorize ifAllGranted="ROLE_ADMIN">
	<@tiles.insertAttribute name="bottomControls" />
</@sec.authorize>	
<script type="text/javascript" charset="utf-8">
		//<![CDATA[
			var dom = {
				hasClass: function(elem, klass) {
					return new RegExp("(^|\\s)" + klass + "(\\s|$)")
						.test(elem.className);
				},
			
				addClass: function(elem, klass) {
					if (!this.hasClass(elem, klass))
						elem.className += (elem.className ? " " : "") + klass;
					return elem;
				},
			
				removeClass: function(elem, klass) {
					elem.className = elem.className
						.replace(new RegExp("(^|\\s)" + klass + "(\\s|$)"), "$2");
					return elem;
				},
			
				toggleClass: function(elem, klass) {
					return this[(this.hasClass(elem, klass) ? 'remove' : 'add') +'Class'](elem, klass);
				}
			};

<#--			
			var invisible = document.getElementById("toggle-invisible");
			dom.addClass(invisible, "hidden");
			var toggleInvisible = function() { dom.toggleClass(invisible, "hidden"); }
-->
			function hideShowComments() {
				var comments = document.getElementById("commentList");
				if(comments) {
					dom.toggleClass(comments, "hidden");
				}
			}
			
			function toggleComments(obj, noOfComments) {
				var link = jQuery(obj);
				if (link.attr('title') && link.attr('title') == 'Hide comments') { // true == uždarinėjam komentarus
					hideShowComments();
					link.attr('title', 'Read comments');
					link.text('Comments (' + noOfComments + ')');
				} else {	// atidarinėjam komentarus
					link.attr('title', 'Hide comments');
					hideShowComments();
					link.text('Hide comments');	
				}
			}			
			
			var search = document.getElementById("searchInput");
			var searchDefaultValue = search.value;
			
			search.onfocus = function() {
				if (search.value == searchDefaultValue) {
					dom.removeClass(search, "blured");
					search.value = "";
				}
			}
			
			search.onblur = function() {
				if (search.value == "") {
					dom.addClass(search, "blured");
					search.value = searchDefaultValue;
				}
			}
		//]]>
</script>
<script src="${model.contextPath}/scripts/widgets.js"></script>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-1622602-8']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script></body>
</html>
