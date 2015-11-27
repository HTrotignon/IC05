
public class candidat {
	public candidat( String sId, String sNuance ) {
		super();
		this.id = Integer.parseInt( sId );
		this.nuance = sNuance;
	}
	
	private int id;
	private String nuance;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNuance() {
		return nuance;
	}
	public void setNuance(int nuance) {
		this.nuance = nuance;
	}
	
	
}