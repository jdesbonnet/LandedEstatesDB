<%
	context.put("numberOfEstateRecords",em.createQuery("select COUNT(e) from Estate as e").getSingleResult());
	context.put("numberOfHouseRecords",em.createQuery("select COUNT(h) from House as h").getSingleResult());
	context.put("numberOfFamilyRecords",em.createQuery("select COUNT(f) from Family as f").getSingleResult());
	context.put("numberOfImageRecords",em.createQuery("select COUNT(f) from Image as f").getSingleResult());

	
	Date recent = new Date(System.currentTimeMillis() - 60*24*3600000L);
	List<House> recentHouses = em.createQuery("from House where lastModified>:recent order by lastModified")
	.setParameter("recent", recent)
	.setMaxResults(10)
	.getResultList();
	context.put("recentHouses", recentHouses);
	
	List<House> recentEstates = em.createQuery("from Estate where lastModified>:recent order by lastModified")
	.setParameter("recent", recent)
	.setMaxResults(10)
	.getResultList();
	context.put("recentEstates", recentEstates);
	
	context.put("pageId","./index");
	templates.merge ("/backend/master.vm",context,out);
%>
