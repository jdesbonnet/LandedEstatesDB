<%@include file="_header.jsp"%><%
	
	Long id = null;
	try {
		id = new Long(request.getParameter("id"));
	} catch (Exception e) {
	}
	
	Family family;
	if (id == null) {
		family = new Family();
	} else {
		family = (Family)em.find(Family.class,id);
	}
	
	context.put ("tabId","families");
	context.put ("family", family);

	context.put ("estates",DB.getInstance().getEstatesByFamily(em,family));

	context.put ("pageTitle", family.getName());
	
	templates.merge ("/family-show.vm",context,out);
%>
