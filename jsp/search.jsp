<%@include file="_header.jsp"%><%
	String q = request.getParameter("q").trim();
	List<Object> results = db.search (hsession,q);
	context.put ("results",results);
	context.put ("q",q);
	templates.merge ("/search-results.vm",context,out);
%>
