<%@page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter"%>

<html>
  <head><title>Login page</title></head>
  <body>
  <%
	if (request.getParameter("login_error") != null)
	out.println("Incorrect login/password<br />");
  %>

	Please identify yourself...<br /><br />
    <form method="post" action="j_spring_security_check">
		Login:
		<input type="text" name="j_username" id="j_username" value="<%= (request.getParameter("login_error") != null) ? session.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY) : "" %>"/><br />
		Password:
		<input type="password" name="j_password" id="j_password"/><br />
		<input type="submit" />
    </form>
  </body>
</html>
