<%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


db.makeIndex(em);

%>
done.