<%@include file="_header.jsp"%><%if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


House property;
try {
	Long id = new Long(request.getParameter("property_id"));
	property = (House)hsession.load(House.class,id);
} catch (Exception e) {
	throw new ServletException(e);
}

Image image;
try {
	Long id = new Long (request.getParameter("id"));
	image = (Image)hsession.load(imageEntityName,id);
} catch (Exception e) {
	throw new ServletException(e);
}

if (property.getImages().contains(image)) {
	property.getImages().remove(image);
	hsession.save(property);
	hsession.delete(image);
}

	response.sendRedirect("property-edit.jsp?id=" + property.getId());%>