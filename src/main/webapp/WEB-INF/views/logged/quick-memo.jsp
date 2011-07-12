<%@include file="/WEB-INF/views/logged/header.jsp"%>
<script type="text/javascript" src="<spring:url value="/js/logged/quick-memo.js" />" ></script>

<%--CLEditor includes --%>
<script type="text/javascript" src="<spring:url value="/js/jquery-cleditor.min.js" />"></script>
<link rel="stylesheet" href="<spring:url value="/css/jquery.cleditor.css" />" type="text/css" />

<form:form method="POST" commandName="memoform" class="inline">
	<div id="cleditor-container" class="hiddenClass">
		<%-- Quick memo --%>
		<form:textarea id="input" path="input"/>

		<%-- Save button --%>
		<div id="memo-btn-container">
			<button class="button" type="submit">
				<spring:message code="memo.save.button" />
			</button>
		</div>

		<%-- Last saved date --%>
		<c:if test="${memoform.saveDate != null}">
			<div id="memo-date-container">
			<spring:message code="memo.save.dateFormat" var="dateFormat" />
			<spring:message code="memo.save.last" /> <fmt:formatDate value="${memoform.saveDate}" pattern="${dateFormat}" />
		</div>
		</c:if>
	</div>
</form:form>

<%@include file="/WEB-INF/views/logged/footer.jsp"%>
