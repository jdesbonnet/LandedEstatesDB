<%
	
	Long id = new Long(request.getParameter("id"));
	
	Family family = (Family)em.find(Family.class,id);
	
	context.put ("tabId","families");
	context.put ("family", family);

	List<Estate> estates = em.createQuery ("from Estate as e "
			+ " inner join fetch e.families as family"
			+ " where family.id=:familyId")
			.setParameter("familyId",family.getId())
			.getResultList();
	context.put ("estates",estates);

	context.put("pageId","./family-show");
	context.put("showSideCol","true");
	templates.merge ("/backend/master.vm",context,out);
%>
