public class innerList {

	Chip head;
	public innerList(int x, int y){
		head = new TreeNode(x, y, null);
	}
	
	public void addToList(int x, int y){
		head.addToList(x, y);
	}
	
	public void oAddToList(int x, int y){
		head.oAddToList(x, y);
	}
	
	public void update(Chip [][] n, int col){
		if (col == Network.BLACK){
			for (int a = x; a < 8; a ++){
				if (n[a][y] != null){
					if(n[a][y].col() == col){
						head.addChild(n[a][y]);
						break;
					}
					else{
						break;
					}
				}
			}
			for (int b = y; b < 7; b ++){
				if (n[x][b] != null){
					head.addChild(n[x][b]);
					break;
				}
			}
			for (int c = y; c > 0; c --){
				if (n[x][c] != null){
					head.addChild(n[x][c]);
				}
			}			
		}
	}
	
	public void update()
}