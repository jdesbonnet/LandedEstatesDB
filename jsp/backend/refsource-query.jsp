<%@include file="_header.jsp"%><%

	String query = request.getParameter("query");
	String hquery = "from ReferenceSource where name like ? order by name";
	
	Iterator iter = hsession.createQuery(hquery)
					.setString(0, query + "%")
					.list().iterator();
	
	out.clear();
	
	while (iter.hasNext()) {
		ReferenceSource refSource = (ReferenceSource)iter.next();
		out.write (refSource.getName());
		if (refSource.getCategory() != null) {
			out.write (" (" + refSource.getCategory().getName() + ")");
		}
		
		out.write (" [");
		out.write (refSource.getId().toString());
		out.write ("]\n");
	}
%>