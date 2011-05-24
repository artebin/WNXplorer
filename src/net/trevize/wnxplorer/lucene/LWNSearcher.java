package net.trevize.wnxplorer.lucene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LuceneWNSearcher.java - Mar 25, 2010
 */

public class LWNSearcher {

	private Analyzer analyzer;
	private Searcher searcher;
	private TopDocs topdocs;
	private QueryParser qparser;
	private Query query;
	private int doc_per_page = 1024;

	public LWNSearcher(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public void search(String field, String queryString, String indexPath) {
		topdocs = null;

		try {
			searcher = new IndexSearcher(new SimpleFSDirectory(new File(
					indexPath)));

			qparser = new QueryParser(Version.LUCENE_30,
					LWNUtils.SYNSET_DATA_WORD_LEMMA, analyzer);

			qparser.setAllowLeadingWildcard(true);

			query = qparser.parse(queryString);

			topdocs = searcher.search(query, doc_per_page);

			//display number of results.
			System.out.println(topdocs.totalHits + " results.");

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeQueryLog() {
		String logFilename = "./lucene-query-results-logs/query-"
				+ Calendar.getInstance().getTimeInMillis() + ".log";
		FileWriter fw = null;
		BufferedWriter bw = null;

		//write the query result in a file.
		try {
			fw = new FileWriter(logFilename);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bw.write("####\n" + query + "\n####\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (topdocs.totalHits > 0) {
			int result_id = 0;
			while (result_id < topdocs.totalHits) {
				StringBuffer sb = new StringBuffer();

				try {
					int doc_id = topdocs.scoreDocs[result_id].doc;
					Field[] fields = searcher.doc(doc_id).getFields(
							LWNUtils.SYNSET_DATA_WORD_LEMMA);
					sb.append(fields[0].stringValue());
					for (int i = 1; i < fields.length; ++i) {
						sb.append("," + fields[i].stringValue());
					}
					sb.append("\n");
				} catch (CorruptIndexException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					bw.write(sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}

				++result_id;
			}
		}

		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		LWNSearcher searcher = new LWNSearcher(new WhitespaceAnalyzer());
		searcher.search(LWNUtils.SYNSET_DATA_GLOSS, "drive", "./WNLuceneIndex");
		searcher.writeQueryLog();
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

	public TopDocs getTopdocs() {
		return topdocs;
	}

	public void setTopdocs(TopDocs topdocs) {
		this.topdocs = topdocs;
	}

	public QueryParser getQparser() {
		return qparser;
	}

	public void setQparser(QueryParser qparser) {
		this.qparser = qparser;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public int getDoc_per_page() {
		return doc_per_page;
	}

	public void setDoc_per_page(int docPerPage) {
		doc_per_page = docPerPage;
	}

}
