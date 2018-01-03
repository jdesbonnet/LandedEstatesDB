<%@include file="_header.jsp"%><%
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);
	
	if ("_all".equals(letter)) {
		context.put ("families",
				em.createQuery("from Family order by name")
				.getResultList());
	}	
	if (letter != null && letter.length() == 1) {
		context.put("families",
				em.createQuery("from Family where name like :letter")
				.setParameter("letter","" + letter + "%")
				.getResultList()
				);
	} 
	
	context.put("pageId","./family-list");
	templates.merge ("/backend/master.vm",context,out);
%>
