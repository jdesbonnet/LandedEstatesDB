<%@page import="java.util.ArrayList"%>
<%@page import="ie.wombat.imagetable.ImageDB"%>
<%@page import="ie.wombat.imagetable.Image"%>
<%
	if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}

/*
	java.util.List<Image> allImages = em.createQuery ("from " + imageEntityName).getResultList();
	ArrayList allImageIds = new ArrayList(allImages.size());
	java.util.Iterator iter = allImages.iterator();
	while (iter.hasNext()) {
		Image image = (Image)iter.next();
		allImageIds.add(image.getId());
	}
	
	tx.commit();
	HibernateUtilOld.closeSession();
	
	iter = allImageIds.iterator();
	
	while (iter.hasNext()) {
		
	hsession = HibernateUtilOld.currentSession();
	tx = hsession.beginTransaction();
	
	Long id = (Long)iter.next();
	//Image image = (Image)iter.next();
	Image image = (Image)hsession.load(imageEntityName,id);
	
	out.write ("processing image #" + image.getId()
			+ image.getCaption()
			+ "...");
	out.flush();
	
	//tx = hsession.beginTransaction();
	ImageDB.getInstance(imageEntityName).rescaleImage(hsession, image);
	tx.commit();
	HibernateUtilOld.closeSession();
	out.write ("done.<br>\n");
	out.flush();
	}
*/
%>
all done!