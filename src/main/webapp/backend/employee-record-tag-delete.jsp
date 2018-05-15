<%

	context.put ("tabId","employeeRecords");
	
	Long tagId = new Long(request.getParameter("tag_id"));
	Tag tag = em.find(Tag.class,tagId);
	context.put ("tag",tag);

	List<EmployeeRecord> list = em.createQuery("from EmployeeRecord as er "
					+ " inner join fetch er.tags as tag where tag.id=:tagId")
					.setParameter("tagId",tag.getId())
					.getResultList();
	context.put ("employeeRecords", list);
	
	templates.merge ("/backend/employee-record-tag-delete.vm",context,out);
%>
