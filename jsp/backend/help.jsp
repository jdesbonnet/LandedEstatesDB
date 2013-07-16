<%@include file="_header.jsp"%><%

	context.put ("tabId",request.getParameter("tab"));
	context.put ("topic", request.getParameter("topic"));
	 
	templates.merge ("/backend/help.vm",context,out);
%>
