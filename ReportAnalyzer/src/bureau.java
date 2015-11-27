import java.util.Map;


public class bureau {
	public bureau( String[] ligneResultat ) {
		// format ligneResultat :
		// |num_tour|code_dpt|code_commune|nom_commune|num_bureau|nb_inscrits|nb_votants|nb_exprimes| ...
		// ... |num_candidat|nom_cand|prenom_cand|nuance|nb_voix|
		super();
		this.code_dpt = Integer.parseInt( ligneResultat[1] );
		this.code_commune = Integer.parseInt( ligneResultat[2] );
		this.num_bureau = Integer.parseInt( ligneResultat[4] );
		this.nb_inscrits = Integer.parseInt( ligneResultat[5] );
		
		if( 1 == Integer.parseInt( ligneResultat[0] ) ) {
			this.nb_votants_T1 = Integer.parseInt( ligneResultat[6] );
			this.nb_exprimés_T1 = Integer.parseInt( ligneResultat[7] );
		}
		else {
			this.nb_votants_T2 = Integer.parseInt( ligneResultat[6] );
			this.nb_exprimés_T2 = Integer.parseInt( ligneResultat[7] );
		}
		
		candidat newCandidat = new candidat( ligneResultat[8], ligneResultat[11] );
		addCandidat( newCandidat, ligneResultat[0] );
	}

	private int code_dpt;
	private int code_commune;
	private int num_bureau;
	
	private int nb_inscrits;
	private int nb_votants_T1;
	private int nb_exprimés_T1;
	
	private int nb_votants_T2;
	private int nb_exprimés_T2;	
	
	Map< candidat, Long > liste_candidats_T1;
	Map< candidat, Long > liste_candidats_T2;
	
	public void addCandidat( candidat newCandidat, String num_tour ){
		if( 1 == Integer.parseInt( num_tour ))
			{ this.liste_candidats_T1.put( newCandidat, Long.parseLong( num_tour )); }
		else
			{ this.liste_candidats_T2.put( newCandidat, Long.parseLong( num_tour )); }
	}
}
 