
public class candidat {
	public candidat( String sId, String sNuance ) {
		super();
		this.id = Integer.parseInt( sId );
		this.nuance = sNuance;
	}
	
	private int id;
	private String nuance;
	private int score;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNuance() {
		return nuance;
	}
	public void setNuance(String nuance) {
		this.nuance = nuance;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	
}