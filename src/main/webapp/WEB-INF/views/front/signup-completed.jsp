<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
	<title><spring:message code="signupcompleted.title" /></title>
	<meta name="decorator" content="front" />
</head>

<body>
	<br /><br /><br />
	<div class="main-box span-24 last">
		<div class="prepend-4 span-20 last">
			<h2>
				<img src="<spring:url value="/images/front/tick-green.png" htmlEscape="true" />" alt="" />
				<spring:message code="signupcompleted.title" />
			</h2>
			<h3 class="alt"><spring:message code="signupcompleted.subtitle" /></h3>
			<br />
			<h4>
				<spring:message code="signupcompleted.canAccessDashboard" />
				<a href="<spring:url value="/dashboard" htmlEscape="true" />"> <spring:message code="signupcompleted.link" /></a>.
			</h4>
		</div>
	</div>
</body>
</html>
