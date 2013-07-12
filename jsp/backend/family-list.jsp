<%@include file="_header.jsp"%><%

	long time = -System.currentTimeMillis();

	context.put ("tabId","families");
	
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
	
	templates.merge ("/backend/family-list.vm",context,out);
	
	time += System.currentTimeMillis();
	System.err.println ("family-list.jsp: " + time + "ms");
%>
