<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}


	// How much memory available prior to upload?
			 
	Runtime rt = Runtime.getRuntime();
	System.err.println("LandedEstates: memory prior to upload: total=" + rt.totalMemory()
		+ " free=" + rt.freeMemory());
	
	Long houseId = new Long(request.getParameter("house_id"));
	House house = em.find(House.class,houseId);

	List<Image> images = ImageDB.getInstance("Image").handleImageUpload (em, request);	

	System.err.println("LandedEstates: memory after image upload: total=" + rt.totalMemory()
			+ " free=" + rt.freeMemory());

	for (Image image : images) {
		house.getImages().add(image);
	}
	
	response.sendRedirect ("house-show.jsp?id=" + house.getId() + "#images");
%>
