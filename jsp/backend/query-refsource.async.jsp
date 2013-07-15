<%@include file="_header.jsp"%><%

	String q = request.getParameter("q");
	List<ReferenceSource> list = em
			.createQuery("from ReferenceSource where name like :q order by name")
			.setParameter("q","%"+q+"%")
			.getResultList();
		
	response.setContentType("application/json");
	out.write ("[");
	
	boolean first = true;
	for (ReferenceSource refSource : list) {
		if (first) {
			first = false;
		} else {
			out.write (",");
		}
		out.write ("{\"label\":");
		String label = refSource.getName();
		if (refSource.getCategory() != null) {
			label += " (" + refSource.getCategory().getName() + ")";
		}
		out.write(JSONUtils.quote(label));
		out.write (",\"value\":");
		out.write (refSource.getId().toString());
		out.write ("}\n");
	}
	out.write ("]");
%>