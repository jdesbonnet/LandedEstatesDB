<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}
	
	Long employeeRecordId = new Long(request.getParameter("id"));
	EmployeeRecord employeeRecord = (EmployeeRecord)em.find(EmployeeRecord.class, employeeRecordId);
		
	context.put ("tabId","employeeRecords");
	context.put ("employeeRecord", employeeRecord );
	context.put ("house", employeeRecord.getHouse() );

	templates.merge ("/backend/employee-record-edit.vm",context,out);%>
