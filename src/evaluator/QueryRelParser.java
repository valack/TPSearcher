package evaluator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class QueryRelParser {

	private String queryRelDataPath;


	public QueryRelParser(String queryRelDataPath) {
		super();
		this.setQueryRelDataPath(queryRelDataPath);
	}

	public QueryRelJudgements parse() throws IOException {

		FileReader fileToRead = new FileReader(queryRelDataPath);
		BufferedReader buffer = new BufferedReader(fileToRead);
		
		try {
			String line = "";
			QueryRelevance query = new QueryRelevance();
			QueryRelJudgements qrJudgements = new QueryRelJudgements();
			
			while (	buffer.ready()	) {
				//ciclo por todo el archivo
				
				line = buffer.readLine();
				query = new QueryRelevance((String) line.subSequence(line.indexOf("<Query>"), 
						line.lastIndexOf("</Query>)")));
								
				while (	buffer.ready()	&&	!line.startsWith("<Query>")	) {
					//ciclo por cada query. Agrego el id y relevancia del documento en la query
					
					Integer docRel = Integer.parseInt(line.substring(line.length()-1));
					Integer docId = Integer.parseInt(line.substring(0, line.indexOf(" ")));
					query.addDoc(docId, docRel);
					line = buffer.readLine();
				}
				/*
				Integer docRel = Integer.parseInt(line.substring(line.length()-1));
				Integer docId = Integer.parseInt(line.substring(0, line.indexOf(" ")));
				query.addDoc(docId, docRel);
				habria que rehacer el while o ver que onda con el ultimo.. esta crotada capaz anda*/
				
				qrJudgements.addQueryRel(query);

			}

			buffer.close();
			return qrJudgements;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buffer.close();
		return null;

	}

	public String getQueryRelDataPath() {
		return queryRelDataPath;
	}

	public void setQueryRelDataPath(String queryRelDataPath) {
		this.queryRelDataPath = queryRelDataPath;
	}
}
