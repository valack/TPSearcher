package ranker;

import java.io.IOException;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.BytesRef;

public class Ranker extends Similarity{

	ClassicSimilarity classicSim;
	Scorer scorer;

	public Ranker(){
		classicSim = new ClassicSimilarity();
	}

	@Override
	public long computeNorm(FieldInvertState state) {
		// TODO Auto-generated method stub
		classicSim.computeNorm(state);
		return 0;
	}

	@Override
	public SimWeight computeWeight(CollectionStatistics arg0, TermStatistics... arg1) {
		// TODO Auto-generated method stub
		classicSim.computeWeight(arg0, arg1);
		return null;
	}

	@Override
	public SimScorer simScorer(SimWeight weight, LeafReaderContext context) throws IOException {
		// TODO Auto-generated method stub
		
		return new Scorer (weight, context);
	}
	
	private class Scorer extends SimScorer{

		Similarity sim = new ClassicSimilarity();
		LeafReaderContext contextReader;
		SimWeight simWeight;
		
		public Scorer (SimWeight weight, LeafReaderContext context){
			contextReader = context;
			simWeight = weight;
		}
		
		@Override
		public float score(int doc, float freq) {
			
			/*
			 * 
			 * se extrae el valor de page rank del documento del context reader
			 * 
			 */
			
			Double pageRank = (Double) 0.0;
			try {
				pageRank = Double.valueOf(contextReader.reader().document(doc).get("PageRank"));
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			 * 
			 * se extrae el score del documento basico de lucene
			 * 
			 */
			Double luceneScore = (Double) 0.0;
			
			try {
				luceneScore = (double) sim.simScorer(simWeight, contextReader).score(doc, freq);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * 
			 * se extrae el valor de sentimiento del documento del context reader
			 * 
			 */
			Double sentimentValue = 0.0;
			
			try {
				sentimentValue = Double.valueOf(contextReader.reader().document(doc).get("sentiment"));
				System.out.println("SCORING del thread:" + contextReader.reader().document(doc).get("ThreadId") + "ls:" + luceneScore + " pr:"+ pageRank + " sv:" + sentimentValue);
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return (float) (luceneScore * 0.5 + pageRank * 0.4 + sentimentValue * 0.1);
		}

		@Override
		public float computeSlopFactor(int distance) {
			return 0;
		}

		@Override
		public float computePayloadFactor(int doc, int start, int end, BytesRef payload) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
