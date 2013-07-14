<%@include file="_header.jsp"%><%if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	// How much memory available prior to upload?
			 
	Runtime rt = Runtime.getRuntime();
	System.err.println("LandedEstates: memory prior to upload: total=" + rt.totalMemory()
		+ " free=" + rt.freeMemory());

	Properties params = new Properties();
  	Image[] images = ie.wombat.imagetable.ImageDB.getInstance(imageEntityName).handleImageUpload (hsession, request, params);	
 
	System.err.println("LandedEstates: memory after image upload: total=" + rt.totalMemory()
			+ " free=" + rt.freeMemory());
	House property;
	try {
		Long propertyId = new Long(params.getProperty("property_id"));
		property = (House)hsession.load(House.class,propertyId);
	} catch (Exception e) {
			throw new ServletException(e.toString());
	}
	
	for (int i = 0; i < images.length; i++) {
			property.getImages().add(images[i]);
	}
	
	hsession.save(property);
	
	response.sendRedirect ("property-show.jsp?id=" + property.getId());%>