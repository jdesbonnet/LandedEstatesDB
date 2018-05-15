<%
	String query = request.getParameter("q");
	List<Family> families = em.createQuery("from Family where name like :q order by name")
							.setParameter("q",query + "%")
							.getResultList();
	
	response.setContentType("application/json");
	out.write("[\n");
	boolean first = true;
	for(Family family : families) {
		if (first) {
			first = false;
		} else {
			out.write (",");
		}
		out.write ("{");
		out.write ("\"id\":" + family.getId());
		out.write (",\"name\":");
		out.write (JSONUtils.quote(family.getName()));
		out.write ("}\n");
	}
	out.write("]");
	
%>
