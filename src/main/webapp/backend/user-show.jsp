<%
	
	Long id = new Long(request.getParameter("id"));
	
	User u = em.find(User.class,id);
	context.put("u",u);
	
	List<Estate> recentEstates = em.createQuery("from Estate where lastModifiedBy=:u order by lastModified")
	.setParameter("u",u)
	.getResultList();
	
	context.put("recentEstates", recentEstates);
	
	context.put("pageId","./user-show");
	context.put("showSideCol","false");
	templates.merge ("/backend/master.vm",context,out);
%>
