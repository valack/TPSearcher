package evaluator;

import java.io.IOException;


public class Evaluator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QueryRelParser parser = new QueryRelParser("Forum_Data\\queryRelJudgements");
		try {
			QueryRelJudgements relData = parser.parse();
			/*
			 * Teniendo cargados los documentos con sus relevancias para cada consulta puedo comparar
			 * con el resultado de nuestro buscador y asi obtener las metricas.
			 */
			
			
			
			
			/*	FOR DEBUG
			 * 
			 * for (QueryRelevance query : relData.getQueryRelData()) {
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
			*/


		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Se rompio al toque");
			e.printStackTrace();
		}

	}

}
