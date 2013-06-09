package player;

class Network
{
    public static int BLACK = 0;
    public static int WHITE = 1;
    static final int WIN = 1;
	static final int TIE = 0;
	static final int LOSS = -1;
    Chip [][] net;
    Chip [] connections = new Chip [3];
    int n_connections = 0;
    Chip [] oconnections = new Chip[3];
    int n_oconnections = 0;
    int col;
    int ocol;
    public boolean netComplete = false;
    int winner;
    int blackchips;
    int whitechips;
    int score = 0;
    int longestdepth = 0;
    int olongestdepth = 0;
    Chip [] longestcon;
    Chip [] olongestcon;
    int n_chips;
    int n_ochips;
    Chip [] chips;
    Chip [] ochips;
    int numberofchips=0;
    int numberofochips=0;
    public Network(int col, int ocol)
    {
        net = new Chip[8][8];
        this.col = col;
        this.ocol = ocol;
    }
    public boolean piecesAround(int c, int x, int y)
    {
        int ctr = 0;
        int col = c;
        for (int i = x-1; i <= x+1; i++){
            for (int j = y-1; j <= y+1; j++){
                if (ctr >= 2){
                    return true;
                }
                if (net[i][j] != null && col == net[i][j].col()){
                    ctr++;
                }
            }
        }
        return false;
    }
        
    public boolean isValidMove(Move m, int col)
    {
    	if (m.moveKind == Move.ADD){
    		if (col == this.col && numberofchips == 10){
    			return false;
    		}
    		else if (col == this.ocol && numberofochips == 10){
    			return false;
    		}
    	}
    	if (m.moveKind == Move.STEP && ((net[m.y2][m.x2] == null) ||net[m.y2][m.x2].col()!=col)){
    		return false;
    	}
        int x = m.y1;
        int y = m.x1;
        if ((x == 0 || x == 7) && (y == 0 || y == 7))
        {
            return false;
        }
        else if (col == BLACK && (y == 0 || y == 7))
        {
            return false;
        }
        else if (col == WHITE && (x == 0 || x == 7))
        {
            return false;
        }
        else if (net[x][y] != null)
        {
            return false;
        }
        else if (piecesAround(col, x, y))
        {
            return false;
        }
        else if (m.y2 == x && m.x2 == y){
            return false;
        }
        else
        {
            return true;
        }
    }
    public Move [] validMoves(Chip [] c){
        int col;
        int num = 0;
        Move [] moves = new Move [470];
        for (Chip k : c){
        	col = k.col();
        	for (int i = 0; i < 8; i++){
        		for (int j = 0; j < 8; j++){
        			Move m = new Move (j, i, k.y, k.x);
        			if ((k.x != i && k.y != j) && isValidMove(m, col)){
        				moves[num] = m;
        				num ++;
        			}
        		}
        	}
        }
        return moves;
    }
    
    public void update(){
    	clear();
    	n_chips = 0;
    	n_ochips = 0;
    	for (int i = 0; i < n_connections; i ++){
    		connections[i].update(net, col);
    		n_chips+=connections[i].chips;
    		if (connections [i].max_depth > longestdepth){
    			longestdepth = connections[i].max_depth;
    			longestcon = connections[i].longest;
    		}
    		if (connections[i].complete == true){
    			netComplete = true;
    			winner = col;
    		}
    	}
    	for (int i = 0; i < n_oconnections; i ++){
    		oconnections[i].update(net, ocol);
    		n_ochips+=oconnections[i].chips;
    		if (oconnections [i].max_depth > olongestdepth){
    			olongestdepth = oconnections[i].max_depth;
    			olongestcon = oconnections[i].longest;
    		}
    		if (connections[i].complete == true){
    			netComplete = true;
    			winner = ocol;
    		}
    	}
    }
    
    public void clear(){
    	for (int i = 0; i < n_connections; i ++){
    		connections[i].clear();
    	}
    	for (int i = 0; i < n_oconnections; i ++){
    		oconnections[i].clear();
    	}
    }
    
    public void addChip(Chip c, int col){
    	if (col == this.col){
    		chips[numberofchips] = c;
    		numberofchips++;
    	}
    	else{
    		ochips[numberofochips] = c;
    		numberofochips++;
    	}
    }
    
    public void removeChip(Chip c, int col){
    	if (col == this.col){
    		for (int i = 0; i < numberofchips; i++){
    			if (chips[i] == c){
    				chips[i] = null;
    				while (i < numberofchips-1){
    					chips[i] = chips[i+1];
    					i ++;
    				}
    			}
    		}
    	}
    }
    
    public void undoMove(Move m, int col){
    	Move undo = new Move(m.x2, m.y2, m.x1, m.y1);
    	makeMove(undo, col);
    }
    
