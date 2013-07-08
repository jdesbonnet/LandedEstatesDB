<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	String estateIdStr = request.getParameter("estate_id");
	if (estateIdStr == null) {
			throw new ServletException ("no estate_id");
	}
	
	if (request.getParameter("_submit_cancel") != null) {
			response.sendRedirect ("estate-show.jsp?id="+estateIdStr);
			return;
	}
	
	Estate estate;
	try {
		Long estateId = new Long (request.getParameter("estate_id"));
		estate = (Estate)hsession.load(Estate.class, estateId);
	} catch (Exception e) {
		throw new ServletException (e.toString());
	}
	
	String referenceDescription = request.getParameter("description");
	
	if (referenceDescription != null && referenceDescription.length()>0) {
	
		Reference reference = new Reference ();
		reference.setDescription (request.getParameter("description"));
		
		Long refSourceId = DB.getIdFromAutoCompleteField(request.getParameter("refsource"));
		ReferenceSource refSource = (ReferenceSource)hsession.load(ReferenceSource.class, refSourceId);
		reference.setSource(refSource);
		
		hsession.save (reference);
		estate.getReferences().add(reference);
		hsession.save (estate);
	
		/*
		// There is more than one pull down menu for source_id
		String[] vals = request.getParameterValues("source_id");
		if (vals != null) {
			for (int i = 0; i < vals.length; i++) {
				try {
					int sourceId = Integer.parseInt(vals[i]);
					ReferenceSource source = db.getReferenceSource(sourceId);
					reference.setSource(source);
					break;
				} catch (Exception e) {
					//e.printStackTrace();
					// ignore
				}
			}
		}
		*/
		
				

		
		//db.saveReference (reference);
	
	}
	
	/*
	 * Two save buttons: one will return to reference-new.jsp,
	 * the other to estate-show.jsp
	 */
	if (request.getParameter("_submit_exit") != null) {
		response.sendRedirect("estate-show.jsp?id=" + estate.getId());
	} else {
		response.sendRedirect("reference-new.jsp?estate_id=" + estate.getId());
	}
%>
