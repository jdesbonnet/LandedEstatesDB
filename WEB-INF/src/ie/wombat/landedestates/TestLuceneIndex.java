package ie.wombat.landedestates;

import java.io.File;

import java.util.Enumeration;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class TestLuceneIndex {

	public static void main(String[] arg) throws Exception {

		File indexDir = new File(arg[0]);
		String k = arg[1];
		String q = arg[2];

		System.err.println ("searching for " + q + " in " + k);
		
		IndexSearcher searcher = new IndexSearcher(IndexReader.open(indexDir));

		Analyzer analyzer = new StopAnalyzer();
		QueryParser qp = new QueryParser(k, analyzer);
		qp.setOperator(QueryParser.DEFAULT_OPERATOR_AND);

		Query query;

		query = qp.parse(q);

		Hits hits = searcher.search(query);
		System.err.println ("found " + hits.length() + " hits");
		for (int i = 0; i < hits.length(); i++) {
			Document doc = hits.doc(i);
			String idStr = doc.get("id");
			System.err.println("doc=" + doc + " id=" + idStr + " title=" + doc.get("title"));
			Enumeration en = doc.fields();
			while (en.hasMoreElements()) {
				System.err.println (" ** " + en.nextElement());
			}
		}

	}
}
