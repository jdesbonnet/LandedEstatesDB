<%@page import="org.apache.lucene.analysis.standard.StandardAnalyzer"%>
<%@page import="org.apache.lucene.util.Version"%>
<%@page import="org.apache.lucene.queryParser.MultiFieldQueryParser"%>
<%@page import="org.hibernate.search.jpa.FullTextEntityManager"%>
<%@page import="org.hibernate.search.jpa.Search"%>
<%@include file="_header.jsp"%><%
	String q = request.getParameter("q");

	final String[] fields = new String[]{"description"}; // search on these fields
	FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
	MultiFieldQueryParser parser = new MultiFieldQueryParser(
		Version.LUCENE_31, 
		fields, 
		new StandardAnalyzer(Version.LUCENE_31) 
	);
	
	org.apache.lucene.search.Query query = parser.parse(q);

	context.put ("results",fullTextEntityManager.createFullTextQuery(query).getResultList());
	templates.merge ("/backend/search-results.vm",context,out);
%>
