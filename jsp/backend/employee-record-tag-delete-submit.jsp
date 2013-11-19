<%@include file="_header.jsp"%><%

	Long tagId = new Long(request.getParameter("tag_id"));
	Tag tag = em.find(Tag.class,tagId);


	List<EmployeeRecord> list = em.createQuery("from EmployeeRecord as er "
					+ " inner join fetch er.tags as tag where tag.id=:tagId")
					.setParameter("tagId",tag.getId())
					.getResultList();
	
	if (list.size()>0) {
		throw new ServletException ("Cannot delete tag when it is used in records. Remove tag from records first.");
	}

	em.remove(tag);

	response.sendRedirect("employee-record-tag-list.jsp");

%>
