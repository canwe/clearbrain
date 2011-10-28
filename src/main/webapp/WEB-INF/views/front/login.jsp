<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter"%>

<html>
<head>
	<meta name="decorator" content="front" />
	<title><spring:message code="login.title" /></title>
</head>

<body>
	<br /><br /><br />

	<div class="main-box span-24 last">
		<div id="signup-container" class="prepend-3 span-21 last">
			<h2><img src="<spring:url value="/images/front/key.png" />" alt="" /> <spring:message code="login.title" /></h2>
			<h3 class="alt"><spring:message code="login.subtitle" /></h3>
		</div>

		<c:if test="${param.error != null}">
			<c:set var="prevLogin" value="<%=session.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY) %>" />
			<div class="prepend-5 span-11 append-8 last">
				<div class="error"><spring:message code="login.error" /></div>
			</div>
		</c:if>

		<form method="post" action="<c:url value='/j_spring_security_check'/>" class="inline">
			<div class="prepend-5 span-19 last signup-line-height">
				<div class="span-4">
					<label for="j_username"><spring:message code="login.mainEmail" /></label>
				</div>
				<div class="span-15 last">
					<input type="text" name="j_username" id="j_username" <c:if test="${!empty prevLogin}">value="<c:out value='${prevLogin}' escapeXml='false' />"</c:if> class="signup-input-size" maxlength="254" />
				</div>
			</div>

			<div class="prepend-5 span-19 last signup-line-height">
				<div class="span-4">
					<label for="j_password"><spring:message code="signup.password" /></label>
				</div>
				<div class="span-15 last">
					<input type="password" name="j_password" id="j_password" class="signup-input-size" />
				</div>
			</div>

			<div class="prepend-9 span-15 last signup-line-height">
				<input id="rememberme" type="checkbox" name="_spring_security_remember_me" />
				<label for="rememberme"><spring:message code="login.rememberMeComputer" /></label>
			</div>

			<div class="prepend-10 span-14 last">
				<p class="buttons">
					<button class="button blue-color" type="submit">
						<img alt="" src="css/blueprint/plugins/buttons/icons/key.png" />
						<spring:message code="login.logIn" />
					</button>
				</p>
			</div>
		</form>
	</div>
</body>
</html>
