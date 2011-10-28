<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
	<meta name="decorator" content="logged" />
	<title><spring:message code="stats.title" /></title>
	<script type="text/javascript" src="<spring:url value="/js/jquery/highcharts/highcharts-2.1.6.js" />" ></script>
	<script type="text/javascript" src="<spring:url value="/js/jquery/highcharts/exporting.js" />" ></script>
	<script type="text/javascript" src="<spring:url value="/js/logged/statistics.js" />" ></script>

	<%-- Creates arrays containing chart's data and determines start date of the chart --%>
	<c:if test="${created != null && not empty created && done != null && not empty done}">
		<script type="text/javascript">
			var created = [<c:forEach var="entry" items="${created}">${entry},</c:forEach>];
			var done = [<c:forEach var="entry" items="${done}">${entry},</c:forEach>];
			var pointStart = Date.UTC(${fromYear}, ${fromMonth}, ${fromDay});
		</script>
	</c:if>
</head>

<body>
	<%-- Displays chart --%>
	<c:if test="${created != null && not empty created && done != null && not empty done}">
		<div id="chart-div"></div>
	</c:if>

	<%-- Otherwise, displays a warning message --%>
	<c:if test="${created == null || empty created || done == null || empty done}">
		<div id="chart-no-data" class="no-data">
			<spring:message code="stats.no.task1" /><br />
			<spring:message code="stats.no.task2" />
			<a href="<spring:url value="/note" />"><spring:message code="stats.no.task3" /></a>.
		</div>
	</c:if>
</body>
</html>
