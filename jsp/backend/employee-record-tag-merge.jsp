<%@include file="_header.jsp"%><%

	// Merge tags by removing records with fromTag and replacing with intoTag.

	Tag fromTag = em.find(Tag.class,new Long(request.getParameter("from_tag_id")));
	Tag intoTag = em.find(Tag.class,new Long(request.getParameter("into_tag_id")));


	List<EmployeeRecord> fromList = em.createQuery("from EmployeeRecord as er "
					+ " inner join fetch er.tags as tag where tag.id=:tagId")
					.setParameter("tagId",fromTag.getId())
					.getResultList();
	

	for (EmployeeRecord er : fromList) {
		er.addTag(intoTag);
		er.removeTag(fromTag);
	}
%>

Done.
