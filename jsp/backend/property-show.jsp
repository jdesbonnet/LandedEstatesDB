<%@include file="_header.jsp"%><%

	Long houseId = new Long(request.getParameter("id"));
	Property house = (Property)em.find(Property.class, houseId);
	context.put ("property", house);

	context.put ("tabId","houses");
	
	String query = "from Estate as e "
			+ " inner join fetch e.houses as house"
			+ " where house.id=" + house.getId();
	
	List<Property> estates = em.createQuery(query).getResultList();
	context.put ("estates",estates);
	
	
	templates.merge ("/backend/property-show.vm",context,out);
%>
