package Searcher;

public class Paginator {

	private int start;
	private int end;
	private int pageSize;
	//private int pageCount;
	private int length;
	/*
	 * falta revisar los controles de ultima pagina y primera pagina, pageCount debe ser usada?
	 */
	
	public Paginator (int xLength, int xPageSize){
		
		length    = xLength;
		pageSize  = xPageSize;
		start     = 0;
		end       = pageSize-1;
		//pageCount = 1;
		
	}
	
	public void nextPage(){
		
		if (end+pageSize<=length) {
			start = end+1;
			end   = Math.min(length, end+pageSize);
			//pageCount++;
		}
		
	}
	
	public void prevPage(){
		
		if (start-pageSize>=0){
			//pageCount--;
			start = Math.max(0, start-pageSize);
			end   = end-pageSize;
		}
	}

}
