<%@include file="_header.jsp"%><%
	context.put ("tabId","refsources");
	context.put ("referenceSources", hsession.createQuery("from ReferenceSource order by name").list());
	templates.merge ("/refsource-list.vm",context,out);
%>
