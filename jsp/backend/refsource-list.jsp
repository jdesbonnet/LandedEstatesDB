<%@include file="_header.jsp"%><%
	context.put ("tabId","refsources");
	context.put ("referenceSources", 
			em.createQuery("from ReferenceSource order by name")
			.getResultList());
	templates.merge ("/backend/refsource-list.vm",context,out);
%>
