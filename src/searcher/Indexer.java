package searcher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import gui.TPSearcher;
import ranker.PageRanker;
import ranker.Ranker;

/***
*
* @author Sipitria, Valacco, Zamora
*
*/


public class Indexer {

	//Atributos
	IndexWriter writer = null;
	XMLdocsParser dataParser; 
	
	//Metodos

	//Constructor
	public Indexer(String linkDataPath){		
		dataParser = new XMLdocsParser(linkDataPath);
	}
	

	//Si existe el indice lo devuelve y sino lo crea
	public IndexWriter getIndexWriter(boolean create, String indexPath) throws IOException {
		if (writer == null) {
			//Creo el camino donde voy a ubicar el indice
			Path path = Paths.get(indexPath);
			Directory indexDir = FSDirectory.open(path);
			//Creo la configuracion del indexwriter con nuestras similaridades
			Ranker ranker = new Ranker();
			IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
			config.setSimilarity(ranker);
			//Creo el indexwriter con la configuracion y el destino donde se ubicar�
			writer = new IndexWriter(indexDir, config);
		}
		return writer;
	}


	//Indexa un documento
	private void atomIndex(String xmlPath, String indexPath){
		
		Document thread = dataParser.parse(xmlPath);
		try {
			writer.addDocument(thread);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Error al agregar el documento"+thread.get("Path"));
			e.printStackTrace();
		}	
		TPSearcher.indexLog("  - Documento "+thread.get("ThreadID")+" agregado al indice, Sentimiento: "+thread.get("Sentiment"));

	}

	//Recorre el directorio dado e indexa los archivos xml encontrados
	public void index (String xmlPath, String indexPath, String linkDataPath){
		
		//Verifica que exista el directorio raiz de los archivos
		File f = new File(xmlPath);
		if (!f.exists()){ 
			JOptionPane.showMessageDialog(null,"No se pudo encontrar el directorio de threads");	
		}
		File[] directories = f.listFiles();

		//Inicializo el writer
		try {
			writer = getIndexWriter(false, indexPath);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Error al crear el indice");
			e.printStackTrace();
		}
		
		TPSearcher.indexLog("Escaneando directorio");
		TPSearcher.indexLog(" - - - - - - - - - - - - - - - - - - - - - - - ");
				
		//Por cada archivo en el directorio
		
	
		for (int x = 0; x < directories.length; x++)	{
			// Path contiene la ruta raiz mas las carpetas y archivos
			String path = xmlPath+"\\"+directories[x].getName(); 
			
			TPSearcher.indexLog("Path:"+path);
			
			String files;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			if (listOfFiles != null ){

				for (int i = 0; i < listOfFiles.length; i++) 
				{
					if (listOfFiles[i].isFile()) 
					{
						files = listOfFiles[i].getName();
						if (files.endsWith(".xml") || files.endsWith(".XML"))
						{
							TPSearcher.indexLog(" - "+path+"\\"+files);
							atomIndex(path+"\\"+files, indexPath);
						}
					}
				}
			}
			TPSearcher.indexLog("--------------------------------------------------------------------------------------------------------------------------------");
		}

		//Cierra el Writer
		try {
			closeIndexWriter();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"No se pudo cerrar el indice");
			e.printStackTrace();
		}
		TPSearcher.indexLog("Indice creado");

	}
	
	
	//Cierra el indice
	public void closeIndexWriter() throws IOException {
		if (writer != null) {
			writer.close();
		}
	}

}
