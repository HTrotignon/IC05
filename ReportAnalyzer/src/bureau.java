import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class bureau {
	public bureau( String[] ligneResultat ) {
		// format ligneResultat :
		// |id|num_tour|code_dpt|code_commune|num_bureau|nb_inscrits|nb_votants|nb_exprimes|num_candidat|nuance|nb_voix|
		
		super();
		this.code_dpt = ligneResultat[2];
		this.code_commune = Integer.parseInt( ligneResultat[3] );
		this.num_bureau = ligneResultat[4];
		this.nb_inscrits = Integer.parseInt( ligneResultat[5] );
		
		candidat newCandidat = new candidat( ligneResultat[8], ligneResultat[9] );
		this.addCandidat( newCandidat, Integer.parseInt( ligneResultat[1] ), Integer.parseInt( ligneResultat[10] ));
	}

	private String code_dpt;
	private int code_commune;
	private String num_bureau;
	
	private int nb_inscrits;
	private int nb_votants_T1 = 0;
	private int nb_exprimés_T1 = 0;
	
	private int nb_votants_T2 = 0;
	private int nb_exprimés_T2 = 0;
	
	private int nb_voix_reportées = 0;
	private Map< candidat, Integer > liste_candidats_T1 = new HashMap< candidat, Integer >();
	private Map< candidat, Integer > liste_candidats_T2 = new HashMap< candidat, Integer >();
	private List< report > liste_reports = new ArrayList< report >();
	
	public void addCandidat( candidat newCandidat, int num_tour, int nb_voix ){
		//System.out.println( num_tour );
		if( 1 == num_tour )
			{ liste_candidats_T1.put( newCandidat, nb_voix ); }
		else
			{ liste_candidats_T2.put( newCandidat, nb_voix ); }
	}

	public void calculReportV1() {
		System.out.println( "BEGIN" );
		this.liste_candidats_T1.forEach(( candidat1 , nb_voix1 ) -> {
			//System.out.println( " T1 " + candidat1.getNuance() + " voix : " + nb_voix1 );
			if( false == liste_candidats_T2.containsKey( candidat1 )) {
				this.liste_candidats_T2.forEach(( candidat2, nb_voix2 ) -> {
						//System.out.println( " T2 " + candidat2.getNuance() + " voix : " + nb_voix2 );
					// on considère que les votants pour les candidats T2 ont aussi voté pour eux au T1
						//System.out.println( nb_exprimés_T2 );
						report newReport = new report( candidat1.getNuance(), candidat2.getNuance(), (double)nb_voix2*nb_voix1/nb_exprimés_T2 );
						liste_reports.add( newReport );
						nb_voix_reportées += nb_voix1;
						//System.out.println( Integer.toString( nb_voix1 ));
				});
			}
		});
		
		liste_reports.forEach((report) -> {
			report.setRatio_report( report.getRatio_report()/nb_voix_reportées );
			//System.out.println( nb_voix_reportées );
			System.out.println( Double.toString( report.getRatio_report() ));
		});
		System.out.println( "END" );
	}
	
	public int getNb_votants_T1() {
		return nb_votants_T1;
	}

	public void setNb_votants_T1(int nb_votants_T1) {
		this.nb_votants_T1 = nb_votants_T1;
	}

	public int getNb_votants_T2() {
		return nb_votants_T2;
	}

	public void setNb_votants_T2(int nb_votants_T2) {
		this.nb_votants_T2 = nb_votants_T2;
	}

	public int getNb_exprimés_T1() {
		return nb_exprimés_T1;
	}

	public void setNb_exprimés_T1(int nb_exprimés_T1) {
		this.nb_exprimés_T1 = nb_exprimés_T1;
	}

	public int getNb_exprimés_T2() {
		return nb_exprimés_T2;
	}

	public void setNb_exprimés_T2(int nb_exprimés_T2) {
		this.nb_exprimés_T2 = nb_exprimés_T2;
	}
}
 