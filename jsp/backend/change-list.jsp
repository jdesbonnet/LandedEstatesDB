<%@include file="_header.jsp"%><%
	context.put ("changes", db.getRecentChanges(em));
	context.put("pageId","./change-list");
	templates.merge ("/backend/master.vm",context,out);
%>