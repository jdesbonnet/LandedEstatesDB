<%@include file="_header.jsp"%><%context.put ("tabId","images");
	
	
	Long id = new Long (request.getParameter("id"));
	Image image = (Image)em.find(Image.class,id);
	context.put ("image", image);
		
	//	 Identify associated house
	String query="SELECT h FROM House AS h JOIN h.images AS i where i = :image";
	List<House>houses = em.createQuery(query)
	.setParameter("image",image)
	.getResultList();
	if (houses.size() == 0) {
		throw new ServletException ("associated house for image " + id + " not found");
	}
	House house = houses.get(0);
	context.put ("property",house);
	
	
	// Find Estate or Estates associated with this Property record
	query = "SELECT e FROM Estate AS e JOIN e.houses h where h = :house";
	List<Estate>estates = em.createQuery(query)
			.setParameter("house",house)
			.getResultList();
	if (estates.size() == 0) {
		throw new ServletException ("could not find associated Estate record to image " + id);
	}
	if (estates.size() == 1) {
		context.put ("estate",estates.get(0));
	}
	context.put ("estates", estates);
	
	context.put ("pageTitle","Landed Estates Image#" + id
			+ ": " + house.getName());
	context.put ("pageDescription","Caption: " + image.getCaption() );
	
	templates.merge ("/image-show.vm",context,out);%>