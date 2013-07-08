<%@include file="_header.jsp"%><%

	/*
	 * Query service for autocomplete field
	 */
	 
	String query = request.getParameter("query").trim();
	String hquery = "from Property where name like ? order by name";
	
	response.setContentType("text/plain");
	out.clear();
	
	Iterator iter = hsession.createQuery(hquery)
					.setString(0,query+"%")
					.list().iterator();
	while (iter.hasNext()) {
		Property p = (Property)iter.next();
		if (p.getName() != null) {
			String name = p.getName().trim();
			if (name.length() == 0) {
				out.write ("(no name)");
			} else {
				out.write (name);
			}
		}
		//out.write (p.getName());
		out.write (" [");
		out.write (p.getId().toString());
		out.write ("]\n");
	}
%>