<%@include file="_header.jsp"%><%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	user = db.verifyUsernamePassword(em,username,password);
	if (user== null) {
		System.err.println (username + "/" + password + " not found");
		response.sendRedirect("login.jsp?message=auth+fail");
		return;
	}
	session.setAttribute("_user",user);
	System.err.println ("user=" + user);
	response.sendRedirect("estate-list.jsp");
%>
