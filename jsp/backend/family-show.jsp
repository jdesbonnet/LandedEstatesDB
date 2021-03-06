<%@include file="_header.jsp"%><%
	
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

	templates.merge ("/backend/family-show.vm",context,out);
%>
