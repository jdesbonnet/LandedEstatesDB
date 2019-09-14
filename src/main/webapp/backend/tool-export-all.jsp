<%@page import="java.io.File"%>
<%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	File dataDir = new File("/var/tmp/ledb_export");
	Export export = new Export();
	export.setExportRoot(dataDir);
	
	export.exportAll(em);
%>
done.
