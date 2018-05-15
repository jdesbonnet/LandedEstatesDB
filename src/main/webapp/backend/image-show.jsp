<%context.put ("tabId","images");
	
	
	Long id = new Long (request.getParameter("id"));
	context.put ("image", em.find(Image.class,id));
	
	String query;
	List list;
	
	//	 Identify associated property
	query = "from Property where images.id=" + id;
	list = em.createQuery(query).getResultList();
	if (list.size() == 0) {
		throw new ServletException ("associated property for image " + id + " not found");
	}
	House property = (House)list.get(0);
	context.put ("property",property);
	
	
	// Find Estate or Estates associated with this Property record
	query = "from Estate where houses.id=" + property.getId();
	list = em.createQuery(query).getResultList();
	if (list.size() == 0) {
		throw new ServletException ("could not find associated Estate record to image " + id);
	}
	if (list.size() == 1) {
		context.put ("estate",list.get(0));
	}
	context.put ("estates", list);
	
	templates.merge ("/backend/image-show.vm",context,out);%>