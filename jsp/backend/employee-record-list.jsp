<%@include file="_header.jsp"%><%

	context.put ("tabId","employeeRecords");
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);

	if ("_all".equals(letter)) {
		List<EmployeeRecord> employeeRecords = em
			.createQuery("from EmployeeRecord as er order by er.id")
			.getResultList();
		context.put ("employeeRecords",employeeRecords);
	}	
	
	if (letter != null && letter.length() == 1) {
		List<EmployeeRecord> employeeRecords = em
				.createQuery("from EmployeeRecord as er where er.name like :letter order by er.id")
				.setParameter("letter",letter + "%")
				.getResultList();
		context.put ("employeeRecords",employeeRecords);
	}
	
	templates.merge ("/backend/employee-record-list.vm",context,out);
%>
