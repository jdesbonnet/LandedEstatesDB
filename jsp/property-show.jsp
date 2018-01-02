<%@page import="java.util.HashSet"%>
<%@include file="_header.jsp"%><%// Required 
	
	Long houseId = new Long(request.getParameter("id"));
	House house = em.find(House.class, houseId);
	context.put ("property", house);
	
	context.put ("tabId","houses");
	
	Set<House> houses = new HashSet<>();
	houses.add(house);
	
	// Select Estate records that have this house a member
	List<Estate> estates = em
			.createQuery("SELECT e FROM Estate AS e JOIN e.houses AS h where h = :house")
			.setParameter("house",house)
			.getResultList();
	context.put ("estates",estates);
	
	
	context.put ("pageTitle", house.getName());
	
	if (house.hasGridReference()) {
		context.put ("onLoadFunction","drawHouseMap("
				+ house.getLatitudeDeg()
				+ ","
				+ house.getLongitudeDeg()
				+ ")"
				);
	}

	templates.merge ("/property-show.vm",context,out);%>
