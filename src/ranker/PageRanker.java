package ranker;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import edu.uci.ics.jung.algorithms.scoring.PageRank;


public class PageRanker {

	LinksParser parser;
	DirectedGraph<String, DefaultEdge> threadLinks;
	PageRank<DirectedGraph<String, DefaultEdge>, Float> pageRanker;
	
	final float jumpProb = (float) 0.15;
	
	public PageRanker(String threadsPath) {
		
		parser 		= 	new LinksParser();
		threadLinks =	parser.parse("F:\\workspace\\TPSearcher\\Forum_Data\\linkData");
		PageRank<DirectedGraph<String, DefaultEdge>, Float>  pageRanker = new PageRank<>(threadLinks, jumpProb);
		pageRanker.evaluate();
		
	}

	public void showAllPageRank(){
		System.out.println(pageRanker.toString());
	}
//	
//	public Float getRankValue (String threadID){
//		if ( !threadLinks.containsVertex(threadID) )
//			return (float) 0;
//		
//		return pageRanker.getVertexScore(threadID);
//	}
	
}