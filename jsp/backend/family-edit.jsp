<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	context.put ("tabId","families");

	Long id = null;
	if (request.getParameter("id") != null) {
		try {
			id = new Long (request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
	}
	
	Long familyId = new Long(request.getParameter("id"));
	Family family = (Family)em.find(Family.class,id);
	
	if (family == null) {
		family = new Family ();
	}

	context.put ("family", family);
	
	String referer = request.getHeader("Referer");
	if (referer != null) {
		context.put ("next",referer);
	}
	
	context.put("pageId","./family-edit");
	templates.merge ("/backend/master.vm",context,out);
%>
