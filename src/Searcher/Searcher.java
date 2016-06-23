package Searcher;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.ParseException;



public class Searcher {

	private IndexSearcher indexSearcher;
	private Query query;
	private String error;

	public Searcher(){
	}
	
	public String getError(){
		return error;
	}
	
	public void setQuery (String field, String term){
		QueryParser p= new QueryParser(field, new StandardAnalyzer());
		query = null;
		try {
			query = p.parse(term);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(null,"Error al parsear el query");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TopDocs search(String indexPath, int matchs){
		Path 	path = Paths.get(indexPath);
		TopDocs hits = null;
		try {
			indexSearcher = new IndexSearcher (DirectoryReader.open(FSDirectory.open(path)));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"No se pudo encontrar el directorio del indice");
			e.printStackTrace();
		}
		try {
			hits = indexSearcher.search(query,matchs);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Error en la busqueda");
			e.printStackTrace();
		}

		return hits;

	}

	public Document doc(int docId){
		try {
			return indexSearcher.doc(docId);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"No se encontro el documento");
			e.printStackTrace();
		}
		return null;
	}
}
