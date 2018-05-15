<%@page import="javax.xml.bind.Marshaller"%>
<%@page import="javax.xml.bind.JAXBContext"%>
<%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	// estate_id is mandatory
	Long estateId = new Long(request.getParameter("estate_id"));
	Estate estate = em.find(Estate.class, estateId);
	
	// employee_record_id is present if editing an existing record. If absent
	// then create a new record.
	EmployeeRecord employeeRecord;
	if (request.getParameter("employee_record_id") == null) {
		employeeRecord = new EmployeeRecord();
		employeeRecord.setEstate(estate);
		em.persist(employeeRecord);
	} else {
		Long employeeRecordId = new Long(request.getParameter("employee_record_id"));
		employeeRecord = em.find(EmployeeRecord.class, employeeRecordId);
	}
	
	// Attempt to parse date and if successful set it. Else silently ignore.
	/*
	try {
		java.text.SimpleDateFormat erdf = new java.text.SimpleDateFormat("dd MMM yyyy");
		java.util.Date d = erdf.parse(request.getParameter("date"));
		employeeRecord.setDate(d);
	} catch (Exception e) {
		// ignore
	}
	*/
	employeeRecord.setDateExpression(request.getParameter("date_expr"));
	
	employeeRecord.setDescription(request.getParameter("description"));
	
	// Look for references to delete
	for (String p : request.getParameterMap().keySet()) {
		if (p.startsWith("delete_ref_")) {
			Long refId = new Long(p.substring("delete_ref_".length()));
			Reference ref = em.find(Reference.class, refId);
			employeeRecord.getReferences().remove(ref);
			em.remove(ref);
		}
	}
	
	
	try {
		Long tagId = new Long(request.getParameter("new_tag_id"));
		Tag tag = em.find(Tag.class,tagId);
		employeeRecord.getTags().add(tag);
	} catch (Exception e) {
		String tagName = request.getParameter("new_tag");
		if (tagName != null && tagName.trim().length()>0) {
			Tag tag = new Tag();
			tag.setName(tagName);
			em.persist(tag);
			employeeRecord.getTags().add(tag);
		}
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
		if (p.startsWith("remove_tag_")) {
			Long tagId = new Long(p.substring("remove_tag_".length()));
			Tag tag = em.find(Tag.class, tagId);
			employeeRecord.removeTag(tag);
		}
	}
	
	AuditUtil.writeAuditRecord(em,user,employeeRecord);
	
	if (request.getParameter("_submit_save")!=null) {
		response.sendRedirect("estate-show.jsp?id=" + estate.getId());
	} else if (request.getParameter("_submit_new_reference")!=null) {
		response.sendRedirect("employee-record-reference-add.jsp?id=" + employeeRecord.getId());
	} else {
		response.sendRedirect("employee-record-edit.jsp?id=" + employeeRecord.getId());
	}
	
%>
