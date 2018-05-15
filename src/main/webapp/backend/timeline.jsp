<%@page import="java.util.Collections"%>
<%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}

// Add
HashMap<String,List<Object>> yearMap = new HashMap<>();

List<House> houses = em.createQuery("from House order by name").getResultList();
for (House house : houses) {
	Set<String> years = TimelineAnalysis.parseTextForDate(house.getDescription());
	for (String year : years) {
		if (!yearMap.containsKey(year)) {
			yearMap.put(year,new ArrayList<>());
		}
		yearMap.get(year).add(house);
	}
}

List<Estate> estates = em.createQuery("from Estate order by name").getResultList();
for (Estate estate : estates) {
	Set<String> years = TimelineAnalysis.parseTextForDate(estate.getDescription());
	for (String year : years) {
		if (!yearMap.containsKey(year)) {
			yearMap.put(year,new ArrayList<>());
		}
		yearMap.get(year).add(estate);
	}
}

// TODO: sort properly

List<String> years = new ArrayList<>(yearMap.keySet());
Collections.sort(years);
context.put("years",years);
context.put("yearMap",yearMap);
context.put("pageId", "./timeline");
templates.merge ("/backend/master.vm",context,out);


%>
done.