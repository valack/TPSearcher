package Searcher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;




public class Searcher {
	
	private IndexSearcher indexSearcher;
	
	
	public Searcher(){
	}
	
	public TopDocs search(String indexPath, int matchs, Query q){
		Path path = Paths.get(indexPath);
		try {
			indexSearcher = new IndexSearcher (DirectoryReader.open(FSDirectory.open(path)));
		} catch (IOException e) {
			System.out.println("El indice no existe");
			e.printStackTrace();
		}
		TopDocs hits = null;
		try {
			hits = indexSearcher.search(q,matchs);
		} catch (IOException e) {
			System.out.println("Error en la busqueda");
			e.printStackTrace();
		}
		
		return hits;
		
	}
	
	public Document doc(int docId){
		try {
			return indexSearcher.doc(docId);
		} catch (IOException e) {
			System.out.println("No se encontro el documento");
			e.printStackTrace();
		}
		return null;
		
	}
}
