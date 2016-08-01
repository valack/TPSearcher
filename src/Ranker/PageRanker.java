package Ranker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PageRanker {

	private String linkDataPath;


	public PageRanker(String linkDataPath) {
		super();
		this.linkDataPath = linkDataPath;
	}

	public void rank() throws IOException {

		FileReader fileToRead = new FileReader(linkDataPath);
		BufferedReader buffer = new BufferedReader(fileToRead);
		

	}
	
	public static void main(String[] args) {
		SentimentAnalyzer s = new SentimentAnalyzer();
		
	}
}