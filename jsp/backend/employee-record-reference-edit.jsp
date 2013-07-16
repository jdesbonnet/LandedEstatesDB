<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}
	
	Long employeeRecordId = new Long(request.getParameter("employee_record_id"));
	EmployeeRecord employeeRecord = (EmployeeRecord)em.find(EmployeeRecord.class, employeeRecordId);
	
	Long referenceId = new Long(request.getParameter("reference_id"));
	Reference reference = em.find(Reference.class,referenceId);
	
	if ( ! employeeRecord.getReferences().contains(reference)) {
		throw new ServletException ("Reference#" + reference.getId() + " does not belong to EmployeeRecord#" + employeeRecord.getId());
	}
		
	context.put ("tabId","employeeRecords");
	context.put ("employeeRecord", employeeRecord );
	context.put ("reference", reference);
	
	templates.merge ("/backend/employee-record-reference-edit.vm",context,out);
%>
