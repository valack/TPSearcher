package evaluator;

import java.util.Vector;

public class QueryRelJudgements extends AbsQueryRelevance {  

	private Vector<QueryRelevance> queryRelData;

	
	public void addQueryRel(QueryRelevance qr) {
		queryRelData.addElement(qr);
	}
	
	@Override
	public QueryRelevance getQueryRelevance(String query) {
		QueryRelevance queryRel = null;
		for (QueryRelevance qr : queryRelData) {
			queryRel = qr.getQueryRelevance(query);
			if (queryRel != null)
				return queryRel;
		}
		return null;
	}
	
	public Vector<QueryRelevance> getQueryRelData() {
		return queryRelData;
	}

	public void setQueryRelData(Vector<QueryRelevance> queryRelData) {
		this.queryRelData = queryRelData;
	}
	
}
