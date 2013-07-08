<%@include file="_header.jsp"%><%

	String query = request.getParameter("query");
	String hquery = "from Family where name like ? order by name";
	
	System.err.println ("Query="+hquery);
	
	Iterator iter = hsession.createQuery(hquery)
							.setString(0,query + "%")
							.list().iterator();
	while (iter.hasNext()) {
		Family family = (Family)iter.next();
		out.write (family.getName());
		out.write (" [");
		out.write (family.getId().toString());
		out.write ("]\n");
	}
	
%>
