<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
	<meta name="decorator" content="logged" />
	<title><spring:message code="search.sub.title" /></title>
	<script type="text/javascript" src="<spring:url value="/js/logged/todo.js" />" ></script>
</head>

<body>
	<form:form id="search-form" method="POST" commandName="searchform" class="inline">
		<%-- Subtitle --%>
		<div class="prepend-2 span-20 append-2 last forms-subtitle">
			<h3 class="alt forms-border-separator">
				<spring:message code="search.sub.title" />
			</h3>
		</div>

		<%-- Search  --%>
		<div class="prepend-2 span-22 last forms-line-height">
			<%-- Search input --%>
			<div class="span-18">
				<form:input id="search" path="search" />
			</div>
			<div class="span-4 last">
				<p class="buttons createaccount">
					<button class="button positive" type="submit">
						<spring:message code="search.btn.content" />
					</button>
				</p>
			</div>

			<%-- Notes list --%>
			<c:if test="${notes != null}">
				<div id="notes-container" class="span-22 last">
					<%-- No note found --%>
					<c:if test="${fn:length(notes) == 0}"><i><spring:message code="search.result.no" /></i></c:if>
					<c:if test="${fn:length(notes) > 0}"><br /><i><spring:message code="search.result.yes" /> </i><br /><br /></c:if>

					<%-- Notes found --%>
					<c:forEach items="${notes}" var="cur">
						<div id="note-${cur.id}" class="note search-note">
							<input type="checkbox" <c:if test="${cur.resolvedDate != null}">checked="checked"</c:if> />
							<span id="notecontent-${cur.id}" <c:if test="${cur.resolvedDate != null}">class="note-strikethrough"</c:if>>
								<c:out value="${cur.name}" />
							</span>
							<span id="noteedit-${cur.id}" class="notes-edit">
								<a href="<spring:url value="/note?id=${cur.id}" />"><img src="<spring:url value="/images/logged/edit.gif" />" /></a>
							</span>
							<div id="notecat-${cur.id}" class="note-category">${categories[notesCatIds[cur.id]]}</div>
						</div>
					</c:forEach>
				</div>
			</c:if>
		</div>
	</form:form>
</body>
</html>
