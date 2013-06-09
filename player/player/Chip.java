package player;

class Chip
{
	static final int NORTH = 8;
	static final int SOUTH = 2;
	static final int WEST = 4;
	static final int EAST = 6;
	static final int NW = 7;
	static final int NE = 9;
	static final int SW = 1;
	static final int SE = 3;
	static final int NONE = 5;
	static final int SAME = 0;
    static final int BLACK = 0;
    static final int WHITE = 1;
    
    public int colour;
    Chip [][][] parents = new Chip[3][10][10];
    Chip [] children = new Chip [8];
    int childSize = 0;
    int [][] parentSize = new int [3][10];
    int x;
    int y;
    int list;
    int depth;
    int max_depth;
    int sublist [] = {0, 0, 0};
    Chip [] longest;
    boolean head = false;
    boolean complete = false;
    int chips = 0;

    public Chip(int col, int x, int y, int list)
    {
        colour = col;
        this.x = x;
        this.y = y;
        this.list = list;
        depth = 0;
        max_depth = 0;
        parentSize[list][0] = 0;
        head = true;
    }
    
    public Chip(int col, int x, int y)
    {
        colour = col;
        this.x = x;
        this.y = y;
    }
    
    public Chip(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    public int col()
    {
        return colour;
    }
    
    public int orientation(int x1, int y1){
    	if (x1 == x && y1 == y){
    		return SAME;
    	}
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
    
    public boolean check(Chip [][]n, int x, int y){
    	for (int i = 0; i < parentSize[list][sublist[list]]; i ++){
    		if (n[x][y] == parents[list][sublist[list]][i]){
    			return true;
    		}
    	}
    	int d;
    	if(head){
    		d = SAME;
    	}
    	else{
    		d = orientation(parents[list][sublist[list]][parentSize[list][sublist[list]] - 1].x, parents[list][sublist[list]][parentSize[list][sublist[list]] - 1].y);
    	}
    	switch(d){
    	case SAME: return false;
    	case NORTH: return orientation(x, y) == SOUTH;
    	case SOUTH: return orientation(x, y) == NORTH;
    	case WEST: return orientation(x, y) == EAST;
    	case EAST: return orientation(x, y) == WEST;
    	case NW: return orientation(x, y) == SE;
    	case NE: return orientation(x, y) == SW;
    	case SW: return orientation(x, y) == NE;
    	case SE: return orientation(x, y) == NW;
    	}
    	return false;
    }
    
    public void clear(){
    	for(int i = 0; i < childSize; i ++){
    		children[i].clear();
    	}
        parents = new Chip[3][10][10];
        children = new Chip [8];
        childSize = 0;
        parentSize = new int [3][10];
        depth = 0;
        max_depth = 0;
        longest = null;
        int [] templist = {0, 0, 0};
        sublist = templist;
        chips = 0;
    }
    
    public void update(Chip [][] n, int col){
    	
		for (int a = x+1; a < 8; a ++){
			if (n[a][y] != null){
				if(n[a][y].col() == col){
					if (check (n, a, y) == false){
						addChild(n[a][y]);
						n[a][y].update(n, col);
						break;
					}
				}
				else{
					break;
				}
			}
		}
		for (int b = y+1; b < 8; b ++){
			if (n[x][b] != null){
				if (n[x][b].col() == col){
					if (check(n, x, b) == false){
						addChild(n[x][b]);
						n[x][b].update(n, col);
						break;
					}
				}
				else{
					break;
				}
			}
		}
		for (int c = y-1; c > 0; c --){
			if (n[x][c] != null){
				if (n[x][c].col() == col){
					if (check(n, x, c) == false){
						addChild(n[x][c]);
						n[x][c].update(n, col);
						break;
					}
				}
				else{
					break;
				}
			}
		}
		for (int d = x-1; d > 0; d --){
			if (n[d][y] != null){
				if(n[d][y].col() == col){
					if (check(n, d, y) == false){
						addChild(n[d][y]);
						n[d][y].update(n, col);
						break;
					}
				}
				else{
					break;
				}
			}
		}
		for (int e = x + 1; e < 8; e ++){
			boolean c1 = false;
			boolean c2 = false;
			for (int f = y + 1; f < 8 && c1 == false; f ++){
				if (n[e][f] != null){
					if (n[e][f].col() == col){
						if (check(n, e, f) == false){
							addChild(n[e][f]);
							n[e][f].update(n, col);
							c1 = true;
						}
					}
					else{
						c1 = true;
					}
				}
			}
			for (int g = y - 1; g > 0 && c2 == false; g --){
				if (n[e][g] != null){
					if (n[e][g].col() == col){
						if (check(n, e, g) == false){
							addChild(n[e][g]);
							n[e][g].update(n, col);
							c2 = true;
						}
					}
					else{
						c2 = true;
					}
				}
			}
		}
		
		for (int e = x - 1; e > 0; e --){
			boolean c1 = false;
			boolean c2 = false;
			for (int f = y + 1; f < 8 && c1 == false; f ++){
				if (n[e][f] != null){
					if (n[e][f].col() == col){
						if (check(n, e, f) == false){
							addChild(n[e][f]);
							n[e][f].update(n, col);
							c1 = true;
						}
					}
					else{
						c1 = true;
					}
				}
			}
			for (int g = y - 1; g > 0 && c2 == false; g --){
				if (n[e][g] != null){
					if (n[e][g].col() == col){
						if (check(n, e, g) == false){
							addChild(n[e][g]);
							n[e][g].update(n, col);
							c2 = true;
						}
					}
					else{
						c2 = true;
					}
				}
			}
		}
    }
    
    public void addParent(Chip c){
    	int i;
    	if (c.head){
    		i = 0;
    	}
    	else{
    		i = c.sublist[c.list]-1;
    	}
    	parents[list][sublist[list]] = c.parents[c.list][i];
    	parentSize[list][sublist[list]] = c.parentSize[c.list][i];
    	parents[list][sublist[list]][parentSize[list][sublist[list]]] = c;
    	parentSize[list][sublist[list]]++;
    }
    
    public void addChild(Chip c){
    	chips ++;
    	c.list = this.list;
    	c.addParent(this);
    	c.depth = this.depth + 1;
    	if (c.depth == 6){
    		if (c.col() == BLACK && x == 7){
    			complete = true;
    		}
    		else if (c.col() == WHITE && y == 7){
    			complete = true;
    		}
    	}
    	if (c.depth > c.parents[list][0][0].max_depth){
    		c.parents[list][0][0].max_depth = c.depth;
    		Chip [] temp = c.parents[c.list][c.sublist[c.list]];
    		temp[c.parentSize[c.list][c.sublist[c.list]]] = c;
    		c.parents[list][0][0].longest = temp;
    	}
    	children[childSize] = c;
    	childSize++;
    	c.sublist[list]++;
    }
}
