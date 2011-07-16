<%@include file="/WEB-INF/views/logged/header.jsp"%>
<script type="text/javascript" src="<spring:url value="/js/logged/dashboard.js" />" ></script>

<%-- Create an array to keep links between a note and a category --%>
<script type="text/javascript">
	<c:forEach var="entry" items="${notesCatIds}">catNA[${entry.key}] = ${entry.value};</c:forEach>
</script>

<%-- Left --%>
<div class="span-7">
	<%-- Date --%>
	<div class="top-frames date-container">
		<spring:message code="header.dateFormat" var="dateFormat" />
		<c:set var="today" value="<%=new java.util.Date()%>" />
		<fmt:formatDate value="${today}" pattern="${dateFormat}" />
	</div><br /><br />

	<%-- Categories --%>
	<div class="title"><spring:message code="dashboard.cat.title" /></div><br />
	<div id="categories-container">
		<ul id="categories" class="pointer">
			<c:forEach items="${categoriesList}" var="cur">
				<li id="cat-${cur.id}" class="category">
					<span id="catname-${cur.id}"><c:out value="${cur.name}" /></span>
					<span id="catcount-${cur.id}" class="category-count"></span>
					<span id="catmenu-${cur.id}" class="category-menus">
						<img id="catrnm-${cur.id}" src="<spring:url value="/images/logged/cat-edit.gif" />" />
						<img id="catrmv-${cur.id}" src="<spring:url value="/images/logged/cat-remove.png" />" />
					</span>
				</li>
			</c:forEach>
		</ul>
		<div id="cat-unclassified" class="category category-selected">
			<span id="catname-0"><spring:message code="dashboard.cat.unclassified" /></span>
			<span id="catcount-0" class="category-count"></span>
		</div>
		<div id="no-category"><spring:message code="dashboard.cat.nothing" /></div>
	</div>
	<%-- Categories edition link --%>
	<div id="categories-edit-link">
		<a id="categories-edit">
			<spring:message code="dashboard.cat.edit" /> <img src="<spring:url value="/images/logged/edit.gif" />" />
		</a>
	</div>
	<%-- Categories edition container --%>
	<div id="categories-edit-container" class="hide">
		<b><spring:message code="dashboard.cat.insert" /></b><br />
		<input id="catadd-name" type="text" class="category" /><br />
		<i><spring:message code="dashboard.cat.insert.help" /></i><br />
		<a id="categories-endedit">
			<spring:message code="dashboard.cat.finEdit" /> <img src="<spring:url value="/images/logged/grey-tick.png" />" />
		</a>
	</div>
	<br />
</div>

<%-- Right --%>
<div class="span-17 last">
	<div id="insert-note-container" class="top-frames">
		<input id="quick-add-task" class="clearField clearFieldBlurred" type="text" value="<spring:message code="dashboard.note.quickInsert" />" />
	</div><br /><br />

	<%-- Notes --%>
	<div class="title">Notes:</div><br />
	<div id="notes-container">
		<c:if test="${fn:length(notesList) == 0}">
			<div id="no-note"><spring:message code="dashboard.note.nothing" /></div>
		</c:if>
		<c:forEach items="${notesList}" var="cur">
			<div id="note-${cur.id}" class="note">
				<input type="checkbox"<c:if test="${cur.resolvedDate != null}"> checked="checked"</c:if> />
				<c:out value="${cur.name}" />
				<span id="noteedit-${cur.id}" class="notes-edit">
					<a href="<spring:url value="/note?id=${cur.id}" />"><img src="<spring:url value="/images/logged/edit.gif" />" /></a>
				</span>
				<div id="notecat-${cur.id}" class="note-category"></div>
			</div>
		</c:forEach>
	</div>
</div>

<%-- Preload some images --%>
<img class="hide" src="<spring:url value="/images/logged/cat-bg-to-note.png" />" />

<%@include file="/WEB-INF/views/logged/footer.jsp"%>
