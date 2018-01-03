<%@include file="_header.jsp"%><%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long refSourceId = new Long(request.getParameter("id"));
	
	ReferenceSource source = em.find(ReferenceSource.class,refSourceId);
	
	if (source == null) {
		source = new ReferenceSource ();
	}

	context.put ("source", source);
	context.put ("categories", em.createQuery("from ReferenceCategory").getResultList());
	
	context.put ("pageId","./refsource-edit");
	templates.merge ("/backend/master.vm",context,out);
%>
