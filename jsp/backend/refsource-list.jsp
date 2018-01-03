<%@include file="_header.jsp"%><%
	context.put ("tabId","refsources");
	context.put ("referenceSources", 
			em.createQuery("from ReferenceSource order by name")
			.getResultList());
	context.put ("pageId","./refsource-list");
	templates.merge ("/backend/master.vm",context,out);
%>
