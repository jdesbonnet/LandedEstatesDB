<%

	context.put ("tabId","employeeRecords");
	
	List<EmployeeRecord> list; 
	if (request.getParameter("tag")!=null) {
		// Filter by tag name
		String tagName = request.getParameter("tag");
		list = em.createQuery("from EmployeeRecord as er "
					+ " inner join fetch er.tags as tag where tag.name=:tagName")
					.setParameter("tagName",tagName)
					.getResultList();
		 context.put ("heading", "Employee Records filtered by tag '" + tagName + "'");
	} else if (request.getParameter("tag_id") != null) {
		// Filter by tag id
		Long tagId = new Long(request.getParameter("tag_id"));
		Tag tag = em.find(Tag.class, tagId);
		 list = em.createQuery("from EmployeeRecord as er "
					+ " inner join fetch er.tags as tag where tag.id=:tagId"
					//+ " where er.tag.id=:tagId"
					)
					.setParameter("tagId",tag.getId())
					.getResultList();
		 context.put ("heading", "Employee Records filtered by tag '" + tag.getName() + "'");
	} else {
		// List all records
		list = em
			.createQuery("from EmployeeRecord as er order by er.id")
			.getResultList();
	}	
	
	context.put("employeeRecords",list);

	 
	templates.merge ("/employee-record-list.vm",context,out);
%>
