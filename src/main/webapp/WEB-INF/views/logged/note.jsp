<%@include file="/WEB-INF/views/logged/header.jsp"%>
<script type="text/javascript" src="<spring:url value="/js/logged/note.js" />"></script>

<%-- Check if this is a save or an update --%>
<c:set var="saveupdate" value="save" />
<c:if test="${param.id != null}">
	<c:set var="saveupdate" value="update" />
</c:if>

<form:form method="POST" commandName="noteform" class="inline">
	<form:hidden path="note.id" />

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
		<div class="span-5">
			<label for="name"><spring:message code="note.description" /></label>
		</div>
		<div class="span-6">
			<form:input id="name" path="note.name" cssClass="forms-input-size" />
		</div>
		<div class="span-8 last">
			<div id="name-check" class="hide">&nbsp;</div>
			<form:errors id="name-error" path="note.name" cssClass="error error-block" />
		</div>
	</div>

	<%-- Category --%>
	<div class="prepend-5 span-19 last forms-line-height">
		<div class="span-5">
			<label for="categoryId"><spring:message code="note.category" /></label>
		</div>
		<div class="span-14 last">
			<form:select id="categoryId" path="categoryId" cssClass="forms-input-size">
				<form:options items="${categoriesList}" />
			</form:select>
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

	<%-- Delete account --%>
	<div class="prepend-2 span-20 append-2 last forms-down-separator">
		<div class="forms-border-separator"></div>
	</div>
</form:form>

<%-- Preload images --%>
<img src="images/front/loading-circle.gif" class="hide" />

<%@include file="/WEB-INF/views/logged/footer.jsp"%>
