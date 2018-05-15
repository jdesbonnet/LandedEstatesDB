<%

	Long houseId = new Long(request.getParameter("id"));
	House house = (House)em.find(House.class, houseId);
	
	context.put ("house", house);
	
	// What estate or estates does this house belong to?
	String query = "SELECT e from Estate AS e "
			+ " JOIN e.houses as h"
			+ " WHERE h = :house";
	List<Estate> estates = em.createQuery(query)
			.setParameter("house",house)
			.getResultList();
	context.put ("estates",estates);
	
	context.put ("revisionHistory" , db.getRevisionHistory(em, 
			house.getClass().getName(), 
			houseId));
	
	context.put("pageId","./house-show");
	context.put("showSideCol","true");
	templates.merge ("/backend/master.vm",context,out);
%>
