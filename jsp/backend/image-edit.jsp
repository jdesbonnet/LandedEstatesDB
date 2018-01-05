<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Long id = new Long(request.getParameter("id"));
	Image image = (Image)em.find(imageEntityName, id);
	context.put ("image",image);
	context.put ("next", request.getHeader("Referer"));
	
	//context.put ("alltags", ImageDB.getInstance().getAllTags(hsession));
	
	context.put ("tabId","images");
	templates.merge ("/backend/image-edit.vm",context,out);
	
%>