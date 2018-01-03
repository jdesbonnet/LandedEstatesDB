<%@include file="_header.jsp"%><%
	
	context.put ("tabId","images");
	
	List<Image> images = em.createQuery("from Image").getResultList();
	
	if ("true".equals(request.getParameter("captions"))) {
		context.put ("showCaptions","true");
	}
	
	
	context.put ("images",images);
	context.put("pageId","./images");
	templates.merge ("/backend/master.vm",context,out);
%>