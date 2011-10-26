<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>

<!DOCTYPE html>
<html>
<head>
	<title>Clearbrain - <decorator:title default="Logged" /></title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link rel="stylesheet" href="<spring:url value="/css/blueprint/print.css" />" type="text/css" media="print" />
	<!--[if lt IE 8]>
	<link rel="stylesheet" href="<spring:url value="/css/blueprint/ie.css" />" type="text/css" media="screen, projection" />
	<![endif]-->
	<link rel="stylesheet" href="<spring:url value="/css/blueprint/screen.css" />" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<spring:url value="/css/blueprint/plugins/fancy-type/screen.css" />" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<spring:url value="/css/blueprint/plugins/buttons/screen.css" />" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<spring:url value="/css/jquery/jquery-ui.css" />" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<spring:url value="/css/jquery/cleditor.css" />" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="<spring:url value="/css/logged.css" />" type="text/css" media="screen, projection" />

	<script type="text/javascript" src="<spring:url value="/js/jquery/jquery-1.6.3.min.js" />"></script>
	<script type="text/javascript" src="<spring:url value="/js/jquery/ui/jquery-ui-1.8.16.custom.min.js" />"></script>
	<script type="text/javascript" src="<spring:url value="/js/jquery/clearfield/clearfield-1.1.min.js" />"></script>
	<script type="text/javascript" src="<spring:url value="/js/core.js" />"></script>
	<script type="text/javascript" src="<spring:url value="/js/logged/menu.js" />"></script>

	<script type="text/javascript">
	<c:if test="${i18nJS != null && not empty i18nJS}">
		var i18n = new Array();
		<c:forEach var="entry" items="${i18nJS}">i18n["${entry.key}"]="${entry.value}";</c:forEach>
	</c:if>
	</script>
	<decorator:head />
	<script type="text/javascript" src="<spring:url value="/js/less/less-1.1.3.min.js" />"></script>
</head>

<body>
	<div id="menu">
		<div class="container">
			<div class="span-24 last">
				<%-- Left --%>
				<div id="logo"><a href="<spring:url value="/dashboard" />">ClearBrain</a></div>
				<a id="m-today" class="m-datebox" href="<spring:url value="/today" />">
					<spring:message code="header.menu.today" />
					<span class="m-today-notif-${sessionScope.locale}<c:if test="${sessionScope.today == null || sessionScope.today == '0'}"> hide-forced</c:if>">
						${sessionScope.today}
					</span>
				</a>
				<a id="m-tomorrow" class="m-datebox" href="<spring:url value="/tomorrow" />">
					<spring:message code="header.menu.tomorrow" />
					<span class="m-tomorrow-notif-${sessionScope.locale}<c:if test="${sessionScope.tomorrow == null || sessionScope.tomorrow == '0'}"> hide-forced</c:if>">
						${sessionScope.tomorrow}
					</span>
				</a>
				<a id="m-week" class="m-datebox" href="<spring:url value="/this_week" />">
					<spring:message code="header.menu.week" />
					<span class="m-week-notif-${sessionScope.locale}<c:if test="${sessionScope.week == null || sessionScope.week == '0'}"> hide-forced</c:if>">
						${sessionScope.week}
					</span>
				</a>

				<%-- Right --%>
				<div id="m-profile-container" class="m-dropdown">
					<a id="m-profile">
						<spring:message code="header.menu.profile" /><img src="<spring:url value="/images/logged/dropdown.png" />" />
					</a>
					<div class="dropdown hide-forced">
						<ul>
							<li class="dropdown-li-top">
								<a id="m-settings" href="<spring:url value="/settings" />">
									<spring:message code="header.menu.settings" />
								</a>
							</li>
							<li>
								<a id="m-stats" href="<spring:url value="/statistics" />">
									<spring:message code="header.menu.stats" />
								</a>
							</li>
							<li>
								<a id="m-help" href="<spring:url value="/help" />">
									<spring:message code="header.menu.help" />
								</a>
							</li>
							<li>
								<a id="m-logout" href="<spring:url value="/j_spring_security_logout" />">
									<spring:message code="header.menu.logout" />
								</a>
							</li>
						</ul>
					</div>
				</div>
				<div id="m-notes-container" class="m-dropdown">
					<a id="m-notes">
						<spring:message code="header.menu.notes" /><img src="<spring:url value="/images/logged/dropdown.png" />" />
					</a>
					<div class="dropdown hide-forced">
						<ul>
							<li class="dropdown-li-top">
								<a id="m-add" href="<spring:url value="/note" />"><spring:message code="header.menu.add" /></a>
							</li>
							<li>
								<a id="m-memo" href="<spring:url value="/quick_memo" />"><spring:message code="header.menu.memo" /></a>
							</li>
							<li>
								<a id="m-search" href="<spring:url value="/search" />"><spring:message code="header.menu.search" /></a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="content" class="container">
		<div id="blank">&nbsp;</div>
		<decorator:body />
		</div>
	</body>
</html>
