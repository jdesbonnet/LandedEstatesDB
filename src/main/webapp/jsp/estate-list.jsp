<%

	long time = -System.currentTimeMillis();
		
	context.put ("tabId","estates");
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);

	List<Estate> estates=null;
	
	if ("_all".equals(letter)) {
		//context.put ("estates",hsession.createQuery("from Estate where projectPhase=1 order by name").list());
		//context.put ("estates",em.createQuery("from Estate order by name").getResultList());
		estates = em.createQuery("from Estate order by name").getResultList();
	}	
	if (letter != null && letter.length() == 1) {
		//context.put ("estates",hsession.createQuery("from Estate where name like :letter and projectPhase=1 order by name")
		//context.put ("estates",em.createQuery("from Estate where name like :letter order by name")
		//		.setParameter("letter", letter + "%")
		//		.getResultList());
		
		estates = em.createQuery("from Estate where name like :letter order by name")
				.setParameter("letter", letter + "%")
				.getResultList();
	}
	
	
	if (estates != null) {
		context.put ("estates",estates);
	}
	
	templates.merge ("/estate-list.vm",context,out);
	
	out.flush();
	
	time += System.currentTimeMillis();
	System.err.println ("estate-list.jsp: " + time + "ms");
%>
