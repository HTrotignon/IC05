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

	private String nuance_origine;
	private String nuance_cible;
	private Double ratio_report;
	private int nb_voix_reportées;	// correspond au nombre de voix totales reportées sur le bureau de vote. utile si l'on souhaite pondérer les % de reports de voix
}