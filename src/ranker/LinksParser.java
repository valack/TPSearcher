package ranker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.uci.ics.jung.graph.DirectedSparseGraph;



public class LinksParser {

	private DirectedSparseGraph<String, Integer> graph; 


	public LinksParser() {
		graph = new DirectedSparseGraph<String, Integer>();
	}

	public DirectedSparseGraph<String, Integer> parse(String linkDataPath)	{
		int edgeCount = 0;
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
				graph.addEdge(new Integer(edgeCount++), source, target);
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
