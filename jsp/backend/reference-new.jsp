<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	try {
		Long estateId = new Long (request.getParameter("estate_id"));
		context.put ("estate", hsession.load(Estate.class, estateId) );
	} catch (Exception e) {
		throw new ServletException ("error: no estate_id");
	}
	
	HashMap categoryHash = new HashMap();
	ArrayList miscList = new ArrayList();
	categoryHash.put ("_misc_", miscList);
	
	List referenceSources = hsession.createQuery("from ReferenceSource order by name").list();
	for (int i = 0; i < referenceSources.size(); i++) {
		ReferenceSource refSource = (ReferenceSource)referenceSources.get(i);
		ReferenceCategory refCat = refSource.getCategory();
		String categoryName;
		if (refCat == null) {
			categoryName = "_misc_";
		} else {
			categoryName = refCat.getName();
		}
		ArrayList list = (ArrayList)categoryHash.get(categoryName);
		if (list == null) {
			list = new ArrayList();
			categoryHash.put (categoryName, list);
		}
		list.add(refSource);
	}
	
	context.put ("tabId","estates");
	context.put ("categoryHash", categoryHash);
	
	templates.merge ("/backend/reference-new.vm",context,out);
%>
