<%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}


	ImageUpload  upload = new ImageUpload(request);
	Properties props = upload.getProperties();
	Long houseId = new Long(props.getProperty("house_id"));
	House house = em.find(House.class,houseId);

	// How much memory available prior to upload?
	Runtime rt = Runtime.getRuntime();
	log.info("LandedEstates: memory prior to upload: total=" + rt.totalMemory()
		+ " free=" + rt.freeMemory());
	
	
	ImageDB imageDb = ImageDB.getInstance("Image");
	List<Image> images = imageDb.handleImageUpload (em, upload);	

	System.err.println("LandedEstates: memory after image upload: total=" + rt.totalMemory()
			+ " free=" + rt.freeMemory());

	for (Image image : images) {
		house.getImages().add(image);
	}
	
	response.sendRedirect ("house-show.jsp?id=" + house.getId() + "#images");
%>
