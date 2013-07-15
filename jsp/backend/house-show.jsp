<%@include file="_header.jsp"%><%Long houseId = new Long(request.getParameter("id"));

	House house = (House)em.find(House.class, houseId);
	context.put ("house", house);
	context.put ("tabId","houses");
	
	String query = "from Estate as e "
			+ " inner join fetch e.houses as house"
			+ " where house.id=" + house.getId();
	
	List<House> estates = em.createQuery(query).getResultList();
	context.put ("estates",estates);
	
	
	templates.merge ("/backend/house-show.vm",context,out);%>
