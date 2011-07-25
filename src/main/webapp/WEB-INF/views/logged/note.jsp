<%@include file="/WEB-INF/views/logged/header.jsp"%>
<script type="text/javascript" src="<spring:url value="/js/logged/note.js" />"></script>

<%-- Import Jquery UI Datepicker CSS + JS (i18n) --%>
<link rel="stylesheet" href="<spring:url value="/css/jquery.ui.css" />" type="text/css" media="screen, projection" />
<spring:message code="note.calendar.locale" var="jslocale" />
<c:if test="${!empty jslocale}">
	<script src="<spring:url value="/js/jquery-ui.datepicker-${jslocale}.js" />"></script>
</c:if>

<%-- Check if this is a save or an update --%>
<c:set var="saveupdate" value="save" />
<c:if test="${param.id != null}">
	<c:set var="saveupdate" value="update" />
</c:if>

<form:form method="POST" commandName="noteform" class="inline">
	<form:hidden path="note.id" />
	<form:hidden id="alt-datepicker" path="dueDate" />

	<%-- If note insert/modification failed, display error --%>
	<c:if test="${sessionScope.note_ko != null}">
	    <div class="error top-msg-block">
		    <spring:message code="note.err.${saveupdate}" />
	    </div>
		<c:remove var="note_ko" scope="session" />
	</c:if>

	<%-- Display confirmation message if note was successfully saved --%>
	<c:if test="${sessionScope.note_ok != null}">
	    <div class="success top-msg-block">
		    <spring:message code="note.ok.${saveupdate}" />
	    </div>
		<c:remove var="note_ok" scope="session" />
	</c:if>

	<%-- Subtitle --%>
	<div class="prepend-2 span-20 append-2 last forms-subtitle">
		<h3 class="alt forms-border-separator">
			<spring:message code="note.title.${saveupdate}" />
		</h3>
	</div>

	<%-- Description --%>
	<div class="prepend-5 span-19 last forms-line-height">
		<div class="span-4">
			<label for="name"><spring:message code="note.description" /></label>
		</div>
		<div class="span-7">
			<form:input id="name" path="note.name" cssClass="forms-input-size" />
		</div>
		<div class="span-8 last">
			<div id="name-check" class="hide">&nbsp;</div>
			<form:errors id="name-error" path="note.name" cssClass="error error-block" />
		</div>
	</div>

	<%-- Category --%>
	<div class="prepend-5 span-19 last forms-line-height">
		<div class="span-4">
			<label for="categoryId"><spring:message code="note.category" /></label>
		</div>
		<div class="span-15 last">
			<form:select id="categoryId" path="categoryId" cssClass="forms-input-size">
				<form:options items="${categoriesList}" />
			</form:select>
		</div>
	</div>

	<%-- Due date --%>
	<div class="prepend-5 span-19 last forms-line-height-radio">
		<div class="span-4 forms-bold">
			<spring:message code="note.due.date" />
		</div>
		<div id="edit-duedate-radios" class="span-7">
			<form:radiobutton id="edit-duedate-1" path="editDueDate" value="no" />
			<label for="edit-duedate-1"><spring:message code="note.no.duedate" /></label><br />
			<form:radiobutton id="edit-duedate-2" path="editDueDate" value="yes"/>
			<label for="edit-duedate-2">
				<form:input id="datepicker" readonly="true" path="dueDateStr" cssClass="forms-input-size" cssStyle="width: 160px;" />
				<img src="<spring:url value="/images/logged/calendar.png" />" />
			</label>
		</div>
		<div class="span-8 last">
			<div id="duedate-check" class="hide">&nbsp;</div>
			<form:errors id="duedate-error" path="dueDate" cssClass="error error-block" />
		</div>
	</div>

	<%-- Save buttons --%>
	<div class="prepend-10 span-14 last forms-save-container">
		<p class="buttons createaccount">
			<button class="button positive" type="submit">
				<spring:message code="note.save.btn" />
			</button>
		</p>
	</div>

	<div class="prepend-2 span-20 append-2 last forms-down-separator">
		<div class="forms-border-separator"></div>
	</div>
	<%-- Delete note --%>
	<c:if test="${param.id != null}">
		<div class="span-22 append-2 last forms-rm-container">
			<img src="<spring:url value="/images/logged/grey-cross.png" />" />
			<input id="delete-note" type="submit" name="_action_delete" value="<spring:message code="note.delete.text" />" />
		</div>
	</c:if>

</form:form>

<%-- Preload images --%>
<img src="images/front/loading-circle.gif" class="hide" />

<%@include file="/WEB-INF/views/logged/footer.jsp"%>
