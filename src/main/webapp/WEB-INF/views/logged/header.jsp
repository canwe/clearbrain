<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="<spring:url value="/css/blueprint/screen.css" />" media="screen, projection" />
    <link rel="stylesheet" href="<spring:url value="/css/blueprint/print.css" />" type="text/css" media="print" />
    <!--[if lt IE 8]>
	<link rel="stylesheet" href="<spring:url value="/css/blueprint/ie.css" />" type="text/css" media="screen, projection" />
    <![endif]-->
    <link rel="stylesheet" href="<spring:url value="/css/logged.css" />" type="text/css" media="screen, projection" />
	<script type="text/javascript" src="<spring:url value="/js/jquery-1.5.1.min.js" />"></script>
    <script type="text/javascript" src="<spring:url value="/js/jquery-ui-1.8.11.custom.min.js" />"></script>
	<c:if test="${i18nJS != null && not empty i18nJS}">
		<script type="text/javascript">
			var i18n = new Array();
			<c:forEach var="entry" items="${i18nJS}">i18n["${entry.key}"]="${entry.value}";</c:forEach>
		</script>
	</c:if>
	<script type="text/javascript" src="<spring:url value="/js/logged.js" />" ></script>
    <title>~S2NDBRN logged~</title>
</head>

<body>
	<div class="header">
		S2NDBRN - All - Today - Tomorrow - This week - This month - [+] Add task - Account
	</div>
    <div class="container">