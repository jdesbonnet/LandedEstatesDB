<%@include file="_header.jsp"%><%

	Long houseId = new Long(request.getParameter("id"));
	House house = (House)em.find(House.class, houseId);
	
	context.put ("house", house);
	
	String query = "from Estate AS e "
			+ " JOIN e.houses as h"
			+ " WHERE h = :house";
	
	List<House> estates = em.createQuery(query)
			.setParameter("house",house)
			.getResultList();
	context.put ("estates",estates);
	
	
	context.put("pageId","./house-show");
	templates.merge ("/backend/master.vm",context,out);%>
