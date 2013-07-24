<%@include file="_header.jsp"%><%

	Long auditRecordId = new Long(request.getParameter("id"));
	AuditRecord auditRecord = em.find(AuditRecord.class, auditRecordId);

	response.setContentType("text/xml");
	
	out.write (auditRecord.getEntityXml());
	
%>
