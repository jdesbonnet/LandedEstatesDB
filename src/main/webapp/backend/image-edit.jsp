<%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Long id = new Long(request.getParameter("id"));
	Image image = (Image)em.find(Image.class, id);
	context.put ("image",image);
	context.put ("next", request.getHeader("Referer"));
	
	context.put("pageId","./image-edit");
	//context.put("showSideCol","true");
	templates.merge ("/backend/master.vm",context,out);

%>