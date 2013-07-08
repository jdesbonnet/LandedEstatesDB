<%@include file="_header.jsp"%><%

	double latMin = Double.parseDouble(request.getParameter("latmin"));
	double latMax = Double.parseDouble(request.getParameter("latmax"));
	double lonMin = Double.parseDouble(request.getParameter("lonmin"));
	double lonMax = Double.parseDouble(request.getParameter("lonmax"));

	List<Barony> baronies = hsession.createQuery("from Barony where "
			+ " latitude>=:latMin and latitude<:latMax "
			+ " and longitude>=:lonMin and longitude<:lonMax ")
			.setDouble("latMin",latMin)
			.setDouble("latMax",latMax)
			.setDouble("lonMin",lonMin)
			.setDouble("lonMax",lonMax)
			.list();
	
	out.clear();
	response.setContentType("text/plain");
	out.write ("[\n");
	for (Barony barony : baronies) {
		out.write ("{id:" + barony.getId() + ",");
		out.write ("lat:" + barony.getLatitude() + ",");
		out.write ("lon:" + barony.getLongitude() + ",");
		out.write ("name:\"" + escape(barony.getName()) + "\"},\n");
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
