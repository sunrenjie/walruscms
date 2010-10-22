<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>
<#import "_box.ftl" as box/>
    <div id="intro" class="mb">
        <section class="clearfix">
				<@box.showTextBox "topTextBox1" true/>
        </section>
    </div>

	<@box.drawSlideshow "indexSlides" />
