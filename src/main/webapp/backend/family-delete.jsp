<%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Family family = null;
	try {
		Long id = new Long(request.getParameter("id"));
		family = (Family)em.find(Family.class,id);
	} catch (Exception e) {
		throw new Exception (e.toString());
	}
	
	List<Estate> estates = em.createQuery("from Estate where families.id=" + family.getId()).getResultList();
	
	if (estates.size() > 0) {
		StringBuffer errorText = new StringBuffer();
		
		errorText.append ("Cannot delete family. ");
		errorText.append ("Referenced in the following estates:\n<ul>\n");
		Iterator iter = estates.iterator();
		while (iter.hasNext()) {
			Estate estate = (Estate)iter.next();
			errorText.append ("<li> " + estate.getName() 
					+ " (Estate #" +estate.getId() + ")</li>\n");
		}
		errorText.append ("</ul>");
		throw new ServletException (errorText.toString());
	}
	
	em.remove(family);
	
	//response.sendRedirect("family-list.jsp");
	String referer = request.getHeader("Referer");
	if (referer != null) {
		response.sendRedirect(referer);
	} else {
		response.sendRedirect("family-list.jsp");
	}
%>
