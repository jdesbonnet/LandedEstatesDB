<%@page import="java.util.Map"%>
<%@include file="_header.jsp"%><%
	
	Long id = null;
	if (request.getParameter("id") != null) {
		try {
			id = new Long(request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
	}
	
	ReferenceSource source;
	if (id != null) {
		source = (ReferenceSource) hsession.load(
				ReferenceSource.class, id);
	} else {
		source = new ReferenceSource ();
	}

	System.err.println ("source=" + source);
	
	context.put ("source", source);
	context.put ("categories", hsession.createQuery("from ReferenceCategory").list());
	
	
	List<Estate> hitList = hsession
	.createQuery("select distinct estate from Estate as estate "
	+ " join estate.references as reference "
	+ " where reference.source.id= ? ")
	.setLong(0,id)
	.list();

	
	context.put ("tabId","refsources");
	
	context.put ("records", hitList);
	
	templates.merge ("/backend/refsource-show.vm",context,out);
%>
