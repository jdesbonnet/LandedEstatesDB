<%@include file="_header.jsp"%><%

	Long houseId = new Long(request.getParameter("id"));
	House house = em.find(House.class,houseId);
	out.clear();
response.setContentType("application/vnd.google-earth.kml+xml");%><?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://earth.google.com/kml/2.0">
	<Placemark>
		<name> <%=TextUtil.xmlSafe(house.getName())%></name>
		<Point>
			<coordinates>
			<%=house.getLongitudeDeg()%>,
			<%=house.getLatitudeDeg()%>,
			0
			</coordinates>
		</Point>
	</Placemark>
</kml>