<%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Long id = new Long(request.getParameter("id"));
	Image image = (Image)em.find(Image.class, id);
	
	image.setCaption(XSS.clean(request.getParameter("caption")));
	image.setDescription(XSS.clean(request.getParameter("description")));
	
	em.persist(image);

	if (request.getParameter("next")!=null) {
		response.sendRedirect(request.getParameter("next"));
		return;
	}
%>