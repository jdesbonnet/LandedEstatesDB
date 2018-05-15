<%!
	public static String markingsToHtml (String s) {
		return TextUtil.markingsToHtml(s);
	}
%><%

	
	context.put ("pageTitle","Record not available");
	
	
	templates.merge ("/not-available.vm",context,out);
%>
