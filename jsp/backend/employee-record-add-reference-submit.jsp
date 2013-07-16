<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}
	
	Long employeeRecordId = new Long(request.getParameter("employee_record_id"));
	EmployeeRecord employeeRecord = (EmployeeRecord)em.find(EmployeeRecord.class, employeeRecordId);
	
	Long refSourceId = new Long(request.getParameter("refsource_id"));
	ReferenceSource refSource = (ReferenceSource)em.find(ReferenceSource.class, refSourceId);
	
	// Create and persist new reference object
	Reference ref = new Reference();
	ref.setDescription(request.getParameter("description"));
	ref.setSource(refSource);
	em.persist(ref);
	
	// Add it to employee record
	employeeRecord.getReferences().add(ref);
	
	// Go back to add another reference or return to employee record edit screen depending on button clicked
	if (request.getParameter("_submit_exit")!=null) {
		response.sendRedirect("employee-record-edit.jsp?id=" + employeeRecord.getId());
	} else {
		response.sendRedirect("employee-record-add-reference.jsp?id=" + employeeRecord.getId());
	}

%>
