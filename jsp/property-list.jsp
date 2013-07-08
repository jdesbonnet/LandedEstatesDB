<%@include file="_header.jsp"%><%

	long time = -System.currentTimeMillis();

	context.put ("tabId","houses");
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);

	if ("_all".equals(letter)) {
		//context.put ("properties",hsession.createQuery("from Property where projectPhase=1 order by name").list());
		context.put ("properties",hsession.createQuery("from Property order by name").list());
	}	
	if (letter != null && letter.length() == 1) {
		//context.put ("properties",hsession.createQuery("from Property where projectPhase=1 and name like :letter order by name")
		context.put ("properties",hsession.createQuery("from Property where name like :letter order by name")
				.setString ("letter",letter+"%")
				.list());
	} 
	templates.merge ("/property-list.vm",context,out);
	
	time += System.currentTimeMillis();
	System.err.println ("property-list.jsp: " + time + "ms");
%>
