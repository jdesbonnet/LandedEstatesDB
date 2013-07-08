<%@include file="_header.jsp"%><%
	
	String query = request.getParameter("query");
	List list = db.getByQuery(query);
		
	for (int i = 0; i < list.size(); i++) {
		ReferenceSource o = (ReferenceSource)list.get(i);
		out.println ("<li> result: " + o.getId() + " " + o.getName() + " " + o.getCategory());
		
	}
%>
