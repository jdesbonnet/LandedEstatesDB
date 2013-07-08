<%@include file="_header.jsp"%><%

	
	//List<Family> list = hsession.createQuery("from Family").list();
	List<Estate> estates = hsession.createQuery("from Estate").list();
	
	out.clear();
	response.setContentType("text/plain");
	

	HashMap<Family,List<Estate>> familyToEstateHash = new HashMap<Family,List<Estate>>();
	
	for (Estate estate : estates) {
		for (Family family : estate.getFamilies()) {
			List<Estate> list = familyToEstateHash.get(family);
			if (list == null) {
				list = new ArrayList<Estate>();
				familyToEstateHash.put(family,list);
			}
			list.add(estate);
		}
	}
	
	/*
	for (Estate estate : estates) {
		for (Family family : estate.getFamilies()) {
			out.write ("\"E" + estate.getId() + " "  + estate.getName() 
					+ "\" -> \"F" + family.getId() + " "+ family.getName() + "\"\n");
		}
	}
	*/
	
	for (Family family : familyToEstateHash.keySet()) {
		out.write ("\"F" + family.getId() + " " + family.getName() + "\" [color=red];\n");
	}
	for (Estate estate : estates) {
		out.write ("\"E" + estate.getId() + " "  + estate.getName() + "\" [color=green];\n");
	}
	
	for (Family family : familyToEstateHash.keySet()) {
		for (Estate estate : familyToEstateHash.get(family)) {
			out.write (
					"\"F" + family.getId() + " " + family.getName() + "\""
					+ " -> \"E" + estate.getId() + " "  + estate.getName() + "\""
					+ "\n");
		}
	}
	
%>
