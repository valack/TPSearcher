package ranker;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.TypesafeMap;
import edu.stanford.nlp.pipeline.Annotation;


public class SentimentAnalyzer {

	final int POSITIVE = 2;
	final int NEUTRAL = 1;
	final int NEGATIVE = 0;
	/**
	 * Key: ThreadId. Value: Sentiment del thread.
	 */
	private StanfordCoreNLP pipeline;

	public SentimentAnalyzer(){
		//Inicialización del sentiment analizer
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
		//------------//

	}

	public Integer calculateThreadSentiment(String threadContent){

			Annotation annotation = pipeline.process(threadContent);

			System.out.println("Comienzo del analisis del thread");
			int PosCount = 0, NegCount = 0, NeuCount = 0;
			Integer mainSentiment = -1;
			//-------------//
			//Sentencias del thread a ser analizadas. El sentimiento del post, será el que tenga la mayoria
			List<CoreMap> sentences = ((TypesafeMap) annotation).get(CoreAnnotations.SentencesAnnotation.class);
			String sentiment = "";
			for (CoreMap sentence : sentences) {
				sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
				switch (sentiment) {
				case "Positive":
					//Se le da mas relevancia cuando se encuentra una sentencia positiva
					PosCount+=2;
					break;
				case "Negative":
					NegCount++;
					break;
				case "Neutral":
					//Se le da mas relevancia cuando se encuentra una sentencia neutral
					NeuCount+=2;
					break;
				default:
					break;
				}
			}
			if (PosCount >= NeuCount)
				if (PosCount >= NegCount) 
					mainSentiment = POSITIVE;
				else if (NeuCount >= NegCount) 
					mainSentiment = NEUTRAL;
				else	mainSentiment = NEGATIVE;
			else	if (NeuCount >= NegCount)
				mainSentiment = NEUTRAL;
			else	mainSentiment = NEGATIVE;
			
			
			return mainSentiment;
	}

}
