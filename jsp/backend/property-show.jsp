<%@include file="_header.jsp"%><%

	Property property;
	try {
		Long id = new Long(request.getParameter("id"));
		property = (Property)hsession.load(Property.class,id);
	} catch (Exception e) {
		out.println ("error: " + e);
		return;
	}
	context.put ("tabId","houses");
	
	String query = "from Estate as e where e.houses.id=" + property.getId();
	System.err.println (query);
	List estates = hsession.createQuery(query).list();
	context.put ("estates",estates);
	
	context.put ("property", property);
	
	templates.merge ("/backend/property-show.vm",context,out);
%>
