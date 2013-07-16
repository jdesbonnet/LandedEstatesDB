<%@include file="_header.jsp"%><%

	/**
	 * Query number of records matching a tag
	 * Parameters:
	 * q : Sub-string query of tag name
	 *
	 * Returns:
	 * JSON serialized array of label,value objects required for jQuery UI autocomplete widget.
	 */
	 
	 Long tagId = new Long(request.getParameter("id"));
	int nrec = em.createQuery("from EmployeeRecord as er "
			+ " inner join fetch er.tags as tag where tag.id=:id")
			.setParameter("id",tagId)
			.getResultList()
			.size();
	
	response.setContentType("application/json");
	out.write (""+nrec);
%>
