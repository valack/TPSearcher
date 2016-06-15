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
			QueryRelevance query = null;
			QueryRelJudgements qrJudgements = new QueryRelJudgements();

			while (	buffer.ready()	) {
				//ciclo por todo el archivo

				line = buffer.readLine();
				if (line.startsWith("<Query>"))	{
					if (query == null)	
						
						//primer caso
						
						query = new QueryRelevance((String) line.subSequence(line.indexOf("<Query>"), 
								line.indexOf("</Query>)")));
					else	{			
						
						//query nuevo en la lista, agrego el viejo y creo uno nuevo
						
						qrJudgements.addQueryRel(query);
						query = new QueryRelevance((String) line.subSequence(line.indexOf("<Query>"), 
								line.indexOf("</Query>)")));
					}
				}
				else	{ 
					Integer docRel = Integer.parseInt(line.substring(line.length()-1));
					Integer docId = Integer.parseInt(line.substring(0, line.indexOf(" ")));
					query.addDoc(docId, docRel);
				}
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
