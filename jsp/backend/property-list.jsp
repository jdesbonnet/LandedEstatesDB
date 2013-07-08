<%@include file="_header.jsp"%><%

	long time = -System.currentTimeMillis();

	context.put ("tabId","houses");
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);

	if ("_all".equals(letter)) {
		List<Property> properties;
		if (phase == null || phase == 0) {
			properties = hsession.createQuery("from Property as p order by p.name")
			.setCacheable(true)
			.list();
		} else {
			properties = hsession.createQuery("from Property as p where p.projectPhase=:phase order by p.name")
			.setInteger("phase",phase)
			.setCacheable(true)
			.list();
		}
		context.put ("properties",properties);
	}	
	
	if (letter != null && letter.length() == 1) {
		List<Property> properties;
		if (phase == null || phase == 0) {
			properties = hsession.createQuery("from Property as p where p.name like :letter order by p.name")
			.setString("letter",letter + "%")
			.setCacheable(true)
			.list();
		} else {
			properties = hsession.createQuery("from Property as p where p.name like :letter and p.projectPhase=:phase order by p.name")
			.setString("letter",letter + "%")
			.setInteger("phase",phase)
			.setCacheable(true)
			.list();
		}
		context.put ("properties",properties);	
	} 
	templates.merge ("/backend/property-list.vm",context,out);
	
	time += System.currentTimeMillis();
	System.err.println ("property-list.jsp: " + time + "ms");
%>
