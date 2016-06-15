package evaluator;

import java.util.Vector;

/**
 * @author Valacco
 *	Se tiene un query con los documentos relevantes divididos en categorias:
 *	muy relevantes (valor 2)
 *	Parcialmente relevantes (valor 1)
 *	irrelevantes (valor 0)
 */

public class QueryRelevance extends AbsQueryRelevance{

	//atributos
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
	
	@Override
	public QueryRelevance getQueryRelevance(String query) {
		if (this.query.equals(query))
			return this;
		return null;
	}
	
	public int getDocRelevance(int docId)	{ 
		if (HighlyRelevantDocs.contains(docId))
			return 2;
		else
			if (PartiallyRelevantDocs.contains(docId))
				return 1;
			else
				return 0;
	}
	
	public String getQuery() {
		return query;
	}

	public Vector<Integer> getHighlyRelevantDocs() {
		return HighlyRelevantDocs;
	}

	public Vector<Integer> getPartiallyRelevantDocs() {
		return PartiallyRelevantDocs;
	}

	public Vector<Integer> getIrrelevantDocs() {
		return IrrelevantDocs;
	}

}
