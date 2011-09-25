<%@include file="/WEB-INF/views/logged/header.jsp"%>
<script type="text/javascript" src="<spring:url value="/wro/statistics.js" />" ></script>

<%-- If there is any data, displays the chart --%>
<c:if test="${created != null && not empty created && done != null && not empty done}">
	<%-- Creates arrays containing chart's data and determines start date of the chart --%>
	<script type="text/javascript">
		var created = [<c:forEach var="entry" items="${created}">${entry},</c:forEach>];
		var done = [<c:forEach var="entry" items="${done}">${entry},</c:forEach>];
		var pointStart = Date.UTC(${fromYear}, ${fromMonth}, ${fromDay});
	</script>

	<%-- Displays chart --%>
	<c:if test="${created != null && not empty created && done != null && not empty done}">
		<div id="chart-div"></div>
	</c:if>
</c:if>

<%-- Otherwise, displays a warning message --%>
<c:if test="${created == null || empty created || done == null || empty done}">
	<div id="chart-no-data" class="no-data">
		<spring:message code="stats.no.task1" /><br />
		<spring:message code="stats.no.task2" />
		<a href="<spring:url value="/note" />"><spring:message code="stats.no.task3" /></a>.
	</div>
</c:if>

<%@include file="/WEB-INF/views/logged/footer.jsp"%>
