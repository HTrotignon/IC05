import java.util.List;
import java.util.Map;


public class bureau {
	public bureau( String[] ligneResultat ) {
		super();
		this.code_dpt = Integer.parseInt( ligneResultat[1] );
		this.code_commune = Integer.parseInt( ligneResultat[2] );
		this.num_bureau = Integer.parseInt( ligneResultat[4] );
		this.bn_inscrits = Integer.parseInt( ligneResultat[5] );
		
		candidat newCandidat = new candidat( ligneResultat[8], ligneResultat[11] );
		
		if( 1 == Integer.parseInt( ligneResultat[0] ) ){
			this.nb_votants_T1 = Integer.parseInt( ligneResultat[6] );
			this.nb_exprimés_T1 = Integer.parseInt( ligneResultat[7] );
			this.liste_candidats_tour1 = liste_candidats_tour1;
		}
		else{
			this.nb_votants_T2 = Integer.parseInt( ligneResultat[6] );
			this.nb_exprimés_T2 = Integer.parseInt( ligneResultat[7] );
			this.liste_candidats_tour2 = liste_candidats_tour2;
		}
	}


	private int code_dpt;
	private int code_commune;
	private int num_bureau;
	
	private int bn_inscrits;
	private int nb_votants_T1;
	private int nb_exprimés_T1;
	
	private int nb_votants_T2;
	private int nb_exprimés_T2;
	
	
	Map< candidat, Long > liste_candidats_tour1;
	Map< candidat, Long > liste_candidats_tour2;
	
	public void addCandidat(){
		
	}
	
}
 