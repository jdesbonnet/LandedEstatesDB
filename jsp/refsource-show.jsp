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
		source = (ReferenceSource)em.find(
				ReferenceSource.class, id);
	} else {
		source = new ReferenceSource ();
	}

	
	context.put ("source", source);
	context.put ("categories", em.createQuery("from ReferenceCategory").getResultList());
	
	
	List<Estate> hitList = em
		.createQuery("select distinct estate from Estate as estate "
		+ " join estate.references as reference "
		+ " where reference.source.id= :refSourceId "
		)
		.setParameter("refSourceId",id)
		.getResultList();
	
	//List<Estate>hitList = new ArrayList<Estate>();
	
	t += System.currentTimeMillis();
	
	System.err.println ("queryTime=" + t + "ms");
	context.put ("tabId","refsources");
	
	context.put ("records", hitList);
	
	context.put ("pageTitle", source.getName());
	
	templates.merge ("/refsource-show.vm",context,out);
%>
