<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
	<meta name="decorator" content="front" />
</head>

<body>
	<br /><br /><br />

	<div class="main-box span-24 last">
		<div class="signup-container prepend-3 span-21 last">
			<h2><img src="<spring:url value="/images/front/tick-grey.png" />" alt="" /> <spring:message code="signup.title" /></h2>
			<h3 class="alt"><spring:message code="signup.subtitle" /></h3>
		</div>

		<form:form method="POST" commandName="signupform" class="inline">
			<div class="signup-line-height prepend-5 span-19 last">
				<div class="span-5">
					<label for="email"><spring:message code="signup.mainEmail" /></label>
				</div>
				<div class="span-6">
					<form:input id="email" path="user.email" onchange="checkEmail();" cssClass="signup-input-size" maxlength="254" />
				</div>
				<div class="span-8 last">
					<div id="email-check" class="hide">&nbsp;</div>
					<form:errors id="email-error" path="user.email" cssClass="error error-block" />
				</div>
			</div>

			<div class="prepend-5 span-19 last signup-line-height">
				<div class="span-5">
					<label for="password"><spring:message code="signup.password" /></label>
				</div>
				<div class="span-6">
					<form:password id="password" path="user.password" onchange="checkPassword();" cssClass="signup-input-size" />
				</div>
				<div class="span-8 last">
					<div id="password-check" class="hide">&nbsp;</div>
					<form:errors id="password-error" path="user.password" cssClass="error error-block" />
				</div>
			</div>

			<div class="prepend-5 span-19 last signup-line-height">
				<div class="span-5">
					<label for="passwordConfirmation"><spring:message code="signup.confirmPassword" /></label>
				</div>
				<div class="span-6">
					<form:password id="passwordConfirmation" onchange="checkPasswordConfirmation();" path="passwordConfirmation" cssClass="signup-input-size" />
				</div>
				<div class="span-8 last">
					<div id="password-confirm-check" class="hide">&nbsp;</div>
					<form:errors id="password-confirm-error" path="passwordConfirmation" cssClass="error error-block" />
				</div>
			</div>

			<div class="prepend-10 span-14 last">
				<p class="buttons createaccount">
					<button class="button positive" type="submit">
						<img alt="" src="css/blueprint/plugins/buttons/icons/tick.png" />
						<spring:message code="signup.createAccount" />
					</button>
				</p>
			</div>
		</form:form>
	</div>

	<%-- Preload images --%>
	<img src="images/front/loading-circle.gif" class="hide" />
</body>
</html>
