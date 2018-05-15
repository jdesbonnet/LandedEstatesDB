<%

	/**
	 * Query tags by name for jQuery UI autocomplete widget.
	 * Parameters:
	 * q : Sub-string query of tag name
	 *
	 * Returns:
	 * JSON serialized array of label,value objects required for jQuery UI autocomplete widget.
	 */
	 
	String q = request.getParameter("q");

	List<Tag> tags = em
			.createQuery("from Tag where name like :q")
			.setParameter("q", "%" + q + "%")
			.getResultList();
		
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
