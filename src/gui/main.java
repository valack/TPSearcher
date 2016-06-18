package gui;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import evaluator.Evaluator;
import Searcher.Indexer;
import Searcher.Searcher;

/***
 *
 * @author Valacco, Zamora
 *
 */

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String xmlPath="F:\\Facultad\\Optativas\\Analisis y recuperacion de informacion\\Forum_Data\\All";
		String indexPath="index";
		/*
		//Creacion del indice a partir del dataset
		Indexer indexer = new Indexer();
		indexer.index(xmlPath,indexPath);
		*/
		//Creacion de los queries
		//Query q=new TermQuery(new Term("Content", "Ubuntu"));
		QueryParser p= new QueryParser("Content", new StandardAnalyzer());
		Query q=null;
		q = new TermQuery(new Term("contents", "lucene"));
		//TopDocs hits = is.search(q);
		try {
			q = p.parse("virtualbox keyboard problem");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("");
		System.out.println("----------------------------");
		System.out.println("");
		
		//Busqueda a partir del indice
		Searcher searcher = new Searcher();
		//TopDocs hits = searcher.search(indexPath, 100,q);
		
		TopDocs hits = searcher.search(indexPath, 10,q);
		
		System.out.println("Busqueda terminada");
		System.out.println("Cantidad de hits: " + hits.totalHits);
		
		for (ScoreDoc sd : hits.scoreDocs) {
			System.out.println(searcher.doc(sd.doc).get("ThreadID")+": "+sd.score+"   ");
		}
		try {
			Evaluator eval = new Evaluator("Forum_Data\\queryRelJudgements");
			eval.calculateRecall10k("virtualbox keyboard problem", hits, searcher);
			eval.calculatePrecision10k("virtualbox keyboard problem", hits, searcher);
			eval.calculateNDCG("virtualbox keyboard problem", hits, searcher);
			
			System.out.println("Recall: " + eval.getRecall10k());
			System.out.println("Precision: " + eval.getPrecision10k());
			System.out.println("NDCG: " + eval.getNdcg());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
