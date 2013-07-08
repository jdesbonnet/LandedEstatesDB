<%@include file="_header.jsp"%><%
	
	context.put ("tabId","images");
	
	
	Long id = new Long (request.getParameter("id"));
	context.put ("image", hsession.load(imageEntityName,id));
	
	String query;
	List list;
	
	//	 Identify associated property
	query = "from Property where images.id=" + id;
	list = hsession.createQuery(query).list();
	if (list.size() == 0) {
		throw new ServletException ("associated property for image " + id + " not found");
	}
	Property property = (Property)list.get(0);
	context.put ("property",property);
	
	
	// Find Estate or Estates associated with this Property record
	query = "from Estate where houses.id=" + property.getId();
	list = hsession.createQuery(query).list();
	if (list.size() == 0) {
		throw new ServletException ("could not find associated Estate record to image " + id);
	}
	if (list.size() == 1) {
		context.put ("estate",list.get(0));
	}
	context.put ("estates", list);
	
	templates.merge ("/backend/image-show.vm",context,out);
%>