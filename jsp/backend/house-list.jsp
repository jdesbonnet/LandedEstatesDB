<%@include file="_header.jsp"%><%long time = -System.currentTimeMillis();

	context.put ("tabId","houses");
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);

	if ("_all".equals(letter)) {
		List<House> houses;
		if (phase == null || phase == 0) {
			houses = em.createQuery("from House as h order by h.name")
			.getResultList();
		} else {
			houses = em.createQuery("from House as h where h.projectPhase=:phase order by h.name")
			.setParameter("phase",phase)
			.getResultList();
		}
		context.put ("houses",houses);
	}	
	
	if (letter != null && letter.length() == 1) {
		List<House> houses;
		if (phase == null || phase == 0) {
			houses = em.createQuery("from House as h where h.name like :letter order by h.name")
			.setParameter("letter",letter + "%")
			.getResultList();
		} else {
			houses = em.createQuery("from House as h where h.name like :letter and h.projectPhase=:phase order by h.name")
			.setParameter("letter",letter + "%")
			.setParameter("phase",phase)
			.getResultList();
		}
		context.put ("houses",houses);	
	} 
	templates.merge ("/backend/house-list.vm",context,out);
	
	time += System.currentTimeMillis();
	System.err.println ("house-list.jsp: " + time + "ms");%>
