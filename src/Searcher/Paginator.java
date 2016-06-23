package Searcher;

/***
 * 
 * @author Sipitria, Valacco, Zamora
 *
 */


public class Paginator {

	private int pageStart;
	private int pageEnd;
	private int pageSize;
	private int docsLenght;
	private int pageCount;
	

	public Paginator() {
		super();
	}

	public void Paginate(int docsLenght, int pageSize){

		this.pageSize  	= pageSize;
		this.docsLenght		= docsLenght;
		pageStart     	= 0;
		pageEnd      	= pageSize;
		pageCount 		= 1;
	}

	public void nextPage(){
			pageStart = pageEnd;
			pageEnd   = Math.min(docsLenght, pageEnd+pageSize);
			pageCount++;
	}

	public void prevPage(){
			pageCount--;
			pageEnd   = pageStart;//pageEnd-pageSize;
			pageStart = Math.max(0, pageStart-pageSize);
	}
	
	public boolean isLastPage(){
		return docsLenght == pageEnd;
	}
	
	public boolean isFirstPage(){
		return pageStart==0;
	}

	public int getStart (){
		return this.pageStart;
	}
	
	public int getPage(){
		return this.pageCount;
	}

	public int getEnd (){
		return this.pageEnd;
	}

}