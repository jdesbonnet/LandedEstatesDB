<%!public static int calcDistanceInKm (House p0, House p1) {
		int dx = p1.getEasting() - p0.getEasting();
		int dy = p1.getNorthing() - p0.getNorthing();
		int d2 = dx*dx + dy*dy;
		return (int)(Math.sqrt((double)d2)/1000);
	}%><%House property;
	try {
		Long id = new Long(request.getParameter("id"));
		property = (House)em.find(House.class,id);
	} catch (Exception e) {
		out.println ("error: " + e);
		return;
	}
	
	DB db = DB.getInstance();
	
	float radius = db.DEFAULT_NEARBY_RADIUS;
	if (request.getParameter("r_km") != null) {
		try {
			radius = Float.parseFloat(request.getParameter("r_km")) * 1000;
		} catch (NumberFormatException e) {
			// ignore
		}
	}
	
	context.put ("property", property);
	context.put ("radius", new Float(radius));
	context.put ("radiusKm", new Float ((float)radius/1000));
	
	context.put ("mapSystem","YMap");
	
	if (property.hasGridReference()) {
		int easting = property.getEasting();
		int northing = property.getNorthing();
		context.put ("properties", db.getHousesByGridReference(em, easting,northing,(int)radius));
	}
	
	// reference to this JSP for utility methods defined above
	context.put ("jsp", this);
	
	templates.merge ("/property-nearby-map.vm",context,out);%>
