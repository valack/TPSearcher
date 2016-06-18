package evaluator;

import java.io.IOException;
import java.util.Vector;
import java.lang.Math;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import Searcher.Searcher;
/****
 *  Evaluador de metricas
 * @author Valacco
 *
 */

public class Evaluator {

	private final float invalid = -1;

	private Float recall10k;
	private Float precision10k;
	private Float ndcg;
	private Vector<QueryRelevance> relevanceData;

	public Evaluator(String dataComparationPath) throws IOException	{
		recall10k = invalid;
		precision10k = invalid;
		ndcg = invalid;
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

	public void calculateRecall10k(String query, TopDocs retrievedDocs, Searcher search)	{
		/**
		 * Tomo como relevantes tanto los parciales como los totalmente relevantes.
		 * Por cada ScoreDoc dentro de los TopDocs pregunto si el query contiene ese documento entre parcialtes 
		 * y totalmente relevantes y si es cierto aumento el contador.
		 * devuelvo la cantidad de documentos relevantes en la busqueda sobre la cantidad de documentos relevantes
		 * que tiene el query. 
		 * 
		 * DUDA: si limito el TopDocs para que me devuelva los 10 primeros y el query tiene mas de 10 relevantes, 
		 * deberia tomar como cota 10? porque si el query tiene 23 relevantes, por ejemplo, cuando lo divida por ese
		 * numero me achica bastante el recall, aunque quizas en el TopDocs sean todos relevantes (10/23)
		 */
		float recall = 0;
		QueryRelevance q = getQuery(query);
		float relevantDocs = q.getHighlyRelevantDocs().size() + q.getPartiallyRelevantDocs().size();
		for (ScoreDoc sd : retrievedDocs.scoreDocs) {
			if (	q.getHighlyRelevantDocs().contains(Integer.parseInt(search.doc(sd.doc).get("ThreadID")))
					|| q.getPartiallyRelevantDocs().contains(Integer.parseInt(search.doc(sd.doc).get("ThreadID")))	)	
				recall++;
		}
		recall10k = (float) recall/relevantDocs;
	}

	public void calculatePrecision10k(String query, TopDocs retrievedDocs, Searcher search)	{
		/**
		 * Es similar al anterior pero la unica diferencia es que divido la cantidad de documentos relevantes 
		 * de la busqueda por la cantidad de documentos recuperados (en este caso son 10 TopDocs)
		 */
		float precision = 0;
		QueryRelevance q = getQuery(query);
		for (ScoreDoc sd : retrievedDocs.scoreDocs) {
			if (	q.getHighlyRelevantDocs().contains(Integer.parseInt(search.doc(sd.doc).get("ThreadID")))
					|| q.getPartiallyRelevantDocs().contains(Integer.parseInt(search.doc(sd.doc).get("ThreadID")))	 )
				precision++;
		}
		precision10k = (float) precision/retrievedDocs.scoreDocs.length;
	}

	public void calculateNDCG(String query, TopDocs retrievedDocs, Searcher search)	{
		/**
		 * FORMULA
		 * dcg= (rel1 + sum(rel[i] / log(i) ) 
		 * ndcg = dcg / / dcgIDEAL
		 * 
		 * DUDA: cual es la relevancia? el score que le da lucene a los TopDocs o el valor de relevancia
		 * en los queryRelJudgements (0, 1, 2)
		 * 
		 * Version: relevancia como score de los TopDocs. El ideal relevance el score mas alto
		 */
//		QueryRelevance q = getQuery(query);
//		float relevance = 0;
//		// Inicializo la medida con la relevancia del primer documento recuperado
//		float ndcg = retrievedDocs.scoreDocs[0].score;
//		for (int i = 1; i < retrievedDocs.scoreDocs.length; i++) {
//			relevance = retrievedDocs.scoreDocs[i].score;
//			//Es i+1 porque la posición de documentos recuperados arranca en 1, pero el arreglo de ScoreDocs en 0
//			ndcg += relevance / Math.log(i+1);
//		}
//		float idealScore = retrievedDocs.scoreDocs[0].score;
//		float dcgIdeal = idealScore * retrievedDocs.scoreDocs.length;
//		this.ndcg = ndcg / dcgIdeal;
		/**
		 * Version: relevancia como los valores en el archivo de queryRelJudgements. 2 es la relevancia ideal.
		 */
		QueryRelevance q = getQuery(query);
		float relevance = 0;
		// Inicializo la medida con la relevancia del primer documento recuperado
		if (	q.getHighlyRelevantDocs().contains
				(Integer.parseInt(search.doc(retrievedDocs.scoreDocs[0].doc).get("ThreadID")))	)
			ndcg = (float) 2;
		else if (	q.getPartiallyRelevantDocs().contains
				(Integer.parseInt(search.doc(retrievedDocs.scoreDocs[0].doc).get("ThreadID")))	)
			ndcg = (float) 1;
		else 
			ndcg = (float) 0;
		//Itero por el resto del arreglo 
		for (int i = 1; i < retrievedDocs.scoreDocs.length; i++) {
			if (	q.getHighlyRelevantDocs().contains
					(Integer.parseInt(search.doc(retrievedDocs.scoreDocs[i].doc).get("ThreadID")))	)
				relevance = 2;
			else if (	q.getPartiallyRelevantDocs().contains
					(Integer.parseInt(search.doc(retrievedDocs.scoreDocs[i].doc).get("ThreadID")))	)
				relevance = 1;
			else 
				relevance = 0;
			//Es i+1 porque la posición de documentos recuperados arranca en 1, pero el arreglo de ScoreDocs en 0
			ndcg += (float) (relevance / Math.log(i+1));
		}
		float idealScore = 2;
		float dcgIdeal = idealScore * retrievedDocs.scoreDocs.length;
		this.ndcg = ndcg / dcgIdeal;
		
	}


	public float getRecall10k() {
		return recall10k;
	}

	public void setRecall10k(float recall10k) {
		this.recall10k = recall10k;
	}

	public float getPrecision10k() {
		return precision10k;
	}

	public void setPrecision10k(float precision10k) {
		this.precision10k = precision10k;
	}

	public float getNdcg() {
		return ndcg;
	}

	public void setNdcg(float ndcg) {
		this.ndcg = ndcg;
	}

}
