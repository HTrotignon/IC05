public class report {
	public report( String nuanceCandidat1, String NuanceCandidat2, Double alpha) {
		// TODO Auto-generated constructor stub
		super();
		this.nuance_origine = nuanceCandidat1;
		this.nuance_cible = NuanceCandidat2;
		this.setRatio_report( alpha );
	}

	public Double getRatio_report() {
		return ratio_report;
	}
	public void setRatio_report(Double ratioReport) {
		this.ratio_report = ratioReport;
	}

	public String getNuance_origine() {
		return nuance_origine;
	}

	public String getNuance_cible() {
		return nuance_cible;
	}

	public int getNb_voix_report�es() {
		return nb_voix_report�es;
	}
	
	public void setNb_voix_report�es(int nb_voix_report�es) {
		this.nb_voix_report�es = nb_voix_report�es;
	}
	
	private String nuance_origine;
	private String nuance_cible;
	private Double ratio_report;
	private int nb_voix_report�es;	// correspond au nombre de voix totales report�es sur le bureau de vote. utile si l'on souhaite pond�rer les % de reports de voix

}
