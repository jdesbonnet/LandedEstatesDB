<%@page import="org.apache.lucene.queryparser.classic.MultiFieldQueryParser"%>
<%@page import="org.apache.lucene.analysis.Analyzer"%>
<%@page import="org.apache.lucene.analysis.standard.StandardAnalyzer"%>
<%@page import="org.apache.lucene.util.Version"%>
<%@page import="org.hibernate.search.jpa.FullTextEntityManager"%>
<%@page import="org.hibernate.search.jpa.Search"%>
<%
	String q = request.getParameter("q");

	final String[] fields = new String[]{"description"}; // search on these fields
	FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
	Analyzer analyzer = new StandardAnalyzer();
	MultiFieldQueryParser parser = new MultiFieldQueryParser(
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