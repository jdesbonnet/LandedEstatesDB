<%@include file="_header.jsp"%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Estate estate = null;
	try {
		Long estateId = new Long(request.getParameter("estate_id"));
		estate = (Estate)hsession.load(Estate.class,estateId);
	} catch (Exception e) {
	}
	if (estate == null) {
		throw new ServletException ("no estate_id or estate not found");
	}
	
	
	// TODO: check to make sure no references
	//db.deleteEstate(estate);
	hsession.delete(estate);
	
	String referer = request.getHeader("Referer");
	if (referer != null && referer.length()>0) {
		response.sendRedirect(referer);
	} else {
		response.sendRedirect("estate-list.jsp");
	}
%>
