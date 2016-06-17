package Searcher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.xml.sax.SAXException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


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

	//Cierra el indice
	public void closeIndexWriter() throws IOException {
		if (writer != null) {
			writer.close();
		}
	}

	//Parsea el xml en html
	private Document parse( String path){

		//detecting the file type
		BodyContentHandler handler = new BodyContentHandler(-1);
		Metadata metadata = new Metadata();
		FileInputStream inputstream = null;
		try {
			inputstream = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Archivo no encontrado: "+path);
			e.printStackTrace();
		}
		ParseContext pcontext = new ParseContext();

		HtmlParser htmlparser = new HtmlParser();
		try {
			htmlparser.parse(inputstream, handler, metadata,pcontext);
		} catch (IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al parsear el archivo");
			e.printStackTrace();
		}
		
		String strXml = handler.toString();
		String threadId= strXml.substring(0, strXml.indexOf("\n"));
		String content= strXml.substring(strXml.indexOf("\n"));
		String title = metadata.get("title");		
		
		if (title==null){
			title="";
		}
		
		/*
		System.out.println("Title: "+title);
		System.out.println("Path: "+path);
		System.out.println("ThreadID: "+threadId);
		System.out.println("Content: "+content);
		*/
		
		Document thread = new Document();
		thread.add(new TextField("Title",title,Field.Store.YES));
		thread.add(new TextField("Path",path,Field.Store.YES));
		thread.add(new TextField("ThreadID",threadId,Field.Store.YES));
		thread.add(new TextField("Content",content,Field.Store.NO));

		return thread;
	}	

	//Indexa un documento
	private void atomIndex(String xmlPath, String indexPath){

		Document thread=parse(xmlPath);
		try {
			writer.addDocument(thread);
		} catch (IOException e) {
			System.out.println("Error al agregar el documento"+thread.get("Path"));
			e.printStackTrace();
		}	
		System.out.println("Documento "+thread.get("ThreadID")+" agregado al indice");

	}

	//Recorre el directorio dado e indexa los archivos xml encontrados
	public void index (String xmlPath, String indexPath){

		//Verifica que exista el directorio raiz de los archivos
		File f = new File(xmlPath);
		if (!f.exists()){ 
			System.out.println("La ruta "+xmlPath+" no existe");	
		}
		File[] ficheros = f.listFiles();

		//Inicializo el writer
		try {
			writer = getIndexWriter(false, indexPath);
		} catch (IOException e) {
			System.out.println("Error al crear el indice");
			e.printStackTrace();
		}

		//Por cada archivo en el directorio
		for (int x=0;x<ficheros.length;x++){
			
			// Path contiene la ruta raiz mas las carpetas y archivos
			String path = xmlPath+"\\"+ficheros[x].getName(); 
			System.out.println(path);

			String files;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles(); 

			for (int i = 0; i < listOfFiles.length; i++) 
			{
				if (listOfFiles[i].isFile()) 
				{
					files = listOfFiles[i].getName();
					if (files.endsWith(".xml") || files.endsWith(".XML"))
					{
						System.out.println(path+"\\"+files);
						atomIndex(path+"\\"+files, indexPath);
					}
				}
			}
		}
		
		//Cierra el Writer
		try {
			closeIndexWriter();
		} catch (IOException e) {
			System.out.println("No se pudo cerrar el indice");
			e.printStackTrace();
		}
		System.out.println("indice creado");


	}


}
