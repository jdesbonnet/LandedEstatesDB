<%@include file="_header.jsp"%><%context.put ("tabId","home");
	
	/*
	 * All properties with grid reference
	 */
	 
	House[] properties = db.getPropertiesByGridReference(hsession, 200000,200000,500000);
	context.put ("properties", properties);
		
	/*
	 * Calculate centroid 
	 */
	double cx=0,cy=0;
	int nprop = 0;
	for (int i = 0; i < properties.length; i++) {
		if (properties[i].getLongitudeDeg() == 0 
			&& properties[i].getLatitudeDeg() == 0) {
			System.err.println ("skipping house " + properties[i].getId()
			+ " because of 0,0 location");
			continue;
		}
		cx+=properties[i].getLongitudeDeg();
		cy+=properties[i].getLatitudeDeg();
		nprop++;
	}
	cx /= (double)nprop;
	cy /= (double)nprop;

	context.put ("cx",new Double(cx));
	context.put ("cy",new Double(cy));
	
	templates.merge ("/index.vm",context,out);%>
