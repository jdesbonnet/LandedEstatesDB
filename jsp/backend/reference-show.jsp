<%@include file="_header.jsp"%><%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long referenceId = new Long(request.getParameter("id"));
	Reference reference = em.find(Reference.class,referenceId);
	
	// What estate referenced this?
	List<Estate> list = em
	.createQuery("SELECT e from Estate AS e JOIN e.references AS r where r=:reference")
	.setParameter("reference",reference)
	.getResultList();
	
	Estate estate = list.get(0);
	context.put ("estate",estate);
	
		
	context.put ("reference", reference);
	context.put ("pageId","reference-show");
	
	templates.merge ("/backend/master.vm",context,out);
	
%>
