<%@include file="/WEB-INF/views/front/header.jsp"%>
<br /><br /><br />
	<div class="prepend-4 span-20 last">
		<h2>Sign up</h2>
	<h3 class="alt">You're just one step away from improving your organizational process!</h3>
</div>

<div class="column span-12 append-6 prepend-6 last">
	<form:form method="POST" commandName="signupform" class="inline">
		<form:errors path="*" cssClass="errorblock" element="div" /><br /><br />
		<div class="column span-4">
		    <label for="email">Main email address:</label>
		</div>
		<div class="column span-8 last">
			<form:input id="email" path="user.email" />
			<form:errors path="user.email" cssClass="error" />
		</div>

		<div class="column span-4">
		    <label for="password">Password:</label>
		</div>
		<div class="column span-8 last">
			<form:password id="password" path="user.password" />
			<form:errors path="user.password" cssClass="error" />
		</div>

		<div class="column span-4">
		    <label for="passwordConfirmation">Confirm password:</label>
		</div>
		<div class="column span-8 last">
			<form:password id="passwordConfirmation" path="passwordConfirmation" />
		</div>

		<div class="column span-8 prepend-4 last">
			<p class="buttons createaccount">
				<button class="button positive" type="submit">
					<img alt="" src="css/blueprint/plugins/buttons/icons/tick.png" />
					Create my account
				</button>
			</p>
		</div>
	</form:form>
</div>
<%@include file="/WEB-INF/views/front/footer.jsp"%>
