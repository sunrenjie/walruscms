<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN" "http://www.springframework.org/dtd/spring-beans.dtd">

<#function isBannerBox box>
	<#return box.class.canonicalName == "lt.walrus.model.BannerBox"/>
</#function>

<#function isImageBox box>
	<#return box.class.canonicalName == "lt.walrus.model.ImageBox"/>
</#function>

<#function isSlideShowBox box>
	<#return box.class.canonicalName == "lt.walrus.model.SlideshowBox"/>
</#function>

<#function isTextBox box>
	<#return box.class.canonicalName == "lt.walrus.model.TextBox"/>
</#function>

<#function isRubricBox box>
	<#return box.class.canonicalName == "lt.walrus.model.RubricBox"/>
</#function>

<#macro renderBoxSpecifics box>
	<#if isBannerBox(box)>
		<property name="banners">
			<list>
				<#list box.banners as banner>
					<bean class="lt.walrus.model.Banner">
						<property name="banner" value="${banner.banner}" />
						<property name="url" value="${banner.url}" />
					</bean>
				</#list>
			</list>
		</property>		
	<#elseif isImageBox(box)/>
		<property name="image" value="${box.image}" />
	<#elseif isSlideShowBox(box)/>
		<property name="slides">
			<list>
				<#list box.slides as slide>
					<bean class="lt.walrus.model.Slide">
						<property name="title" value="${slide.title?html}" />
						<property name="body" value="${slide.body?html}" />
						<property name="orderno" value="${slide.orderno}" />
					</bean>
				</#list>
			</list>
		</property>
	<#elseif isTextBox(box)/>
		<property name="title" value="${box.title?html}"/>
		<property name="body" value="${box.body?html}"/>
	<#elseif isRubricBox(box)/>
		<property name="rubric" ref="r${box.rubric.id}" />
	</#if>
</#macro>

<#macro rubricBean rubric>
	<bean id="r${rubric.id}" class="lt.walrus.model.Rubric">
		<constructor-arg><#if rubric.parent??><ref bean="r${rubric.parent.id}"/><#else/><null/></#if></constructor-arg>
		<constructor-arg><value>${rubric.title?html}</value></constructor-arg>
		<property name="online"><value><#if rubric.online>true<#else/>false</#if></value></property>
		<property name="abstr"><value>${rubric.abstr?html}</value></property>
		<property name="body"><value>${rubric.body?html}</value></property>
		<property name="date"><value>${rubric.date?html}</value></property>
		<property name="visibleForever"><value><#if rubric.visibleForever>true<#else/>false</#if></value></property>
		<property name="visibleFrom"><value>${rubric.visibleFrom?string("yyyy-MM-dd")}</value></property>
		<property name="visibleTo"><value>${rubric.visibleTo?string("yyyy-MM-dd")}</value></property>
		<property name="leaf"><value><#if rubric.leaf>true<#else/>false</#if></value></property>
		<property name="mode"><value>${rubric.mode}</value></property>
		<property name="url"><#if rubric.url??><value>${rubric.url}</value><#else/><null/></#if></property>
		<property name="orderno" value="${rubric.orderno}"/>
		<property name="commentsAllowed"><value><#if rubric.commentsAllowed>true<#else/>false</#if></value></property>
		<#if rubric.comments??>
		<property name="comments">
			<list>
				<#list rubric.comments as comment>
					<bean class="lt.walrus.model.Comment">
						<property name="name" value="${comment.name?html}"/>
						<property name="email" value="${comment.email?html}"/>
						<#if comment.website??><property name="website" value="${comment.website?html}"/></#if>
						<property name="body" value="${comment.body?html}"/>
						<property name="date" value="${comment.date?string("yyyy-MM-dd")}"/>
					</bean>
				</#list>
			</list>
		</property>
		</#if>
	</bean>
</#macro>
<#macro rubricTree rubric>
		<#list rubric.children as r>
			<@rubricBean r/>
		</#list>
		<#list rubric.children as r>
			<@rubricTree r/>
		</#list>
</#macro>

<beans>
	<@rubricBean model.site.rootRubric/>
	<@rubricTree model.site.rootRubric/>
	<bean id="sitePrototype" class="lt.walrus.model.Site" autowire="no">
		<constructor-arg>
			<ref bean="r${model.site.rootRubric.id}" />
		</constructor-arg>
		<property name="boxes">
			<list>
				<#list model.site.boxes as box>
					<bean class="${box.class.canonicalName}">
						<property name="boxId" value="${box.boxId}" />
						<@renderBoxSpecifics box/>
					</bean>
				</#list>
			</list>
		</property>
	</bean>

	<bean id="customEditorConfigurer" class="org.springframework.beans.factory.config.CustomEditorConfigurer">
		<property name="customEditors">
			<map>
				<entry key="java.util.Date">
					<bean class="org.springframework.beans.propertyeditors.CustomDateEditor">
						<constructor-arg index="0">
							<bean class="java.text.SimpleDateFormat">
								<constructor-arg><value>yyyy-MM-dd</value></constructor-arg>
							</bean>
						</constructor-arg>
						<constructor-arg index="1"><value>true</value></constructor-arg>
					</bean>
				</entry>
			</map>
		</property>
	</bean>
</beans>
