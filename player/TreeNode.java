
public class TreeNode {
	static final int NORTH = 8;
	static final int SOUTH = 2;
	static final int WEST = 4;
	static final int EAST = 6;
	static final int NW = 7;
	static final int NE = 9;
	static final int SW = 1;
	static final int SE = 3;
	static final int NONE = 5;
	private int x;
	private int y;
	TreeNode child [] = new TreeNode [8];
	TreeNode ochild [] = new TreeNode[8];
	TreeNode parent;
	TreeNode t;
	int childSize = 0;
	int oChildSize = 0;
	int depth;
	public int x(){
		return x;
	}
	public int y(){
		return y;
	}
	public TreeNode (int x1, int y1, TreeNode p){
		this.x = x1;
		this.y = y1;
		parent = p;
		if (p == null){
			depth = 0;
		}
		else{
			depth = p.depth+1;
		}
	}
	
	public int orientation(int x1, int y1){
		if (x1 == x){
			if (y1 > y){
				return EAST;
			}
			else{
				return WEST;
			}
		}
		else if (y1 == y){
			if (x1 > x){
				return SOUTH;
			}
			else{
				return NORTH;
			}
		}
		else if (y1 > y){
			if (x1 > x){
				return SE;
			}
			else{
				return NE;
			}
		}
		else{
			if (x1 > x){
				return SW;
			}
			else if (x1 < x) {
				return NW;
			}
			else{
				return NONE;
			}
		}
	}
	
	public void addChild(TreeNode t){
		child[childSize] = t;
		childSize++;
	}

	
	public void addOChild(TreeNode t){
		ochild[oChildSize] = t;
		oChildSize++;
	}
	
	public int checkDepth(){
		if(parent == null){
			return 0;
		}
		else{
			return 1 + parent.checkDepth();
		}
	}
	
	public boolean isBetween(int x1, int y1, int x2, int y2){
		int orient = orientation (x1, y1);
		if (orient != NONE && orient == orientation(x2, y2)){
			int total1 = Math.abs(x1 - this.x) + Math.abs(y1 - this.y);
			int total2 = Math.abs(x2 - this.x) + Math.abs(y2 - this.y);
			if (total1 < total2){
				return true;
			}
		}
		return false;
	}
	
	public void addChild(int x, int y){
		int orient = orientation(x, y);
		if (orient != NONE){
			boolean marker = false;
			for (int i = 0; i < childSize; i++){
				if (orient == orientation(child[i].x(), child[i].y())){
					if (isBetween (x, y, child[i].x(), child[i].y())){
						TreeNode temp = new TreeNode(x,y,this);
						temp.addChild(child[i]);
						child[i] = temp;
					}
					marker = true;
					break;
				}
			}
			if (marker == false){
				child[childSize] = new TreeNode(x, y, this);
				childSize ++;
			}
		}
	}
	
	public void breakChild(int x, int y){
		int orient = orientation(x, y);
		if (orient != NONE){
			for (int i = 0; i < childSize; i++){
				if (isBetween (x, y, child[i].x(), child[i].y())){
					TreeNode temp = new TreeNode(x,y,this);
					temp.t=child[i];
					addOChild(temp);
					child[i] = null;
					for (; i < childSize - 1; i++){
						child[i] = child[i+1];
					}
					childSize--;
					break;
				}
			}
		}
	}
	
	public void removeFromList(int x, int y){
		for (int i = 0; i < childSize; i ++){
			if(child[i].x() == x && child[i].y() == y){
				child[i] = null;
				for(int j = i; j < childSize - 1; j ++){
					child[j] = child[j+1];
				}
				child[i].removeFromList(x, y);
			}
		}
	}
	
	public void removeFromOList(int x, int y){
		for(int i = 0; i < oChildSize; i ++){
			if(ochild[i].x() == x && ochild[i].y() == y){
				addChild(ochild[i].t);
				ochild[i] = null;
				for(int j = i; j < oChildSize - 1; j ++){
					ochild[j] = ochild[j+1];
				}
			}
			child[i] = null;
			for(int j = i; j < childSize - 1; j ++){
				child[j] = child[j+1];
			}
		}
		for (int i = 0; i < childSize; i ++){
			child[i].removeFromList(x, y);
		}
	}
	
	public void addToList(int x, int y){
		addChild(x, y);
		for (int i = 0; i < childSize; i ++){
			child[i].addToList(x, y);
		}
	}
	
	public void oAddToList(int x, int y){
		breakChild(x, y);
		for (int i = 0; i < childSize; i ++){
			child[i].oAddToList(x, y);
		}
	}
}
