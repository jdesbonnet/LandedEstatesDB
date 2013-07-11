<%@include file="_header.jsp"%><%

	long time = -System.currentTimeMillis();
		
	context.put ("tabId","estates");
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);

	if ("_all".equals(letter)) {
		List<Estate> estates;
		if (phase == null || phase == 0) {
			estates = em.createQuery("from Estate order by name")
			//.setCacheable(true)
			.getResultList();
		} else {
			estates = em.createQuery("from Estate where projectPhase=:phase order by name")
			.setInteger("phase",phase)
			.//setCacheable(true)
			.getResultList();
		}
		context.put ("estates",estates);
	}	
	
	if (letter != null && letter.length() == 1) {

		List<Estate> estates;
		if (phase == null || phase == 0) {
			estates = hsession.createQuery("from Estate where name like :letter order by name")
			.setString("letter",letter + "%")
			.setCacheable(true)
			.list();
		} else {
			estates = hsession.createQuery("from Estate where name like :letter and projectPhase=:phase order by name")
			.setString("letter",letter + "%")
			.setInteger("phase",phase)
			.setCacheable(true)
			.list();
		}
		context.put ("estates",estates);
		
	} 
	templates.merge ("/backend/estate-list.vm",context,out);
	
	out.flush();
	
	time += System.currentTimeMillis();
	System.err.println ("estate-list.jsp: " + time + "ms");
%>
