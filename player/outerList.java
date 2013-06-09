public class outerList {
	
	private innerList arr[];
	private int size;
 
	public outerList() {
		arr = new innerList [3];
		size = 0;
	}	
	public void newList(int x, int y){
		arr[size] = new innerList(x, y);
		size++;
	}
	
	public void addToList(int x, int y){
		for (int i = 0; i < size; i ++){
			arr[i].addToList(x, y);
		}
	}
	
	public void updateList(Chip [][] n){
		for (int i = 0; i < size; i ++){
			arr[i].update();
		}
	}
	
	public void oAddToList(int x, int y){
		for (int i = 0; i < size; i ++){
			arr[i].oAddToList(x, y);
		}
	}
	
	public int length() {
		return size;
	}
}