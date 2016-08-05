package ranker;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class PageRanker {

	LinksParser parser;
	DirectedSparseGraph<String, Integer> threadLinks;
	
	PageRank<String, Integer> pageRanker;
	
	final float jumpProb = (float) 0.15;
	final float maxTolerance = (float) 0.00000000001;

	
	public PageRanker(String linksPath) {
		
		parser 		= 	new LinksParser();
		threadLinks =	parser.parse(linksPath);
		pageRanker  = new PageRank<String, Integer>(threadLinks, jumpProb);
		pageRanker.setTolerance(maxTolerance);
		pageRanker.evaluate();
		
	}
//
//	public void showAllPageRank(){
//		System.out.println(threadLinks.toString());
//		
//		String[] vertices = {"733868","537777","928963","744517","631835","480314","467336","528820","683034","960219","651451","671065","753467","584992","511131","482963","1021254","736901","536456" };
//		
//		for (int i = 0; i < vertices.length; i++) {	
//			System.out.println("rank: "+pageRanker.getVertexScore(vertices[i]));
//		}
//	}
	
	public Double getRankValue (String threadID){
		if ( !threadLinks.containsVertex(threadID) )
			return (double) 0;
		
		return pageRanker.getVertexScore(threadID);
	}
	
}