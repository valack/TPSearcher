package evaluator;

import java.util.Vector;

public class QueryRelJudgements {  

	private Vector<QueryRelevance> queryRelData;

	
	public void addQueryRel(QueryRelevance qr) {
		queryRelData.addElement(qr);
	}
	
	public Vector<QueryRelevance> getQueryRelData() {
		return queryRelData;
	}

	public void setQueryRelData(Vector<QueryRelevance> queryRelData) {
		this.queryRelData = queryRelData;
	}
	
	
	
}
