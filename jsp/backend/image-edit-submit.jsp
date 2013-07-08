<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Long id = new Long(request.getParameter("id"));
	Image image = (Image)hsession.load(imageEntityName, id);
	
	image.setCaption(request.getParameter("caption"));
	image.setDescription(request.getParameter("description"));
	
	hsession.save(image);

	if (request.getParameter("next")!=null) {
		response.sendRedirect(request.getParameter("next"));
		return;
	}
%>