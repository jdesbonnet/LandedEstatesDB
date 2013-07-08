<%@include file="_header.jsp"%><%
	context.put ("changes", db.getRecentChanges());
	templates.merge ("/backend/change-list.vm",context,out);
%>