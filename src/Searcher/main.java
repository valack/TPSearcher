package Searcher;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/***
 *
 * @author Valacco, Zamora
 *
 */

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String xmlPath="E:\\Juan\\Facultad\\Optativas\\Analisis y Rec de Info\\Forum_Data\\All";
		String indexPath="E:\\Juan\\Facultad\\Optativas\\Analisis y Rec de Info\\index";
		
		//Creacion del indice a partir del dataset
	/*	Indexer indexer = new Indexer();
		indexer.index(xmlPath,indexPath);*/
		
		//Creacion de los queries
		//Query q=new TermQuery(new Term("Content", "Ubuntu"));
		QueryParser p= new QueryParser("Content", new StandardAnalyzer());
		Query q=null;
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
		TopDocs hits = searcher.search(indexPath, 100,q);
		
		System.out.println("Busqueda terminada");
		System.out.println("Cantidad de hits: " + hits.totalHits);
		
		for (ScoreDoc sd : hits.scoreDocs) {
			System.out.println(searcher.doc(sd.doc).get("ThreadID")+": "+sd.score);
		}
		

	}

}
