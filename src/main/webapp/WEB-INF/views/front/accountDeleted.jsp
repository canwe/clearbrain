<%@include file="/WEB-INF/views/front/header.jsp"%>
<br /><br /><br />
<div class="main-box span-24 last">
	<div class="prepend-3 span-20 append-1 last">
		<h2>
			<img src="<spring:url value="/images/front/round-cross.png" htmlEscape="true" />" class="accdeleted-image" alt="" />
			<spring:message code="accdeleted.title" />
		</h2>
		<h3 class="alt"><spring:message code="accdeleted.subtitle" /></h3>
		<br />
		<h4>
			<spring:message code="accdeleted.text1" /><br />
			<spring:message code="accdeleted.text2" /> <b><spring:message code="accdeleted.text3" /></b><br />
			<spring:message code="accdeleted.text4" /><br /><br />

			<spring:message code="accdeleted.text5" /> <b><spring:message code="accdeleted.text6" /></b>.<br />
			<spring:message code="accdeleted.text7" /><br /><br />

			<spring:message code="accdeleted.text8" /><br /><br />
			<spring:message code="accdeleted.thanks" /><br />
		</h4>
	</div>
</div>
<%@include file="/WEB-INF/views/front/footer.jsp"%>
