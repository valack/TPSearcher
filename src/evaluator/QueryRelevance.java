package evaluator;

import java.util.Vector;

/**
 * @author Toshiba
 *
 */

public class QueryRelevance {

	private String query;
	private Vector<Integer> HighlyRelevantDocs;
	private Vector<Integer> PartiallyRelevantDocs;
	private Vector<Integer> IrrelevantDocs;


	public QueryRelevance(String query) {
		super();
		this.query = query;
		HighlyRelevantDocs = new Vector<Integer>();
		PartiallyRelevantDocs = new Vector<Integer>();
		IrrelevantDocs = new Vector<Integer>();
	}

	public QueryRelevance() {
		// TODO Auto-generated constructor stub
	}

	public void addDoc(Integer docId, Integer docRel) {
		switch (docRel) {
		case 0:
			IrrelevantDocs.add(docId);
			break;
		case 1:
			PartiallyRelevantDocs.add(docId);
			break;
		case 2:
			HighlyRelevantDocs.add(docId);
			break;
		}
	}

	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public Vector<Integer> getHighlyRelevantDocs() {
		return HighlyRelevantDocs;
	}

	public void setHighlyRelevantDocs(Vector<Integer> highlyRelevantDocs) {
		HighlyRelevantDocs = highlyRelevantDocs;
	}

	public Vector<Integer> getPartiallyRelevantDocs() {
		return PartiallyRelevantDocs;
	}

	public void setPartiallyRelevantDocs(Vector<Integer> partiallyRelevantDocs) {
		PartiallyRelevantDocs = partiallyRelevantDocs;
	}

	public Vector<Integer> getIrrelevantDocs() {
		return IrrelevantDocs;
	}

	public void setIrrelevantDocs(Vector<Integer> irrelevantDocs) {
		IrrelevantDocs = irrelevantDocs;
	}

}
