<%@include file="_header.jsp"%><%

	context.put ("tabId","employeeRecords");
		
	if (request.getParameter("tag")!=null) {
		 List<EmployeeRecord> list = em.createQuery("from EmployeeRecord as er "
					+ " inner join fetch er.tags as tag where tag.name=:tagName")
					.setParameter("tagName",request.getParameter("tag"))
					.getResultList();
		 context.put("employeeRecords",list);
	} else {
		List<EmployeeRecord> list = em
			.createQuery("from EmployeeRecord as er order by er.id")
			.getResultList();
		context.put ("employeeRecords",list);
	}	
	
	templates.merge ("/backend/employee-record-list.vm",context,out);
%>
