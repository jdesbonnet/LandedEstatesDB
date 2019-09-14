<%context.put ("tabId","images");
	
	
	Long id = new Long (request.getParameter("id"));
	Image image = em.find(Image.class,id);
	
	context.put ("image", image);
	
	
	//	 Identify associated House records
	List<House> houses = em
	.createQuery("from House where :image MEMBER OF images")
	.setParameter("image",image)
	.getResultList();
	
	context.put("houses", houses);
	
	if (houses.size() == 0) {
		throw new ServletException ("associated House for image " + id + " not found");
	}
	context.put ("house",houses.get(0));
	
	
	// Find Estate or Estates associated with this House record
	/*
	List<Estate> estates = em
	.createQuery("from Estate where house...")
	query = "from Estate where houses.id=" + property.getId();
	list = em.createQuery(query).getResultList();
	if (list.size() == 0) {
		throw new ServletException ("could not find associated Estate record to image " + id);
	}
	if (list.size() == 1) {
		context.put ("estate",list.get(0));
	}
	context.put ("estates", list);
	*/
	
	//templates.merge ("/backend/image-show.vm",context,out);
	
	context.put("pageId","./image-show");
	//context.put("showSideCol","true");
	templates.merge ("/backend/master.vm",context,out);
	
%>