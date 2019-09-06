<%


	context.put("numberOfEstateRecords",em.createQuery("select COUNT(e) from Estate as e").getSingleResult());
	context.put("numberOfHouseRecords",em.createQuery("select COUNT(h) from House as h").getSingleResult());
	context.put("numberOfFamilyRecords",em.createQuery("select COUNT(f) from Family as f").getSingleResult());

	context.put("pageId","./index");
	templates.merge ("/backend/master.vm",context,out);
%>
