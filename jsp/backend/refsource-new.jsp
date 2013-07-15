<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}


	context.put ("tabId","refsources");
	templates.merge ("/backend/refsource-new.vm",context,out);
%>
