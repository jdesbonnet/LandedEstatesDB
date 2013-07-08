<%@include file="_header.jsp"%><%
	
	context.put ("tabId","images");
	
	List images = hsession.createQuery("from " + imageEntityName).list();
	
	if ("true".equals(request.getParameter("captions"))) {
		context.put ("showCaptions","true");
	}
	
	context.put ("images",images);
	templates.merge ("/images.vm",context,out);
%>