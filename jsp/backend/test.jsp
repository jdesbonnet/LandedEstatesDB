<%@include file="_header.jsp"%><%
	context.put("pageId","./test");
		
	templates.merge ("/backend/master.vm",context,out);
%>
