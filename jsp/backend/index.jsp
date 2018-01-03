<%@include file="_header.jsp"%><%
	context.put("pageId","./index");
	templates.merge ("/backend/master.vm",context,out);
%>
