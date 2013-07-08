<%@include file="_header.jsp"%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	context.put ("tabId","estates");
	templates.merge ("/backend/estate-new.vm",context,out);
%>
