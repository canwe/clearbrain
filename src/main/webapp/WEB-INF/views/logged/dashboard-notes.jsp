<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Display a note using to parameters sent from dashboard.jsp --%>
<div id="note-${param.id}" class="note">
	<input type="checkbox" <c:if test="${param.checked}">checked="checked"</c:if> />
	<c:out value="${param.name}" />
	<span id="noteedit-${param.id}" class="notes-edit">
		<a href="<spring:url value="/note?id=${param.id}" />"><img src="<spring:url value="/images/logged/edit.gif" />" /></a>
	</span>
	<div id="notecat-${param.id}" class="note-category"></div>
</div>
