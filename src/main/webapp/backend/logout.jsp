<%
	session.setAttribute("_user",null);
	user=null;
	context.remove("user");
	templates.merge ("/backend/logout.vm",context,out);
%>
