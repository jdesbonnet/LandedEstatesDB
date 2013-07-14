<%@include file="_header.jsp"%><%if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Long estateId = null;
	Estate estate;
	try {
		estateId = new Long (request.getParameter("estate_id"));
		estate = (Estate)hsession.load(Estate.class,estateId);
	} catch (Exception e) {
		throw new ServletException (e.toString());
	}
	
	House property;
	try {
		Long propertyId = new Long (request.getParameter("id"));
		property = (House)hsession.load(House.class, propertyId);
	} catch (Exception e) {
		throw new ServletException (e.toString());
	}
	
	estate.getProperties().remove(property);

	response.sendRedirect ("estate-edit.jsp?id="+estateId);%>
