<%@include file="/WEB-INF/views/logged/header.jsp"%>
<script type="text/javascript" src="<spring:url value="/js/logged/settings.js" />"></script>

<form:form method="POST" commandName="settingsform" class="inline">
	<form:hidden id="currentEmail" path="currentEmail" />

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
	<div class="prepend-2 span-20 append-2 last settings-subtitle">
		<h3 class="alt settings-title-separator">
			<spring:message code="settings.sub.title" />
		</h3>
	</div>

	<%-- Main email address --%>
	<div class="prepend-5 span-19 last settings-line-height">
		<div class="span-5">
			<label for="email"><spring:message code="settings.main.email" /></label>
		</div>
		<div class="span-6">
			<form:input id="email" path="email" cssClass="settings-input-size" />
		</div>
		<div class="span-8 last">
			<div id="email-check" class="hide">&nbsp;</div>
			<form:errors id="emailError" path="email" cssClass="error error-block" />
		</div>
	</div>

	<%-- Edit password --%>
	<div class="prepend-5 span-19 last settings-line-height-radio">
		<div class="span-5 settings-bold">
			<spring:message code="settings.pwd.change" />
		</div>
		<div id="editPasswordRadios" class="span-6">
			<form:radiobutton id="editPassword1" path="editPassword" value="no" />
			<label for="editPassword1"><spring:message code="settings.pwd.no" /></label><br />
			<form:radiobutton id="editPassword2" path="editPassword" value="yes"/>
			<label for="editPassword2"><spring:message code="settings.pwd.yes" /></label>
		</div>
		<div class="span-8 last">
		</div>
	</div>

	<div id="password-container" class="hide">
		<%-- Current Password --%>
		<div class="prepend-5 span-19 last settings-line-height">
			<div class="span-5">
				<label for="currentPassword"><spring:message code="settings.pwd.current" /></label>
			</div>
			<div class="span-6">
				<form:password id="currentPassword" path="currentPassword" cssClass="settings-input-size" />
			</div>
			<div class="span-8 last">
				<div id="curpwd-check" class="hide">&nbsp;</div>
				<form:errors id="currentPasswordError" path="currentPassword" cssClass="error error-block" />
			</div>
		</div>

		<%-- New Password --%>
		<div class="prepend-5 span-19 last settings-line-height">
			<div class="span-5">
				<label for="newPassword"><spring:message code="settings.pwd.new" /></label>
			</div>
			<div class="span-6">
				<form:password id="newPassword" path="newPassword" cssClass="settings-input-size" />
			</div>
			<div class="span-8 last">
				<div id="newpwd-check" class="hide">&nbsp;</div>
				<form:errors id="newPasswordError" path="newPassword" cssClass="error error-block" />
			</div>
		</div>

		<%-- Verify Password --%>
		<div class="prepend-5 span-19 last settings-line-height">
			<div class="span-5">
				<label for="confirmPassword"><spring:message code="settings.pwd.confirm" /></label>
			</div>
			<div class="span-6">
				<form:password id="confirmPassword" path="confirmPassword" cssClass="settings-input-size" />
			</div>
			<div class="span-8 last">
				<div id="confirmpwd-check" class="hide">&nbsp;</div>
				<form:errors id="confirmPasswordError" path="confirmPassword" cssClass="error error-block" />
			</div>
		</div>
	</div>

	<%-- Language --%>
	<div class="prepend-5 span-19 last settings-line-height-radio">
		<div class="span-5 settings-bold">
			<spring:message code="settings.main.lang" />
		</div>
		<div class="span-14 last">
			<form:radiobutton id="lang1" path="lang" value="en_US"/>
				<img src="<spring:url value="/images/lang/en_US.png" />" title="English (United States)" alt="EN" />
				<label for="lang1">English (United States)</label><br />
			<form:radiobutton id="lang2" path="lang" value="fr_FR"/>
				<img src="<spring:url value="/images/lang/fr_FR.png" />" title="Fran&ccedil;ais (France)" alt="FR" />
				<label for="lang2">Fran&ccedil;ais (France)</label>
		</div>
	</div>

	<%-- Save button --%>
	<div id="settings-save-container" class="prepend-10 span-14 last">
		<p class="buttons createaccount">
			<button class="button positive" type="submit">
				<spring:message code="settings.save.btn" />
			</button>
		</p>
	</div>

	<%-- Delete account --%>
	<div id="settings-rm-separator" class="prepend-2 span-20 append-2 last">
		<div class="settings-title-separator"></div>
	</div>
	<div id="settings-rm-container" class="span-22 append-2 last">
		<img src="<spring:url value="/images/logged/delete-account.png" />" /> <a href="<spring:url value="/settings_delete_account" />"><spring:message code="settings.rm.account" /></a>
	</div>
</form:form>

<%-- Preload images --%>
<img src="images/front/loading-circle.gif" class="hide" />

<%@include file="/WEB-INF/views/logged/footer.jsp"%>
