<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="<spring:url value="/css/blueprint/print.css" />" type="text/css" media="print" />
    <!--[if lt IE 8]>
	<link rel="stylesheet" href="<spring:url value="/css/blueprint/ie.css" />" type="text/css" media="screen, projection" />
    <![endif]-->
	<link rel="stylesheet" href="<spring:url value="/wro/front.css" />" type="text/css" media="screen, projection" />
	<script type="text/javascript" src="<spring:url value="/wro/front.js" />"></script>

	<c:if test="${i18nJS != null && not empty i18nJS}">
		<script type="text/javascript">
			var i18n = new Array();
			<c:forEach var="entry" items="${i18nJS}">i18n["${entry.key}"]="${entry.value}";</c:forEach>
		</script>
	</c:if>
    <title>ClearBrain</title>
</head>

<body>
	<div class="main">
		<div id="content">
			<div id="header">
				<div class="logo left"><a href="<spring:url value="/" />">ClearBrain</a></div>
				<div class="login-bt-container right">
					<%-- If user is logged in, display dashboard link --%>
					<sec:authorize access="hasRole('RIGHT_USER')">
						<a href="<spring:url value="/dashboard" />"><sec:authentication property="principal.username" /></a>
					</sec:authorize>

					<%-- Otherwise, display login link --%>
					<sec:authorize access="!hasRole('RIGHT_USER')">
						<a id="login-bt" href="<spring:url value="/login" />"><spring:message code="login.logIn" /></a>
					</sec:authorize>
				</div>
			</div>
			<div id="login-container" class="hide">
				<form method="post" action="<c:url value='/j_spring_security_check'/>">
					<div>
						<label for="j_username_t"><spring:message code="login.email" /></label><br />
						<input id="j_username_t" type="text" name="j_username" />
					</div>
					<div>
						<label for="j_password_t"><spring:message code="login.password" /></label><br />
						<input type="password" id="j_password_t" name="j_password" />
					</div>
					<input id="rememberme_t" type="checkbox" name="_spring_security_remember_me" />
					<label for="rememberme_t"><spring:message code="login.rememberMe" /></label>
					<div>
						<input type="submit" value="<spring:message code="login.logIn" />" />
					</div>
				</form>
			</div>
			<div id="subheader">
				<div class="slogan left"><spring:message code="header.subtitle" /></div>
				<div class="right">
					<spring:message code="header.dateFormat" var="dateFormat" />
					<c:set var="today" value="<%=new java.util.Date()%>" />
  					<fmt:formatDate value="${today}" pattern="${dateFormat}" />
  				</div>
			</div>
			<div class="language-container right">
				<a href="?lang=en_US"><img src="<spring:url value="/images/front/lang/en_US.png" />" title="English (United States)" alt="EN" /></a>&nbsp;
				<a href="?lang=fr_FR"><img src="<spring:url value="/images/front/lang/fr_FR.png" />" title="Fran&ccedil;ais (France)" alt="FR" /></a>
			</div>
			<div class="container">
