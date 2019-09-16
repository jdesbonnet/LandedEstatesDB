<%@page import="ie.wombat.gis.OSIGridReference"
import="ie.wombat.gis.convert.OSILLAConvert"
%><%if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long id = new Long(request.getParameter("id"));
	House house = (House)em.find(House.class, id);

	
	// TODO: XSS clean all these inputs
	
	house.setName(request.getParameter("name"));
	house.setDescription(request.getParameter("description"));
	
	house.setTownland(request.getParameter("townland"));
	house.setCivilParish(request.getParameter("civil_parish"));
	house.setDed(request.getParameter("ded"));
	house.setPlu(request.getParameter("plu"));
	house.setBarony(request.getParameter("barony"));
	house.setCounty(request.getParameter("county"));
	
	house.setOsSheet(request.getParameter("os_sheet"));
	house.setDiscoveryMap(request.getParameter("discovery_map"));
	
	String gridRef = request.getParameter("grid_ref").trim();
	
	// If in (lat,lon) format convert to OSI XEEENNN format (100m)
	if (gridRef.startsWith("(") && gridRef.endsWith(")")) {
		// (lon,lat) format
		String location = gridRef.substring(1,gridRef.length()-1);
		System.err.println ("location = " + location);
		String[] p = location.split (",");
		if (p.length == 2) {
			try {
				double lon = Double.parseDouble(p[1]);
				double lat = Double.parseDouble(p[0]);
				OSILLAConvert cvt = new OSILLAConvert ();
				double[] ngr = cvt.lla2ng(lat * Math.PI/180,lon * Math.PI/180,0);
				OSIGridReference gr = new OSIGridReference ((int)ngr[0],(int)ngr[1]);
				house.setGridReference(gr.getGridReference(OSIGridReference.FORMAT_XEEENNN));
			} catch (NumberFormatException e) {
				// ignore
			}
		}
	} else {
		house.setGridReference(gridRef);
	}
	

	house.setLatitude(new Double(request.getParameter("latitude")));
	house.setLongitude(new Double(request.getParameter("longitude")));

	db.postEntityUpdate(em, user, house);

	db.index(em,house);
	
	response.sendRedirect ("house-show.jsp?id="+house.getId());%>
