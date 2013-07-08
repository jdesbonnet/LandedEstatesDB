<%@include file="_header.jsp"%><%
	String q = request.getParameter("q");
	List results = db.search (hsession,q);
	context.put ("results",results);
	context.put ("nResults", new Integer(results.size()));
	templates.merge ("/backend/search-results.vm",context,out);
%>