    public boolean makeMove(Move m, int col){
    	if (isValidMove(m, col) == false){
    		return false;
    	}
    	if (m.moveKind == Move.ADD && net[m.y1][m.x1] == null){
    		if (col == BLACK && m.y1 == 0){
    			if (this.col == BLACK){
    				net[m.y1][m.x1] = new Chip(col, m.y1, m.x1, n_connections);
    				addChip(net[m.y1][m.x1], col);
    				connections[n_connections] = net[m.y1][m.x1];
    				n_connections ++;
    			}
    			else{
    				net[m.y1][m.x1] = new Chip(col, m.y1, m.x1, n_oconnections);
    				addChip(net[m.y1][m.x1], col);
    				oconnections[n_oconnections] = net[m.y1][m.x1];
    				n_oconnections ++;
    			}
    		}
    		else if (col == WHITE && m.x1 == 0){
    			if (this.col == WHITE){
    				net[m.y1][m.x1] = new Chip(col, m.y1, m.x1, n_connections);
    				addChip(net[m.y1][m.x1], col);
    				connections[n_connections] = net[m.y1][m.x1];
    				n_connections ++;
    			}
    			else{
    				net[m.y1][m.x1] = new Chip(col, m.y1, m.x1, n_oconnections);
    				addChip(net[m.y1][m.x1], col);
    				oconnections[n_oconnections] = net[m.y1][m.x1];
    				n_oconnections ++;
    			}
    		}
    		else{
    			net[m.y1][m.x1] = new Chip(col, m.y1, m.x1);
    			addChip(net[m.y1][m.x1], col);
    		}
    		update();
    	}
    	else if (m.moveKind == Move.STEP && net[m.y1][m.x1] == null){
    		Chip temp = net[m.y2][m.x2];
    		net[m.y2][m.x2] = null;
    		if (isValidMove(m, col)){
    			if (col == BLACK && m.y2 == 0){
        			if (this.col == BLACK){
        				for (int i = 0; i < n_connections; i ++){
        					if (connections[i].y == m.x2){
        						connections[i] = null;
        						while (i < n_connections - 1){
        							connections[i] = connections[i+1];
        							i++;
        						}
        					}
        				}
        				n_connections --;
        				removeChip(net[m.y2][m.x2], col);
        			}
        			else{
        				for (int i = 0; i < n_oconnections; i ++){
        					if (oconnections[i].y == m.x2){
        						oconnections[i] = null;
        						while (i < n_oconnections - 1){
        							oconnections[i] = oconnections[i+1];
        							i++;
        						}
        					}
        				}
        				n_oconnections --;
        				removeChip(net[m.y2][m.x2], col);
        			}
        		}
        		else if (col == WHITE && m.x2 == 0){
        			if (this.col == WHITE){
        				for (int i = 0; i < n_connections; i ++){
        					if (connections[i].x == m.y2){
        						connections[i] = null;
        						while (i < n_connections - 1){
        							connections[i] = connections[i+1];
        							i++;
        						}
        					}
        				}
        				n_connections --;
        				removeChip(net[m.y2][m.x2], col);
        			}
        			else{
        				for (int i = 0; i < n_oconnections; i ++){
        					if (oconnections[i].x == m.y2){
        						oconnections[i] = null;
        						while (i < n_oconnections - 1){
        							oconnections[i] = oconnections[i+1];
        							i++;
        						}
        					}
        				}
        				n_oconnections --;
        				removeChip(net[m.y2][m.x2], col);
        			}
        		}
        		if (col == BLACK && m.y1 == 0){
        			if (this.col == BLACK){
        				net[m.y1][m.x1] = new Chip(col, m.y1, m.x1, n_connections);
        				connections[n_connections] = net[m.y1][m.x1];
        				n_connections ++;
        			}
        			else{
        				net[m.y1][m.x1] = new Chip(col, m.y1, m.x1, n_oconnections);
        				oconnections[n_oconnections] = net[m.y1][m.x1];
        				n_oconnections ++;
        			}
        			addChip(net[m.y1][m.x1], col);
        		}
        		else if (col == WHITE && m.x1 == 0){
        			if (this.col == WHITE){
        				net[m.y1][m.x1] = new Chip(col, m.y1, m.x1, n_connections);
        				connections[n_connections] = net[m.y1][m.x1];
        				n_connections ++;
        			}
        			else{
        				net[m.y1][m.x1] = new Chip(col, m.y1, m.x1, n_oconnections);
        				oconnections[n_oconnections] = net[m.y1][m.x1];
        				n_oconnections ++;
        			}
        			addChip(net[m.y1][m.x1], col);
        		}
    			net[m.y1][m.x1] =  new Chip(col, m.y1, m.x1);
    			update();
    		}
    		else{
    			net[m.y2][m.x2] = temp; 
    			return false;
    		}
    	}
    	return true;
    }
		
	public int evaluation () { //run evaluation on BOARD itself after every move
		int score = 0;
		if (netComplete = true){
			if (winner == WHITE){
				return 10;
			}
			else if(winner == BLACK){
				return -10;
			}
		}
		int childdif = longestdepth - olongestdepth; //scores based on who has the longer connection
		int chipdif = n_chips - n_ochips; //scores based on who has the most connections
		score = score + childdif + chipdif;
		return score;
	}
	
	public Best chooseMove(int col, int alpha, int beta, int searchDepth) {
		int opposite = col == WHITE?BLACK:WHITE;
		Best myBest = new Best(); // My best move
		Best reply; // Opponent’s best reply
		int eval = evaluation();
		if (searchDepth == 0 || eval == 10 || eval == -10){
			return new Best(eval);
		}
		if (col == this.col) {
			myBest.score = alpha;
		} 
		else {
			myBest.score = beta;
		}
		Move [] temp;
		if (col == this.col){
			temp = validMoves(chips);
		}
		else{
			temp = validMoves(ochips);
		}
		for (Move m : temp) {
			makeMove(m, col); // Modifies "this" Grid
			reply = chooseMove(opposite, alpha, beta, searchDepth --);
			undoMove(m, col);
			if ((col == this.col) && (reply.score > myBest.score)) {
				myBest.move = m;
				myBest.score = reply.score;
				alpha = reply.score;
			} 
			else if ((col == this.ocol) && (reply.score < myBest.score)){
				myBest.move = m;
				myBest.score = reply.score;
				beta = reply.score;
			}
			if (alpha >= beta) {
				return myBest; 
			}
		}
		return myBest;
	}
}