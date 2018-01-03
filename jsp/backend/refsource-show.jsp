<%@include file="_header.jsp"%><%
	
	Long sourceId = new Long(request.getParameter("id"));
	ReferenceSource source = em.find(ReferenceSource.class,sourceId);
	
	context.put ("source", source);
	context.put ("categories", em.createQuery("from ReferenceCategory order by name").getResultList());
	
	
	List<Estate> hitList = em
	.createQuery("select distinct estate from Estate as estate "
	+ " join estate.references as reference "
	+ " where reference.source.id= :sourceId ")
	.setParameter("sourceId",sourceId)
	.getResultList();

	
	context.put ("tabId","refsources");
	
	context.put ("records", hitList);
	
	context.put ("pageId","./refsource-show");
	templates.merge ("/backend/master.vm",context,out);
%>
