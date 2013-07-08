<%@include file="_header.jsp"%><%

	double latMin = Double.parseDouble(request.getParameter("latmin"));
	double latMax = Double.parseDouble(request.getParameter("latmax"));
	double lonMin = Double.parseDouble(request.getParameter("lonmin"));
	double lonMax = Double.parseDouble(request.getParameter("lonmax"));

	System.err.println ("latmin="+latMin + " lonmin="+lonMin 
			+ " latmax=" + latMax + " lonmax=" + lonMax);
	
	List<Property> houses = hsession.createQuery("from Property where "
			+ " latitude>=:latMin and latitude<:latMax "
			+ " and longitude>=:lonMin and longitude<:lonMax "
			//+ " and projectPhase=1"
			)
			.setDouble("latMin",latMin)
			.setDouble("latMax",latMax)
			.setDouble("lonMin",lonMin)
			.setDouble("lonMax",lonMax)
			.list();
	
	out.clear();
	response.setContentType("text/plain");
	out.write ("[\n");
	for (Property house : houses) {
		out.write ("{id:" + house.getId() + ",");
		out.write ("lat:" + house.getLatitude() + ",");
		out.write ("lon:" + house.getLongitude() + ",");
		if (house.hasImage()) {
			Image image = house.getImage();
			out.write ("image_id: " + image.getId() + ",");
		}
		out.write ("name:\"" + escape(house.getName()) + "\"},\n");
	}
	out.write ("]\n");
	
	// Experimental heat map record
	HeatMapRecord hmr = new HeatMapRecord();
	hmr.setIpNumber(request.getRemoteAddr());
	hmr.setSessionId(request.getSession().getId());
	hmr.setAgent(request.getHeader("User-Agent"));
	hmr.setSwLatitude(latMin);
	hmr.setSwLongitude(lonMin);
	hmr.setNeLatitude(latMax);
	hmr.setNeLongitude(lonMax);
	hsession.save(hmr);
	
%>
