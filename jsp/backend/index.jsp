<%@include file="_header.jsp"%><%context.put ("tabId","home");
	
	/*
	 * All houses with grid reference
	 */
	 
	List<House> houses = db.getHousesByGridReference(em, 200000,200000,500000);
	context.put ("houses", houses);
		
	/*
	 * Calculate centroid 
	 */
	double cx=0,cy=0;
	int nprop = 0;
	for (House house : houses) {
		if (house.getLongitudeDeg() == 0 
			&& house.getLatitudeDeg() == 0) {
			System.err.println ("skipping house " + house.getId()
			+ " because of 0,0 location");
			continue;
		}
		cx+=house.getLongitudeDeg();
		cy+=house.getLatitudeDeg();
		nprop++;
	}
	cx /= (double)nprop;
	cy /= (double)nprop;

	context.put ("cx",new Double(cx));
	context.put ("cy",new Double(cy));
	
	templates.merge ("/index.vm",context,out);%>
