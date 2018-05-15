<%
	context.put ("tabId","refsources");
	context.put ("referenceSources", em.createQuery("from ReferenceSource order by name").getResultList());
	templates.merge ("/refsource-list.vm",context,out);
%>
