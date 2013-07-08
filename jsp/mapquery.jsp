<%@include file="_header.jsp"%><%

	double latMin = Double.parseDouble(request.getParameter("latmin"));
	double latMax = Double.parseDouble(request.getParameter("latmax"));
	double lonMin = Double.parseDouble(request.getParameter("lonmin"));
	double lonMax = Double.parseDouble(request.getParameter("lonmax"));

	List<Property> houses = db.getHousesInBoundingBox(hsession, latMin,latMax,lonMin,lonMax);
	
	// Filter for phase 1 records
	/*
	List<Property> houses2 = new ArrayList<Property>(houses.size());
	for (Property house : houses) {
		if (house.getProjectPhase() == 1) {
			houses2.add(house);
		}
	}
	houses = houses2;
	*/
	
	out.clear();
	response.setContentType("text/plain");
	out.write ("[\n");
	for (Property house : houses) {
		out.write ("{id:" + house.getId() + ",");
		out.write ("lat:" + house.getLatitudeDeg() + ",");
		out.write ("lon:" + house.getLongitudeDeg() + ",");
		if (house.hasImage()) {
			Image image = house.getImage();
			out.write ("image_id: " + image.getId() + ",");
		}
		out.write ("name:\"" + escape(house.getName()) + "\"},\n");
	}
	out.write ("]\n");
%>
