<%@include file="_header.jsp"%><%

Property property;
try {
	Long id = new Long(request.getParameter("id"));
	property = (Property)hsession.load(Property.class,id);
} catch (Exception e) {
	out.println ("error: " + e);
	return;
}

out.clear();
response.setContentType("application/vnd.google-earth.kml+xml");

%><?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://earth.google.com/kml/2.0">
  <Placemark>
    <name> <%=TextUtil.xmlSafe(property.getName())%></name>
    <Point>
      <coordinates><%=property.getLongitudeDeg()%>,<%=property.getLatitudeDeg()%>,0</coordinates>
    </Point>
  </Placemark>
</kml>