<%
	
	context.put ("tabId","images");
	
	List<Image> images = em.createQuery("from " + imageEntityName)
			.getResultList();
	
	if ("true".equals(request.getParameter("captions"))) {
		context.put ("showCaptions","true");
	}
	
	context.put ("images",images);
	templates.merge ("/images.vm",context,out);
%>