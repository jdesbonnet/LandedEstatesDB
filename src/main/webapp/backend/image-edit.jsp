<%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Long id = new Long(request.getParameter("id"));
	Image image = (Image)em.find(Image.class, id);
	context.put ("image",image);
	
	// What hose does this image belong to?
	List<House> houses = em.createQuery("from House where :image MEMBER OF images")
			.setParameter("image",image)
			.getResultList();
	if(houses.size()>1) {
		log.error("Found Image#" + image.getId() + " used for more than one house record");
	}
	if (houses.size()>0) {
		context.put("house",houses.get(0));
	}
	
	context.put ("next", request.getHeader("Referer"));
	
	context.put("pageId","./image-edit");
	//context.put("showSideCol","true");
	templates.merge ("/backend/master.vm",context,out);

%>