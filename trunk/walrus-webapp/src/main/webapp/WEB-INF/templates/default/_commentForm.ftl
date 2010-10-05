<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]/>
<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
		<div id="commentForm">
			<h2>Comments</h2>
			<@form.form commandName="comment" action="addComment" cssClass="clearfix">
			<@form.errors path="name" element="p" cssClass="errorMsg"/>
			<@form.errors path="body" element="p" cssClass="errorMsg"/>
			<@form.errors path="email" element="p" cssClass="errorMsg"/>
			<div class="field fl">
				<@form.label cssErrorClass="errorMsg" path="name"><@spring.message code="product.commentsForm.name"/>:</@form.label>
				<@form.input path="name" maxlength="40" cssErrorClass="error"/>
			</div>
			<div class="field fr">
				<@form.label cssErrorClass="errorMsg" path="email"><@spring.message code="product.commentsForm.email"/>:</@form.label>
				<@form.input path="email"  maxlength="60" cssErrorClass="error"/>
			</div>
			<div class="field long">
				<@form.textarea path="body" rows="7" cols="50"  cssErrorClass="error"/>
			</div>
			<div class="field">
				<input type="submit" name="_eventId_addComment" value="Send" />
			</div>
			<input type="hidden" name="rubric" value="${comment.rubric.id}"/>
			</@form.form>
		</div>