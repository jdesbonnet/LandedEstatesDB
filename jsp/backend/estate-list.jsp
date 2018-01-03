<%@include file="_header.jsp"%><%

	long time = -System.currentTimeMillis();
			
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);

	if ("_all".equals(letter)) {
		List<Estate> estates;
		if (phase == null || phase == 0) {
			estates = em.createQuery("from Estate order by name")
			.getResultList();
		} else {
			estates = em.createQuery("from Estate where projectPhase=:phase order by name")
			.setParameter("phase",phase)
			.getResultList();
		}
		context.put ("estates",estates);
	}	
	
	if (letter != null && letter.length() == 1) {

		List<Estate> estates;
		if (phase == null || phase == 0) {
			estates = em.createQuery("from Estate where name like :letter order by name")
			.setParameter("letter",letter + "%")
			.getResultList();
		} else {
			estates = em.createQuery("from Estate where name like :letter and projectPhase=:phase order by name")
			.setParameter("letter",letter + "%")
			.setParameter("phase",phase)
			.getResultList();
		}
		context.put ("estates",estates);
		
	} 
	
	context.put("pageId","./estate-list");
	
	//templates.merge ("/backend/estate-list.vm",context,out);
	templates.merge ("/backend/master.vm",context,out);

%>
