package evaluator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;



public class QueryRelParser {

	private String queryRelDataPath;


	public QueryRelParser(String queryRelDataPath) {
		super();
		this.setQueryRelDataPath(queryRelDataPath);
	}

	public Vector<QueryRelevance> parse() throws IOException {

		FileReader fileToRead = new FileReader(queryRelDataPath);
		BufferedReader buffer = new BufferedReader(fileToRead);

		try {
			String line = "";
			QueryRelevance query = null;
			Vector<QueryRelevance> qrJudgements = new Vector<QueryRelevance>();

			while (	buffer.ready()	) {
				//ciclo por todo el archivo

				line = buffer.readLine();
				if (line.startsWith("<Query>"))	{
					if (query == null)	{
						
						//primer caso
						int queryStart = line.indexOf(">") + 1;
						int queryEnd = line.lastIndexOf("<");
						String queryName = line.substring(queryStart, queryEnd);
						query = new QueryRelevance(queryName);
					}
					else	{			
						
						//query nuevo en la lista, agrego el viejo y creo uno nuevo
						
						qrJudgements.add(query);
						int queryStart = line.indexOf(">") + 1;
						int queryEnd = line.lastIndexOf("<");
						String queryName = line.substring(queryStart, queryEnd);
						query = new QueryRelevance(queryName);
					}
				}
				else	{ 
					Integer docId = Integer.parseInt(line.substring(0, line.indexOf(" ")));
					Integer docRel = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
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
