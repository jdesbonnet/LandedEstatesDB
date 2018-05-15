<%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}


	Long estateId = new Long (request.getParameter("estate_id"));
	Estate estate = (Estate)em.find(Estate.class, estateId);

	House house = new House();
	house.setName(XSSClean.clean(request.getParameter("name")));
	
	try {
		house.setProjectPhase (new Integer(request.getParameter("project_phase")));
	} catch (Exception e) {
		// ignore
	}
	
	em.persist(house);
	
	estate.getHouses().add(house);
	
	response.sendRedirect("house-edit.jsp?id=" 
	+ house.getId() 
	+ "&estate_id=" + estate.getId());%>
