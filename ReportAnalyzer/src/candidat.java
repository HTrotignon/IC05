
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
	public String getNuance() {
		return nuance;
	}
	public void setNuance(String nuance) {
		this.nuance = nuance;
	}
	
	
}