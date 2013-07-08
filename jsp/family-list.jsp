<%@include file="_header.jsp"%><%

	long time = -System.currentTimeMillis();

	context.put ("tabId","families");
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);
	
	if ("_all".equals(letter)) {
		context.put ("families",hsession
				//.createQuery("from Family where projectPhase=1 order by name")
				.createQuery("from Family order by name")
				.setCacheable(true)
				.list());
	}	
	if (letter != null && letter.length() == 1) {
		context.put("families",hsession
				//.createQuery("from Family where projectPhase=1 and name like :letter order by name")
				.createQuery("from Family where name like :letter order by name")
				.setString ("letter",letter+"%")
				.setCacheable(true)
				.list());
	} 
	
	templates.merge ("/family-list.vm",context,out);
	
	time += System.currentTimeMillis();
	System.err.println ("family-list.jsp: " + time + "ms");
%>
