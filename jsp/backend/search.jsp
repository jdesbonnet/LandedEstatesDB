<%@page import="org.apache.lucene.analysis.Analyzer"%>
<%@page import="org.apache.lucene.analysis.standard.StandardAnalyzer"%>
<%@page import="org.apache.lucene.util.Version"%>
<%@page import="org.apache.lucene.queryParser.MultiFieldQueryParser"%>
<%@page import="org.hibernate.search.jpa.FullTextEntityManager"%>
<%@page import="org.hibernate.search.jpa.Search"%>
<%@include file="_header.jsp"%><%
	String q = request.getParameter("q");
	context.put("q",XSSClean.clean(q));

	final String[] fields = new String[]{"name","description"}; // search on these fields
	FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
	MultiFieldQueryParser parser = new MultiFieldQueryParser(
		Version.LUCENE_31, 
		fields, 
		analyzer
	);
	
	org.apache.lucene.search.Query query = parser.parse(q);
	
	List<Object> results = fullTextEntityManager.createFullTextQuery(query).getResultList();
	
	if (request.getParameter("filter")!=null) {
		String entity = XSSClean.clean(request.getParameter("filter").toLowerCase());
		List<Object> filteredResults = new ArrayList<>();
		for (Object o : results) {
			if (o.getClass().getName().toLowerCase().contains(entity)) {
				filteredResults.add(o);
			}
		}
		results = filteredResults;
		context.put ("filter",entity);
	}
	
	context.put ("results",results);
	
	//SearchResultFormatter formatter = new SearchResultFormatter(analyzer, query, "userDetails");
	//context.put ("formatter",formatter);

	context.put ("pageId","./search-results");
	templates.merge ("/backend/master.vm",context,out);
%>
