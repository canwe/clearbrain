<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<body>
	<h2>Sign up</h2>
	<h4>You're just one step away from improving your organization process!</h4>

	<form:form method="POST" commandName="signupform">
		<form:errors path="*" cssClass="errorblock" element="div" /><br /><br />
			Main email address: <form:input path="user.email" /><form:errors path="user.email" cssClass="error" /><br />
			Password: <form:password path="user.password" /><form:errors path="user.password" cssClass="error" /><br />
			Confirm password: <form:password path="passwordConfirmation" /><br />
			 <input type="submit" value="Create my account" />
	</form:form>
</body>
</html>
