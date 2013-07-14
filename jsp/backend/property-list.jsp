<%@include file="_header.jsp"%><%long time = -System.currentTimeMillis();

	context.put ("tabId","houses");
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);

	if ("_all".equals(letter)) {
		List<House> properties;
		if (phase == null || phase == 0) {
			properties = em.createQuery("from Property as p order by p.name")
			.getResultList();
		} else {
			properties = em.createQuery("from Property as p where p.projectPhase=:phase order by p.name")
			.setParameter("phase",phase)
			.getResultList();
		}
		context.put ("properties",properties);
	}	
	
	if (letter != null && letter.length() == 1) {
		List<House> properties;
		if (phase == null || phase == 0) {
			properties = em.createQuery("from Property as p where p.name like :letter order by p.name")
			.setParameter("letter",letter + "%")
			.getResultList();
		} else {
			properties = em.createQuery("from Property as p where p.name like :letter and p.projectPhase=:phase order by p.name")
			.setParameter("letter",letter + "%")
			.setParameter("phase",phase)
			.getResultList();
		}
		context.put ("properties",properties);	
	} 
	templates.merge ("/backend/property-list.vm",context,out);
	
	time += System.currentTimeMillis();
	System.err.println ("property-list.jsp: " + time + "ms");%>
