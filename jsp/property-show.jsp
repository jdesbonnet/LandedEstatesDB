<%@include file="_header.jsp"%><%// Required 
	// id: (property id)
	
	House property;
	try {
		Long id = new Long(request.getParameter("id"));
		property = (House)hsession.load(House.class,id);
		/*
		if (property.getProjectPhase() != 1) {
			response.sendRedirect ("not-available.jsp");
			return;
		}
		*/
		
	} catch (Exception e) {
		out.println ("error: " + e);
		return;
	}
	context.put ("tabId","houses");
	
	String query = "from Estate as e where e.houses.id=" + property.getId();

	List<Estate> estates = hsession.createQuery(query).list();
	context.put ("estates",estates);
	
	context.put ("property", property);
	
	context.put ("pageTitle", property.getName());
	
	if (property.hasGridReference()) {
		context.put ("onLoadFunction","drawHouseMap("
				+ property.getLatitudeDeg()
				+ ","
				+ property.getLongitudeDeg()
				+ ")"
				);
	}

	templates.merge ("/property-show.vm",context,out);%>
