<%@include file="_header.jsp"%><%!
	public static String markingsToHtml (String s) {
		return TextUtil.markingsToHtml(s);
	}
	public static final String[] referenceCategoryNames = {
		"Archival sources",
		"Contemporary printed sources",
		"Modern printed sources"
	};
%><%
	// Reference to this JSP for access to static methods defined above
	context.put ("jsp",this);
	
	Estate estate;
	try {
		Long id = new Long (request.getParameter("id"));
		estate = (Estate)hsession.load(Estate.class,id);
	} catch (NumberFormatException e) {
		throw new ServletException ("invalid id");
	}
	context.put ("estate", estate);
	
	/*
	 * Sort out reference categories into a HashMap of Lists
	 */
	
	HashMap referenceCategoryHash = new HashMap();
	
	List problemReferences = new ArrayList();
	context.put ("problemReferences", problemReferences);
	
	Iterator iter = estate.getReferences().iterator();
	
	while (iter.hasNext()) {
		Reference ref = (Reference)iter.next();
		
	
		ReferenceCategory refCat;
		if (ref.getSource() == null || ref.getSource().getCategory() == null
		 || ref.getSource().getCategory().getName() == null) {
		 	//refCat = problemRefCat;
		 	problemReferences.add(ref);
		 	continue;
		} else {
			refCat = ref.getSource().getCategory();
		}
		
		List list = (List)referenceCategoryHash.get(refCat.getName());
		if (list == null) {
			list = new ArrayList();
			referenceCategoryHash.put (refCat.getName(), list);
		}
		list.add(ref);
		
	}
	
	// Sort the reference lists
	iter = referenceCategoryHash.keySet().iterator();
	while (iter.hasNext()) {
		String key = (String)iter.next();
		List list = (List)referenceCategoryHash.get(key);
		java.util.Collections.sort(list);
	}
	
	context.put ("tabId","estates");
	
	context.put ("referenceCategories",referenceCategoryHash);
	context.put ("referenceCategoryNames", referenceCategoryNames);
	
	
	//context.put ("revisionHistory",db.getRevisionHistory(Estate.class.getName(), estate.getId()));
	
	
	
	templates.merge ("/backend/estate-show.vm",context,out);
%>
