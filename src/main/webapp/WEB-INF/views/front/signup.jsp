<%@include file="/WEB-INF/views/front/header.jsp"%>
<br /><br /><br />
	<div id="signup-container" class="prepend-4 span-20 last">
		<h2><spring:message code="signup.title" /></h2>
	<h3 class="alt"><spring:message code="signup.subtitle" /></h3>
</div>

<div class="column span-19 prepend-5 last">
	<form:form method="POST" commandName="signupform" class="inline">
		<div class="column span-5">
		    <label for="email"><spring:message code="signup.mainEmail" /></label>
		</div>
		<div class="column span-14 last">
			<form:input id="email" path="user.email" />
			<form:errors path="user.email" cssClass="error" />
		</div>

		<div class="column span-5">
		    <label for="password"><spring:message code="signup.password" /></label>
		</div>
		<div class="column span-14 last">
			<form:password id="password" path="user.password" />
			<form:errors path="user.password" cssClass="error" />
		</div>

		<div class="column span-5">
		    <label for="passwordConfirmation"><spring:message code="signup.confirmPassword" /></label>
		</div>
		<div class="column span-14 last">
			<form:password id="passwordConfirmation" path="passwordConfirmation" />
			<form:errors path="passwordConfirmation" cssClass="error" />
		</div>

		<div class="column span-14 prepend-5 last">
			<p class="buttons createaccount">
				<button class="button positive" type="submit">
					<img alt="" src="css/blueprint/plugins/buttons/icons/tick.png" />
					<spring:message code="signup.createAccount" />
				</button>
			</p>
		</div>
	</form:form>
</div>
<%@include file="/WEB-INF/views/front/footer.jsp"%>
