package player;

public class Best {
	public int score;
	public Move move;
	
	public Best(int score, Move m){
		this.score = score;
		this.move = m;
	}
	
	public Best(int score){
		this.score = score;
	}
	
	public Best(){
		
	}
}