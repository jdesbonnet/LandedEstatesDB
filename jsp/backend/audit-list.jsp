<%@include file="_header.jsp"%><%

	String className = request.getParameter("class_name");
	Long entityId = new Long(request.getParameter("entity_id"));
	
	List<AuditRecord> list = em
			.createQuery("from AuditRecord where entityId=:entityId and className=:className order by timestamp")
			.setParameter("entityId", entityId)
			.setParameter("className", className)
			.getResultList();
	
	context.put ("auditRecords", list);

	templates.merge ("/backend/audit-list.vm",context,out);
	
%>
