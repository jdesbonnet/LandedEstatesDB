<%
	long time = -System.currentTimeMillis();
			
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);
	
	// Recently modified records
	Date since = new Date (System.currentTimeMillis() - 7*24*3600*1000);
	List<Estate> recent = em.createQuery("from Estate where lastModified>:since order by lastModified desc")
			.setParameter("since", since)
			.setMaxResults(20)
			.getResultList();
	log.info("found " + recent.size()  + " recent estate records");
	
	context.put ("recent", recent);
	
	context.put("pageId","./estate-list");
	context.put("showSideCol","true");
	
	//templates.merge ("/backend/estate-list.vm",context,out);
	templates.merge ("/backend/master.vm",context,out);

%>
