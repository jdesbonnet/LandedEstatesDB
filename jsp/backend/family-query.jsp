<%@include file="_header.jsp"%><%
	String query = request.getParameter("query");
	List<Family> families = em.createQuery("from Family where name like :q order by name")
							.setParameter("q",query + "%")
							.getResultList();
	for(Family family : families) {
		out.write (family.getName());
		out.write (" [");
		out.write (family.getId().toString());
		out.write ("]\n");
	}
	
%>
