<%!

	public static int calcDistanceInKm (House p0, House p1) {
		int dx = p1.getEasting() - p0.getEasting();
		int dy = p1.getNorthing() - p0.getNorthing();
		int d2 = dx*dx + dy*dy;
		return (int)(Math.sqrt((double)d2)/1000);
	}%><%
            
            House house;
	try {
		Long id = new Long(request.getParameter("id"));
		house = (House)em.find(House.class,id);
	} catch (Exception e) {
		out.println ("error: " + e);
		return;
	}
	
	float radius = db.DEFAULT_NEARBY_RADIUS;
	if (request.getParameter("r_km") != null) {
		try {
			radius = Float.parseFloat(request.getParameter("r_km")) * 1000;
		} catch (NumberFormatException e) {
			// ignore
		}
	}
	
	context.put ("house", house);
	context.put ("radius", new Float(radius));
	context.put ("radiusKm", new Float ((float)radius/1000));
	
	context.put ("useGoogleMap","true");
	
	if (house.hasGridReference()) {
		int easting = house.getEasting();
		int northing = house.getNorthing();
		context.put ("houses", db.getHousesByGridReference(em, easting,northing,(int)radius));
	}
	
	// reference to this JSP for utility methods defined above
	context.put ("jsp", this);
	        
	context.put("pageId","./house-nearby-map");
	context.put("showSideCol","false");
	templates.merge ("/backend/master.vm",context,out);
    
%>
