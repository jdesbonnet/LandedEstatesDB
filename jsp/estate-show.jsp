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
	// Required:
	// id: estate record ID
	
	// Reference to this JSP for access to static methods defined above
	context.put ("jsp",this);
	
	Estate estate;
	try {
		Long id = new Long (request.getParameter("id"));
		estate = (Estate)em.find(Estate.class,id);
	} catch (NumberFormatException e) {
		throw new ServletException ("invalid id");
	}
	context.put ("estate", estate);
	
	/*
	 * Sort out reference categories into a HashMap of Lists
	 */
	
	HashMap<String,List<Reference>> referenceCategoryHash = new HashMap<String,List<Reference>>();
	
	List<Reference> problemReferences = new ArrayList<Reference>();
	context.put ("problemReferences", problemReferences);
	
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
		
		List<Reference> list = (List<Reference>)referenceCategoryHash.get(refCat.getName());
		if (list == null) {
			list = new ArrayList<Reference>();
			referenceCategoryHash.put (refCat.getName(), list);
		}
		list.add(ref);
		
	}
	
	for (String key : referenceCategoryHash.keySet() ) {
		List<Reference> list = referenceCategoryHash.get(key);
		java.util.Collections.sort(list);
	}
	
	context.put ("tabId","estates");
	
	// Order is important, so need to send String[] with the keys in the
	// correct order. Relying on order from referenceCategoryHash.keySet()
	// does not produce the correct order. Ref Mantis 94.
	context.put ("referenceCategoryNames", referenceCategoryNames);
	
	context.put ("referenceCategories",referenceCategoryHash);
	
	context.put ("pageTitle","Estate Record: " + estate.getName());
	
	
	templates.merge ("/estate-show.vm",context,out);
%>
