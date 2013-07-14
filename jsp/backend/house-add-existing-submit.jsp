<%@include file="_header.jsp"%><%if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Estate estate;
	House property;
	try {
		Long estateId = new Long (request.getParameter("estate_id"));
		estate = (Estate)hsession.load(Estate.class, estateId);
		
		// Handle both autocomplete form (param 'property') and simple
		// pull down menu (param 'property_id').
		Long propertyId;
		if (request.getParameter("property") != null) {
			propertyId= DB.getIdFromAutoCompleteField(request.getParameter("property"));
		} else {
			propertyId = new Long(request.getParameter("property_id"));
		}
		
		property = (House)hsession.load(House.class, propertyId);
	} catch (Exception e) {
		throw new ServletException (e.toString());
	}
	
	estate.getProperties().add(property);
	hsession.save(estate);
	
	response.sendRedirect("estate-edit.jsp?id=" + estate.getId());%>
