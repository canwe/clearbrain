<%@include file="/WEB-INF/views/front/header.jsp"%>
<br /><br /><br />
	<div class="prepend-4 span-20 last">
		<h2><img src="<spring:url value="/images/tick30px.png" htmlEscape="true" />" alt="" /> Sign up completed</h2>
		<h3 class="alt">Congratulations! Your first task has just been accomplished !</h3>
		<br />
		<h4>
			You can now login, using your email and password on <a href="<spring:url value="/login" htmlEscape="true" />">this link</a>.
		</h4>
	</div>
<%@include file="/WEB-INF/views/front/footer.jsp"%>
