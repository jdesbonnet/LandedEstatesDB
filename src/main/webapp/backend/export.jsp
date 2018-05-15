<%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}

response.setContentType("text/xml");
DBExport.export2(response.getOutputStream());
//db.exportDb(out);
%>