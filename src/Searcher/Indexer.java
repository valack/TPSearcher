package Searcher;
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

/***
*
* @author Sipitria, Valacco, Zamora
*
*/


public class Indexer {

	//Atributos
	private IndexWriter writer = null;
	//Metodos

	//Constructor
	public Indexer(){		
	}
	

	//Si existe el indice lo devuelve y sino lo crea
	public IndexWriter getIndexWriter(boolean create, String indexPath) throws IOException {
		if (writer == null) {
			Path path = Paths.get(indexPath);
			Directory indexDir = FSDirectory.open(path);
			IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
			writer = new IndexWriter(indexDir, config);
		}
		return writer;
	}


	//Indexa un documento
	private void atomIndex(String xmlPath, String indexPath){
		
		XMLdocsParser dataParser = new XMLdocsParser();
		Document thread = dataParser.parse(xmlPath);
		try {
			writer.addDocument(thread);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Error al agregar el documento"+thread.get("Path"));
			e.printStackTrace();
		}	
		TPSearcher.indexLog("  - Documento "+thread.get("ThreadID")+" agregado al indice");

	}

	//Recorre el directorio dado e indexa los archivos xml encontrados
	public void index (String xmlPath, String indexPath){

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
		
	
		for (int x=0; x<directories.length; x++)	{
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
