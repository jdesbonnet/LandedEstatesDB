<%
	session.setAttribute("_user",null);
	session.setAttribute("user_id",null);
	user=null;
	context.remove("user");
	
	context.put ("pageId","./logout");
	context.put ("showSideCol","false");
	templates.merge ("/backend/master.vm",context,out);
%>
