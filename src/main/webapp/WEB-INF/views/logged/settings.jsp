<%@include file="/WEB-INF/views/logged/header.jsp"%>
<script type="text/javascript" src="<spring:url value="/wro/settings.js" />"></script>

<form:form method="POST" commandName="settingsform" class="inline">
	<form:hidden id="current-email" path="currentEmail" />

	<%-- If profile modification failed, display error --%>
	<c:if test="${sessionScope.settings_ko != null}">
	    <div class="error top-msg-block">
		    <spring:message code="settings.err.update" />
	    </div>
		<c:remove var="settings_ko" scope="session" />
	</c:if>

	<%-- Display confirmation message if profile informations were successfully saved --%>
	<c:if test="${sessionScope.settings_ok != null}">
	    <div class="success top-msg-block">
		    <spring:message code="settings.ok.update" />
	    </div>
		<c:remove var="settings_ok" scope="session" />
	</c:if>

	<%-- Subtitle --%>
	<div class="prepend-2 span-20 append-2 last forms-subtitle">
		<h3 class="alt forms-border-separator">
			<spring:message code="settings.sub.title" />
		</h3>
	</div>

	<%-- Main email address --%>
	<div class="prepend-5 span-19 last forms-line-height">
		<div class="span-5">
			<label for="email"><spring:message code="settings.main.email" /></label>
		</div>
		<div class="span-6">
			<form:input id="email" path="email" cssClass="forms-input-size" />
		</div>
		<div class="span-8 last">
			<div id="email-check" class="hide">&nbsp;</div>
			<form:errors id="email-error" path="email" cssClass="error error-block" />
		</div>
	</div>

	<%-- Edit password --%>
	<div class="prepend-5 span-19 last forms-line-height-radio">
		<div class="span-5 forms-bold">
			<spring:message code="settings.pwd.change" />
		</div>
		<div id="edit-password-radios" class="span-14 last">
			<form:radiobutton id="edit-password-1" path="editPassword" value="no" />
			<label for="edit-password-1"><spring:message code="settings.pwd.no" /></label><br />
			<form:radiobutton id="edit-password-2" path="editPassword" value="yes"/>
			<label for="edit-password-2"><spring:message code="settings.pwd.yes" /></label>
		</div>
	</div>

	<div id="password-container" class="hide">
		<%-- Current Password --%>
		<div class="prepend-5 span-19 last forms-line-height">
			<div class="span-5">
				<label for="current-password"><spring:message code="settings.pwd.current" /></label>
			</div>
			<div class="span-6">
				<form:password id="current-password" path="currentPassword" cssClass="forms-input-size" />
			</div>
			<div class="span-8 last">
				<div id="curpwd-check" class="hide">&nbsp;</div>
				<form:errors id="current-password-error" path="currentPassword" cssClass="error error-block" />
			</div>
		</div>

		<%-- New Password --%>
		<div class="prepend-5 span-19 last forms-line-height">
			<div class="span-5">
				<label for="new-password"><spring:message code="settings.pwd.new" /></label>
			</div>
			<div class="span-6">
				<form:password id="new-password" path="newPassword" cssClass="forms-input-size" />
			</div>
			<div class="span-8 last">
				<div id="newpwd-check" class="hide">&nbsp;</div>
				<form:errors id="new-password-error" path="newPassword" cssClass="error error-block" />
			</div>
		</div>

		<%-- Verify Password --%>
		<div class="prepend-5 span-19 last forms-line-height">
			<div class="span-5">
				<label for="confirm-password"><spring:message code="settings.pwd.confirm" /></label>
			</div>
			<div class="span-6">
				<form:password id="confirm-password" path="confirmPassword" cssClass="forms-input-size" />
			</div>
			<div class="span-8 last">
				<div id="confirmpwd-check" class="hide">&nbsp;</div>
				<form:errors id="confirm-password-error" path="confirmPassword" cssClass="error error-block" />
			</div>
		</div>
	</div>

	<%-- Language --%>
	<div class="prepend-5 span-19 last forms-line-height-radio">
		<div class="span-5 forms-bold">
			<spring:message code="settings.main.lang" />
		</div>
		<div class="span-14 last">
			<form:radiobutton id="lang1" path="lang" value="en_US"/>
				<label for="lang1">
					<img src="<spring:url value="/images/front/lang/en_US.png" />" title="English (United States)" alt="EN" />
					English (United States)
				</label><br />
			<form:radiobutton id="lang2" path="lang" value="fr_FR"/>
				<label for="lang2">
					<img src="<spring:url value="/images/front/lang/fr_FR.png" />" title="Fran&ccedil;ais (France)" alt="FR" />
					Fran&ccedil;ais (France)
				</label>
		</div>
	</div>

	<%-- Save button --%>
	<div class="prepend-10 span-14 last forms-save-container">
		<p class="buttons createaccount">
			<button class="button positive" type="submit">
				<spring:message code="settings.save.btn" />
			</button>
		</p>
	</div>

	<%-- Delete account --%>
	<div class="prepend-2 span-20 append-2 last forms-down-separator">
		<div class="forms-border-separator"></div>
	</div>
	<div class="span-22 append-2 last forms-rm-container">
		<img src="<spring:url value="/images/logged/grey-cross.png" />" />
		<input id="delete-account" type="submit" name="_action_delete" value="<spring:message code="settings.rm.account" />" />
	</div>
</form:form>

<%-- Preload images --%>
<img src="images/front/loading-circle.gif" class="hide" />

<%@include file="/WEB-INF/views/logged/footer.jsp"%>
