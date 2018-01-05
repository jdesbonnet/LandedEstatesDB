<%@page import="ie.wombat.landedestates.staticsite.StaticSite"%>
<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}


	StaticSite staticSiteMaker = new StaticSite();
	staticSiteMaker.makeStaticSite();
%>
done.
