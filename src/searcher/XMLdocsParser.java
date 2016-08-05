package searcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import ranker.PageRanker;
import ranker.SentimentAnalyzer;

/***
*
* @author Sipitria, Valacco, Zamora
*
*/

public class XMLdocsParser {


	SentimentAnalyzer sentAnalyzer;
	PageRanker pageRanker;
	
	String strXml;
	String threadId;
	String content;
	String title;
	Integer sentiment;
	Double pageRank;
	
	ParseContext pcontext;
	BodyContentHandler handler;
	Metadata metadata;
	HtmlParser htmlparser;

	
	public XMLdocsParser(String linksDataPath) {
		
		sentAnalyzer = new SentimentAnalyzer();
		pageRanker = new PageRanker(linksDataPath);
		
		strXml 		= "";
		threadId  	= "";
		content   	= "";
		title    	= "";
		sentiment 	= 1;
		pageRank = (double) 0;
		
	}
//	F:\Facultad\Optativas\Analisis y recuperacion de informacion\Forum_Data\All


	//Parsea el xml en html
	public Document parse(String path){

		//detecting the file type
		pcontext = new ParseContext();
		handler = new BodyContentHandler(-1);
		metadata = new Metadata();
		htmlparser = new HtmlParser();
		FileInputStream inputstream = null;
		try {
			inputstream = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Archivo no encontrado: "+path);
			e.printStackTrace();
		}

		try {
			htmlparser.parse(inputstream, handler, metadata, pcontext);
		} catch (IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al parsear el archivo");
			e.printStackTrace();
		}
		
		strXml = handler.toString();
		threadId= strXml.substring(0, strXml.indexOf("\n"));
		content= strXml.substring(strXml.indexOf("\n"));
		title = metadata.get("title");		
		
		if (title==null){
			title="";
		}
		//-------------------------------------------------------------//
		//Analizo el sentimiento del thread 
		sentiment = sentAnalyzer.calculateThreadSentiment(content);
		
		//Pregunto el page rank del thread y lo almaceno en el indice
		pageRank = pageRanker.getRankValue(threadId);
		
		Document thread = new Document();
		thread.add(new TextField("Title",title,Field.Store.YES));
		thread.add(new TextField("Path",path,Field.Store.YES));
		thread.add(new TextField("ThreadID",threadId,Field.Store.YES));
		thread.add(new TextField("Sentiment",sentiment.toString(),Field.Store.YES));
		thread.add(new TextField("PageRank",pageRank.toString() ,Field.Store.YES));
		thread.add(new TextField("Content",content,Field.Store.NO));
		
		System.gc();
		
		return thread;
	}
	
}
