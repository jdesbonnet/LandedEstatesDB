<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	user = db.verifyUsernamePassword(em,username,password);
	if (user== null) {
		System.err.println (username + "/" + password + " not found");
		response.sendRedirect("login.jsp?message=auth+fail");
		return;
	}
	session.setAttribute("_user",user);
	session.setAttribute("user_id",user.getId());
	user.setLastLogin(new Date());

	// Jump to dashboard
	response.sendRedirect("index.jsp");
%>
