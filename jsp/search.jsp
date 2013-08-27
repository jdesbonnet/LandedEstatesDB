<%@page import="org.apache.lucene.analysis.Analyzer"%>
<%@page import="org.apache.lucene.analysis.standard.StandardAnalyzer"%>
<%@page import="org.apache.lucene.util.Version"%>
<%@page import="org.apache.lucene.queryParser.MultiFieldQueryParser"%>
<%@page import="org.hibernate.search.jpa.FullTextEntityManager"%>
<%@page import="org.hibernate.search.jpa.Search"%>
<%@include file="_header.jsp"%><%
	String q = request.getParameter("q");

	final String[] fields = new String[]{"description"}; // search on these fields
	FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
	MultiFieldQueryParser parser = new MultiFieldQueryParser(
		Version.LUCENE_31, 
		fields, 
		analyzer
	);
	
	org.apache.lucene.search.Query query = parser.parse(q);

	
	/*
	List results = fullTextEntityManager.createFullTextQuery(query).getResultList();
	SearchResultFormatter formatter = new SearchResultFormatter(analyzer, query, "userDetails");
	context.put ("formatter",formatter);
	*/
	
	
	context.put ("results",fullTextEntityManager.createFullTextQuery(query).getResultList());
	templates.merge ("/search-results.vm",context,out);
%>