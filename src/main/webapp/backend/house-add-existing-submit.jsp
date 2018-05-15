<%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long estateId = new Long (request.getParameter("estate_id"));

	if (request.getParameter("_cancel")!=null) {
		response.sendRedirect("estate-edit.jsp?id=" + estateId);
		return;
	}
	
	Estate estate = em.find(Estate.class,estateId);
	
	Long houseId = new Long(request.getParameter("house_id"));
	House house = em.find(House.class, houseId);
	
	estate.getHouses().add(house);
		
	response.sendRedirect("estate-edit.jsp?id=" + estate.getId());
%>
