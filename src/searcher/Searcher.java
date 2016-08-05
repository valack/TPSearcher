package searcher;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.print.Doc;
import javax.swing.JOptionPane;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import ranker.Ranker;

import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;


/***
*
* @author Sipitria, Valacco, Zamora
*
*/


public class Searcher {

	private IndexSearcher indexSearcher;
	private Query query;
	private String error;
	private MultiFieldQueryParser queryParser;
	
	
	public Searcher(String indexPath){
		//Inicializo un parseador de query que sea multifield
		queryParser = new MultiFieldQueryParser(new String[]{"Content","Title"}, new StandardAnalyzer());
		//Obtengo el directorio del indice y creo un buscador del indice
		Path 	path = Paths.get(indexPath);
		try {
			indexSearcher = new IndexSearcher (DirectoryReader.open(FSDirectory.open(path)));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"No se pudo encontrar el directorio del indice");
			e.printStackTrace();
		}
		//Instancio el buscador por el indice con la similiridad nuestra
		Ranker ranker = new Ranker();
		indexSearcher.setSimilarity(ranker);
	}
	
	public String getError(){
		return error;
	}
	
	public void setQuery ( String term){

		query = null;
		
		try {
			query = queryParser.parse(term);
		
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(null,"Error al parsear el query");
			e.printStackTrace();
		}
	}
	
	public TopDocs search(int matchs, String searchText){
		TopDocs hits = null;
		setQuery(searchText);
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
