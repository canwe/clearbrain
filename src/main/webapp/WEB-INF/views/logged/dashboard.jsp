<%@include file="/WEB-INF/views/logged/header.jsp"%>
<div class="span-7">
	<%-- Date --%>
	<div class="top-grey-box date-container">
		<%-- TODO: Capitalize first letter --%>
		<spring:message code="header.dateFormat" var="dateFormat" />
		<c:set var="today" value="<%=new java.util.Date()%>" />
		<fmt:formatDate value="${today}" pattern="${dateFormat}" />
	</div>

	<div class="categories-main-container">
		<%-- Categories --%>
		<%-- TODO: Change design, shown/hidden is not clear / ugly --%>
		<div class="left">
			<b><spring:message code="dashboard.cat.title" /></b>:
		</div>
		<div class="right">
			<input id="view-example" type="checkbox" checked="checked" disabled="disabled" /> <spring:message code="dashboard.cat.subtitle.shown" />
			<input id="hide-example" type="checkbox" disabled="disabled" /> <spring:message code="dashboard.cat.subtitle.hidden" />
		</div>
		<div id="categories-container">
			<br />
			<ul id="categories" class="pointer">
				<c:forEach items="${categoriesList}" var="cur">
					<li id="cat-<c:out value="${cur.id}" />">&nbsp;&nbsp;<input type="checkbox" <c:if test="${cur.displayed == true}">checked="checked"</c:if> /> <c:out value="${cur.name}" /></li>
				</c:forEach>
			</ul>
			<div id="cat-unclassified" class="pointer selected-category">&nbsp;&nbsp;<input type="checkbox" /> <spring:message code="dashboard.cat.unclassified" /></div>
			<div class='trash pointer'><spring:message code="dashboard.cat.trash" /></div>
		</div>
		<div class="right"><a id="cat-edit" class="pointer"><spring:message code="dashboard.cat.edit" /></a></div>

		<%-- Categories editing --%>
		<div id="cat-edit-container" class="hide">
		    <b><spring:message code="dashboard.cat.insert" /></b><br />
			<input id="cat-name" type="text" />
			<i><spring:message code="dashboard.cat.insert.help" /></i>
		</div>
	</div>
</div>

<div class="span-17 last">
	<%-- Add a quick task --%>
	<div id="quick-add-task-container" class="top-grey-box">
		<div id="hide-quick-add-task" class="right pointer">X&nbsp;&nbsp;</div>
		<input id="quick-add-task" class="clearField" type="text" value="<spring:message code="dashboard.note.quickInsert" />" />
	</div>
	<br />

	<ul id="notes" class="pointer">
		<c:forEach items="${notesList}" var="cur">
			<li id="note-<c:out value="${cur.id}" />"> <input type="checkbox" /> <c:out value="${cur.name}" /></li>
		</c:forEach>
	</ul>

</div>
<%@include file="/WEB-INF/views/logged/footer.jsp"%>
