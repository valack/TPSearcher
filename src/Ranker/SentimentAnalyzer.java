package Ranker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.TypesafeMap;
import edu.stanford.nlp.pipeline.Annotation;

public class SentimentAnalyzer {

	/**
	 * Key: ThreadId. Value: Sentiment del thread.
	 */
	private Hashtable<Integer, String> threadsSentiments;

	public SentimentAnalyzer(){
		threadsSentiments = new Hashtable<>();
	}

	private String calculatePostSentiment(String postContent){
		System.out.println("Comienzo del analisis por post");
		int PosCount = 0, NegCount = 0, NeuCount = 0;
		String mainSentiment = "";
		//String text = postContent;
		//-------------//
		//Inicialización del sentiment analizer
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = pipeline.process(postContent);
		//------------//
		//Sentencias del post a ser analizadas. El sentimiento del post, será el que tenga la mayoria
		List<CoreMap> sentences = ((TypesafeMap) annotation).get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
			switch (sentiment) {
			case "Positive":
				PosCount++;
				break;
			case "Negative":
				NegCount++;
				break;
			case "Neutral":
				NeuCount++;
				break;
			default:
				break;
			}
		}
		if (PosCount >= NeuCount)
			if (PosCount >= NegCount) 
				mainSentiment = "Positive";
			else if (NeuCount >= NegCount) 
				mainSentiment = "Neutral";
			else	mainSentiment = "Negative";
		else	if (NeuCount >= NegCount)
			mainSentiment = "Neutral";
		else	mainSentiment = "Negative";
		System.out.println("");
		return mainSentiment;
	}



	private void calculateSingleThreadSentiment(String threadPath) throws FileNotFoundException{

		int PosCount = 0, NegCount = 0, NeuCount = 0;
		String mainSentiment = "";
		Integer threadID = 0;

		//detecting the file type
		FileReader fileToRead = new FileReader(threadPath);
		BufferedReader buffer = new BufferedReader(fileToRead);

		try {
			String line = "";
			System.out.println("Arranca el analisis por cada thread");
			
			while (	buffer.ready()	) {
				//ciclo por todo el archivo

				line = buffer.readLine();
				if (line.startsWith("<ThreadID>")){
					int textStart = line.indexOf(">") + 1;
					int textEnd = line.lastIndexOf("<");
					threadID = Integer.parseInt(line.substring(textStart,textEnd));
				}

				if (line.startsWith("<icontent>") || line.startsWith("<rcontent>"))	{

					int textStart = line.indexOf(">") + 1;
					int textEnd = line.lastIndexOf("<");
					String postContent = line.substring(textStart, textEnd);						
					String sentiment = calculatePostSentiment(postContent);

					switch (sentiment) {
					case "Positive":
						PosCount++;
						break;
					case "Negative":
						NegCount++;
						break;
					case "Neutral":
						NeuCount++;
						break;
					default:
						break;
					}
				}
			}
			buffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (PosCount >= NeuCount)
			if (PosCount >= NegCount) 
				mainSentiment = "Positive";
			else if (NeuCount >= NegCount) 
				mainSentiment = "Neutral";
			else	mainSentiment = "Negative";
		else	if (NeuCount >= NegCount)
			mainSentiment = "Neutral";
		else	mainSentiment = "Negative";
		System.out.println("----------------------- Thread ID : "+threadID+" sentimiento : "+mainSentiment);
		
		
		threadsSentiments.put(threadID, mainSentiment);
	}


	public void calculateThreadsSentiment(String threadsPath) throws FileNotFoundException	{
		//Verifica que exista el directorio raiz de los archivos
		File f = new File(threadsPath);
		if (!f.exists()){ 
			System.out.println("No se pudo encontrar el directorio de threads");
		}
		File[] directories = f.listFiles();

		//Por cada archivo en el directorio

		for (int x=0; /*x<directories.length*/x<1; x++)	{
			// Path contiene la ruta raiz mas las carpetas y archivos
			String path = threadsPath+"\\"+directories[x].getName(); 
			
			System.out.println("Carpeta :"+path);

			String files;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();

			if (listOfFiles != null ){
				System.out.println("la lista de archivos no es null, itera por todos los archivos en la carpeta----------------");
				for (int i = 0; i < /*listOfFiles.length*/ 5; i++) 
				{
					if (listOfFiles[i].isFile()) 
					{
						files = listOfFiles[i].getName();
						if (files.endsWith(".xml") || files.endsWith(".XML"))
						{
							System.out.println("Path del XML : - "+path+"\\"+files);
							calculateSingleThreadSentiment(path+"\\"+files);
							//atomIndex(path+"\\"+files, indexPath);
						}
					}
				}
			}
		}
	}
	
	public void HashToFile () throws IOException{
		String fileName = "Forum_Data\\ThreadsSentimentAnalysis";
		File hashFile = new File(fileName);
		if (!hashFile.exists()) {
			//System.out.println("");
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			
			for (Integer key : threadsSentiments.keySet()) {
				bw.write(key+" "+threadsSentiments.get(key)+"\n");
			}
			
			bw.close();
		}
	}
}
