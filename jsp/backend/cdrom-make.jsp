<%@include file="_header.jsp"%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	CDROM cdromMaker = new CDROM();
	cdromMaker.makeCdRom();
%>
done.
