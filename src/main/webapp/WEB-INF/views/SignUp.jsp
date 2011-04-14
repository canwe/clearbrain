<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<body>
	<h2>Sign up</h2>
	<h4>You're just one step away from improving your organization process!</h4>

	<form:form method="POST" commandName="signupForm">
		<form:errors path="*" cssClass="errorblock" element="div" /><br /><br />
			Main email address: <form:input path="email" /><form:errors path="email" cssClass="error" /><br />
			Password: <form:password path="password" /><form:errors path="password" cssClass="error" /><br />
			Confirm password: <form:password path="passwordConfirmation" /><br />
			 <input type="submit" value="Create my account" />
	</form:form>
</body>
</html>
