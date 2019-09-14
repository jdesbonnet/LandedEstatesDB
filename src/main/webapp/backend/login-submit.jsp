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
	
	// Jump to dashboard
	response.sendRedirect("index.jsp");
%>
