<%@include file="_header.jsp"%><%/*
	 * Query service for autocomplete field
	 */
	 
	String query = request.getParameter("q").trim();
	
	List<House> houses = em
			.createQuery("from House where name like :name order by name")
			.setParameter("name",query+"%")
			.getResultList();

	out.write ("[\n");
	
	boolean first = true;
	for (House house : houses) {
		if (first) {
			first=false;
		} else {
			out.write(",");
		}
		out.write("{\"id\":" + house.getId());
		out.write(",\"name\":" + JSONUtils.quote(house.getName()));
		out.write("}");
	}

	out.write ("]\n");
	
	%>