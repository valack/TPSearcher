package Ranker;

import java.io.IOException;
import java.util.HashMap;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class PageRanker {

	LinksParser parser;
	DirectedGraph<String, DefaultEdge> threadMap;
	HashMap<String, Float> pageRankValues;
	
	public PageRanker() {
		
		parser = new LinksParser();
		pageRankValues = new HashMap<>();
		threadMap =	parser.parse("F:\\workspace\\TPSearcher\\Forum_Data\\linkData");
	
	}
	
	
	
	public void pageRank() throws IOException {
		/* 
		    public int path[][] = new int[10][10];
		    public double pagerank[] = new double[10];
		  
		public void calc(double n)
		{    
		 double init;
		 double c=0; 
		 double temp[] = new double[10];
		 int i,j,u=1,k=1;
		 init = 1/n;
		 System.out.printf(" n value:"+n+"\t init value :"+init+"\n");
		 for(i=1;i<=n;i++)
		   this.pagerank[i]=init;
		   System.out.printf("\n Initial PageRank Values , 0th Step \n");
		    for(i=1;i<=n;i++)
		   System.out.printf(" Page Rank of "+i+" is :\t"+this.pagerank[i]+"\n");
		   
		   
		   while(u<=2)
		   {
		    for(i=1;i<=n;i++)
		    {  temp[i]=this.pagerank[i];
		          this.pagerank[i]=0;
		       }
		     
		 for(j=1;j<=n;j++)
		        for(i=1;i<=n;i++)
		     if(this.path[i][j] == 1)
		     {  k=1;c=0; 
		           while(k<=n)
		     {
		       if(this.path[i][k] == 1 )
		                   c=c+1;
		     k=k+1;
		     } 
		             this.pagerank[j]=this.pagerank[j]+temp[i]*(1/c);    
		      } 
		   
		  System.out.printf("\n After "+u+"th Step \n"); 
		     for(i=1;i<=n;i++) 
		      System.out.printf(" Page Rank of "+i+" is :\t"+this.pagerank[i]+"\n"); 
		   
		     u=u+1;
		    } 
		}
	
		 
		}*/
	}
	
	
	
	public Float getRankValue (String threadID){
		return pageRankValues.get(threadID);		
	}
	
}