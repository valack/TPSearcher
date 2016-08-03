package Ranker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;



public class LinksParser {

	//private String linkDataPath = "F:\\workspace\\TPSearcher\\Forum_Data\\linkData";


	public LinksParser() {
		//this.linkDataPath = linkDataPath;
	}

	public DirectedGraph<String, DefaultEdge> parse(String linkDataPath)	{
		DirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		FileReader fileToRead = null;
		try {
			fileToRead = new FileReader(linkDataPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader buffer = new BufferedReader(fileToRead);
		String line = "";
		String source = "";
		String target = "";
		try {
			while (	buffer.ready()	) {
				//ciclo por todo el archivo

				line = buffer.readLine();
				
				source = line.substring(0, line.indexOf(":"));
				target = line.substring(line.indexOf(":")+1, line.length());
				
				graph.addVertex(source);
//				System.out.println("Source: "+source+" Agregado al grafo");
				graph.addVertex(target);
//				System.out.println("Target: "+target+" Agregado al grafo");
				graph.addEdge(source, target);
//				System.out.println("cantidad de links "+graph.edgeSet().size());
//				System.out.println(graph.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			buffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return graph;

	}
	
}
