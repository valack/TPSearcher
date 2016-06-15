package evaluator;

import java.io.IOException;
import java.util.Vector;



public class Evaluator {


/*	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
/**
	 * Teniendo cargados los documentos con sus relevancias para cada consulta puedo comparar
	 * con el resultado de nuestro buscador y asi obtener las metricas.
**/



/*
				FOR DEBUG
				
			 for (QueryRelevance query : parser.parse()) {
				System.out.println("Query: " + query.getQuery());
				for (Integer docId : query.getHighlyRelevantDocs()) {
					System.out.println("Documento muy relevante: " + docId);
				}
				for (Integer docId : query.getPartiallyRelevantDocs()) {
					System.out.println("Documento parcialmente relevante: " + docId);
				}
				for (Integer docId : query.getIrrelevantDocs()) {
					System.out.println("Documento irrelevante: " + docId);
				}
			}



		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Se rompio al toque");
			e.printStackTrace();
		}

	}
*/
	private int recall10k;
	private int precision10k;
	private int ndcg;
	private Vector<QueryRelevance> relevanceData;

	public Evaluator(String dataComparationPath) throws IOException	{
		recall10k = -1;
		precision10k = -1;
		ndcg = -1;
		QueryRelParser parser = new QueryRelParser(dataComparationPath);//"Forum_Data\\queryRelJudgements"
		relevanceData = parser.parse();
	}


	private QueryRelevance getQuery(String query)	{
		for (QueryRelevance qr : relevanceData) {
			if (qr.getQuery().equals(query))
				return qr;
		}
		return null;
	}
	/**
	 * Hay que chequear si vamos a tener relevantes e irrelevantes nomas.. o vamos a tener las tres clases
	 */
	
	public void calculateRecall10k(String query, Vector<Integer> retrievedDocs)	{
		QueryRelevance q = getQuery(query);
		for (Integer docId : retrievedDocs) {
			if (q.getHighlyRelevantDocs().contains(docId)	)	
				recall10k++;
		}
		recall10k /= 10;
	}
	
	public void calculatePrecision10k(String query, Vector<Integer> retrievedDocs)	{
		QueryRelevance q = getQuery(query);
		for (Integer docId : retrievedDocs) {
			if (q.getIrrelevantDocs().contains(docId) || q.getPartiallyRelevantDocs().contains(docId))	
				precision10k++;
		}
		precision10k /= 10;
	}
	
	public void calculateNDCG(String query, Vector<Integer> retrievedDocs)	{
	
	}


	public int getRecall10k() {
		return recall10k;
	}


	public void setRecall10k(int recall10k) {
		this.recall10k = recall10k;
	}


	public int getPrecision10k() {
		return precision10k;
	}


	public void setPrecision10k(int precision10k) {
		this.precision10k = precision10k;
	}


	public int getNdcg() {
		return ndcg;
	}


	public void setNdcg(int ndcg) {
		this.ndcg = ndcg;
	}
	
}
