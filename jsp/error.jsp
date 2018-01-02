<%@page isErrorPage="true" %><%
/*
	context.put ("exception", exception);
	if (exception instanceof java.lang.UnsupportedOperationException) {
		context.put ("note", "Probable cause: database unavailable or not configured.");
	}
*/	
	//templates.merge ("/backend/error.vm",context,out);
exception.printStackTrace();
%>error: <%=exception %>

