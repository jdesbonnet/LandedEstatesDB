<%@include file="_header.jsp"%><%if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long houseId = new Long(request.getParameter("house_id"));
	House house = (House)em.find(House.class, houseId);
	
	EmployeeRecord employeeRecord;
	if (request.getParameter("employee_record_id") == null) {
		employeeRecord = new EmployeeRecord();
		employeeRecord.setHouse(house);
		em.persist(employeeRecord);
	} else {
		Long employeeRecordId = new Long(request.getParameter("employee_record_id"));
		employeeRecord = em.find(EmployeeRecord.class, employeeRecordId);
	}
	
	// Attempt to parse date and if successful set it
	try {
		java.text.SimpleDateFormat erdf = new java.text.SimpleDateFormat("dd MMM yyyy");
		java.util.Date d = erdf.parse(request.getParameter("date"));
		employeeRecord.setDate(d);
	} catch (Exception e) {
		// ignore
	}
	
	employeeRecord.setDescription(request.getParameter("description"));
	
	try {
		Long tagId = new Long(request.getParameter("new_tag_id"));
		Tag tag = em.find(Tag.class,tagId);
		employeeRecord.addTag(tag);
	} catch (Exception e) {
		// ignore
	}
	
	/*
	String newTag = request.getParameter("new_tag");
	List<Tag> tags = em.createQuery("from Tag where name=:tagName")
			.setParameter("tagName", newTag)
			.getResultList();
	if (tags.size()>0) {
		// Use existing tag
		Tag tag = tags.get(0);
		employeeRecord.addTag(tag);
	} else {
		// Create a new tag
		Tag tag = new Tag();
		tag.setName(newTag);
		em.persist(tag);
		employeeRecord.addTag(tag);
	}
	*/
	
	// Look for tags to remove
	for (String p : request.getParameterMap().keySet()) {
		if (p.startsWith("delete_tag_")) {
			Long tagId = new Long(p.substring("delete_tag_".length()));
			Tag tag = em.find(Tag.class, tagId);
			employeeRecord.removeTag(tag);
		}
	}
	
	if (request.getParameter("_submit_save")!=null) {
		response.sendRedirect("house-show.jsp?id=" + house.getId());
	} else {
		response.sendRedirect("employee-record-edit.jsp?id=" + employeeRecord.getId());
	}
	
%>
