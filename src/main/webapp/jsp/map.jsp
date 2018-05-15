<%
	
	//context.put ("onLoadFunction", "drawMapPageMap");
	context.put ("GOOGLE_MAP_ENABLE","true");

	out.clear();
	templates.merge ("/map.vm",context,out);
%>
