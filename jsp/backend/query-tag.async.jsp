<%@include file="_header.jsp"%><%

	/**
	 * Query tags by name for autocomplete widget.
	 */
	 
	String q = request.getParameter("q");

	List<Tag> tags = em
			.createQuery("from Tag where name like :q")
			.setParameter("q",q + "%")
			.getResultList();
	
	System.err.println ("Found " + tags.size() + " records matching query " + q);
	
	// Return a JSON array of label/value objects.
	response.setContentType("application/json");
	out.write ("[");
	boolean first = true;
	for (Tag tag : tags) {
		if (first) {
			first = false;
		} else {
			out.write (",");
		}
		out.write ("{\"label\":" + JSONUtils.quote(tag.getName()) + ",\"value\":" + tag.getId() + "}");
	}
	out.write ("]");
%>
