<%@include file="_header.jsp"%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}

	int estate_id = 0;
	if (request.getParameter("estate_id") != null) {
		try {
			estate_id = Integer.parseInt(request.getParameter("estate_id"));
		} catch (NumberFormatException e) {
		}
	}
	
	if (estate_id == 0) {
		out.println ("error: no estate_id");
		return;
	}
	
	int reference_id = 0;
		if (request.getParameter("reference_id") != null) {
		try {
			reference_id = Integer.parseInt(request.getParameter("reference_id"));
		} catch (NumberFormatException e) {
		}
	}
	
	if (reference_id == 0) {
		out.println ("error: no reference_id");
		return;
	}
	
	db.removeReferenceFromEstate (estate_id, reference_id);
	response.sendRedirect ("estate-edit.jsp?id="+estate_id);
%>
