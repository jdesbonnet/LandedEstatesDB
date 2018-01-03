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
	
	Long estateId = new Long(request.getParameter("id"));
	Estate estate = (Estate)em.find(Estate.class, estateId);
	context.put ("estate", estate);
	
	/*
	 * Sort out reference categories into a HashMap of Lists
	 */
	
	HashMap<String,List<Reference>> referenceCategoryHash = new HashMap<String,List<Reference>>();
	
	List<Reference> problemReferences = new ArrayList<Reference>();
	context.put ("problemReferences", problemReferences);
	
	// Group references by reference category
	for (Reference ref : estate.getReferences()) {		
	
		ReferenceCategory refCat;
		if (ref.getSource() == null || ref.getSource().getCategory() == null
		 || ref.getSource().getCategory().getName() == null) {
		 	//refCat = problemRefCat;
		 	problemReferences.add(ref);
		 	continue;
		} else {
			refCat = ref.getSource().getCategory();
		}
		
		List<Reference> list = referenceCategoryHash.get(refCat.getName());
		if (list == null) {
			list = new ArrayList<Reference>();
			referenceCategoryHash.put (refCat.getName(), list);
		}
		list.add(ref);
		
	}
	
	// Sort the reference lists
	for (List<Reference> list : referenceCategoryHash.values()) {
		java.util.Collections.sort(list);
	}
	
	
	context.put ("referenceCategories",referenceCategoryHash);
	context.put ("referenceCategoryNames", referenceCategoryNames);
	
	
	//context.put ("revisionHistory",db.getRevisionHistory(Estate.class.getName(), estate.getId()));
	
	context.put("pageId","./estate-show");
	templates.merge ("/backend/master.vm",context,out);
%>
