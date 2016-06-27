package Searcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

/***
*
* @author Sipitria, Valacco, Zamora
*
*/

public class XMLdocsParser {

	//Parsea el xml en html
	public Document parse(String path){

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
	
}
