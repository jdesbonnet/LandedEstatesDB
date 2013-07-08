<%@include file="_header.jsp"%><%

	long time = -System.currentTimeMillis();

	context.put ("tabId","families");
	
	String letter = request.getParameter("letter");
	context.put ("letter",letter);
	context.put ("alphabet",alphabet);
	
	if ("_all".equals(letter)) {
		context.put ("families",db.getFamilies(hsession));
	}	
	if (letter != null && letter.length() == 1) {
		context.put("families",db.getFamiliesByLetter(hsession,letter));
	} 
	
	templates.merge ("/backend/family-list.vm",context,out);
	
	time += System.currentTimeMillis();
	System.err.println ("family-list.jsp: " + time + "ms");
%>
