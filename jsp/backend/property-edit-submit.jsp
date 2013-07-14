<%@page import="ie.wombat.gis.OSIGridReference"
import="ie.wombat.gis.convert.OSILLAConvert"
%><%@include file="_header.jsp"%><%if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long id = new Long(request.getParameter("id"));
	House property = (House)em.find(House.class, id);

	property.setName(request.getParameter("name"));
	property.setDescription(request.getParameter("description"));
	
	property.setTownland(request.getParameter("townland"));
	property.setCivilParish(request.getParameter("civil_parish"));
	property.setDed(request.getParameter("ded"));
	property.setPlu(request.getParameter("plu"));
	property.setBarony(request.getParameter("barony"));
	property.setCounty(request.getParameter("county"));
	
	property.setOsSheet(request.getParameter("os_sheet"));
	property.setDiscoveryMap(request.getParameter("discovery_map"));
	
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
				property.setGridReference(gr.getGridReference(OSIGridReference.FORMAT_XEEENNN));
			} catch (NumberFormatException e) {
				// ignore
			}
		}
	} else {
		property.setGridReference(gridRef);
	}
	em.persist(property);
	response.sendRedirect ("house-show.jsp?id="+property.getId());%>
