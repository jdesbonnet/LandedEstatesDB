<%@include file="_header.jsp"%><%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long referenceId = new Long(request.getParameter("id"));
	Reference reference = em.find(Reference.class,referenceId);
	context.put ("reference", reference);

	Long estateId = new Long(request.getParameter("estate_id"));
	Estate estate = em.find(Estate.class,estateId);
	context.put("estate",estate);
	
	context.put ("referenceSources", em.createQuery("from ReferenceSource order by name").getResultList());
	
	context.put ("pageId","reference-edit");
	templates.merge ("/backend/master.vm",context,out);
	
%>
