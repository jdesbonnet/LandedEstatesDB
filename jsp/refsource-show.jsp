<%@page import="java.util.Map"%>
<%@include file="_header.jsp"%><%
	
	Long id = null;
	if (request.getParameter("id") != null) {
		try {
			id = new Long(request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
	}
	
	long t = -System.currentTimeMillis();
	
	ReferenceSource source;
	if (id != null) {
		source = (ReferenceSource) hsession.load(
				ReferenceSource.class, id);
	} else {
		source = new ReferenceSource ();
	}

	
	context.put ("source", source);
	context.put ("categories", hsession.createQuery("from ReferenceCategory").list());
	
	List<Estate> hitList = hsession
		.createQuery("select distinct estate from Estate as estate "
		+ " join estate.references as reference "
		+ " where reference.source.id= ? "
		//+ " and  estate.projectPhase=1 "
		)
		.setLong(0,id)
		.list();

	
	t += System.currentTimeMillis();
	
	System.err.println ("queryTime=" + t + "ms");
	context.put ("tabId","refsources");
	
	context.put ("records", hitList);
	
	context.put ("pageTitle", source.getName());
	
	templates.merge ("/refsource-show.vm",context,out);
%>
