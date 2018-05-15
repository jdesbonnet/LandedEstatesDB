<%@page import="ie.wombat.gis.OSIGridReference"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ie.wombat.imagetable.ImageDB"%>
<%@page import="ie.wombat.imagetable.Image"%>
<%
	if (!user.hasWriteAccess()) {
		throw new ServletException("No write access to this database");
	}
	//Get all existing Barony objects and put in a hash
	HashMap<String, Barony> baronyHash = new HashMap<String, Barony>();
	List<Barony> allBaronies = em.createQuery("from Barony").getResultList();
	for (Barony barony : allBaronies) {
		baronyHash.put(barony.getName(), barony);
	}

	// Iterate through all house records and check if there is a corresponding
	// Barony object. If not then create it.
	List<House> allHouses = em.createQuery("from Property").getResultList();
	for (House house : allHouses) {
		String baronyName = house.getBarony();
		Barony barony = baronyHash.get(baronyName);
		if (barony == null) {
			barony = new Barony();
			barony.setName(baronyName);
			baronyHash.put(baronyName, barony);
			em.persist(barony);
		}
		barony.getHouses().add(house);
	}

	int n;
	double slat, slon, lat, lon;
	for (Barony b : baronyHash.values()) {
		// Calculate centroid of barony
		n = 0;
		slat = 0;
		slon = 0;
		for (House house : b.getHouses()) {
			if (house.getLatitude() != null && house.getLongitude() != null) {
				lat = house.getLatitude();
				lon = house.getLongitude();
				if (lat < 51.3 || lat > 55.0 || lon < -10.5 || lon > -6) {
					out.print("Warning: House#" + house.getId() + " outside allowed range lat=" + lat + " lon="
							+ lon + "<br />");
				}

				slat += house.getLatitude();
				slon += house.getLongitude();
				n++;
			}
		}
		slat /= n;
		slon /= n;
		if (n > 0) {
			b.setLatitude(slat);
			b.setLongitude(slon);
		}

	}
%>
all done!
