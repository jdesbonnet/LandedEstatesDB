<%
	
	List<User> users = em.createQuery("from User order by lastLogin desc")
	.getResultList();

	context.put("users",users);

	
	context.put("pageId","./user-list");
	context.put("showSideCol","false");
	templates.merge ("/backend/master.vm",context,out);
%>
