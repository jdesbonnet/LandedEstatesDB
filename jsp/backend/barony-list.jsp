<%@include file="_header.jsp"%><%long time = -System.currentTimeMillis();
		
	context.put ("tabId","estates");
	
	

	HashMap<String, Barony> baronyHash = new HashMap<String,Barony>();

	List<Barony> allBaronies = hsession.createQuery("from Barony").list();
	/*
	for (Barony barony : allBaronies) {
		baronyHash.put(barony.getName(),barony);
	}
	*/

	List<House> allHouses = hsession.createQuery ("from Property").list();
	for (House house : allHouses) {
		Barony barony = baronyHash.get(house.getBarony());
		if ( barony == null ) {
			barony = new Barony();
			barony.setName(house.getBarony());
			baronyHash.put(house.getBarony(), barony);
			//hsession.save(barony);
		}
		barony.getHouses().add(house);
	}
	
	int n;
	double slat,slon;
	for (Barony b : baronyHash.values()) {
		n=0; slat=0; slon=0;
		for (House house : b.getHouses()) {
			if (house.getLatitude()!=null && house.getLongitude() != null) {
				slat += house.getLatitude();
				slon += house.getLongitude();
				n++;
			}
		}
		if (n > 0) {
			b.setLatitude(slat/n);
			b.setLongitude(slon/n);
		}
		
	}
	
	ArrayList<Barony> suspectBaronies = new ArrayList<Barony>();
	for (Barony barony : baronyHash.values()) {
		if (barony.getHouses().size() < 3) {
			suspectBaronies.add(barony);
		}
	}
	context.put ("baronies", suspectBaronies);
	templates.merge ("/backend/barony-list.vm",context,out);
	out.flush();
	
	time += System.currentTimeMillis();
	System.err.println ("barony-list.jsp: " + time + "ms");%>
