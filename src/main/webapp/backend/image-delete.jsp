<%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


House house;
try {
	Long id = new Long(request.getParameter("house_id"));
	house = (House)em.find(House.class,id);
} catch (Exception e) {
	throw new ServletException(e);
}

Image image=null;
try {
	Long id = new Long (request.getParameter("id"));
	image = (Image)em.find(Image.class,id);
} catch (Exception e) {
	throw new ServletException(e);
}

if (house.getImages().contains(image)) {
	house.getImages().remove(image);
	em.remove(image);
}

	response.sendRedirect("house-edit.jsp?id=" + house.getId());
	
%>
