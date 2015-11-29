import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class bureau {
	public bureau( String[] ligneResultat ) {
		// format ligneResultat :
		// |id|num_tour|code_dpt|code_commune|num_bureau|nb_inscrits|nb_votants|nb_exprimes|num_candidat|nuance|nb_voix|
		
		super();
		this.id = ligneResultat[0];
		this.code_dpt = Integer.parseInt( ligneResultat[2] );
		this.code_commune = Integer.parseInt( ligneResultat[3] );
		this.num_bureau = Integer.parseInt( ligneResultat[4] );
		this.nb_inscrits = Double.parseDouble( ligneResultat[5] );
		
		if( 1 == Integer.parseInt( ligneResultat[1] ) ) {
			this.nb_votants_T1 = Double.parseDouble( ligneResultat[6] );
			this.nb_exprimés_T1 = Double.parseDouble( ligneResultat[7] );
		}
		else {
			this.nb_votants_T2 = Double.parseDouble( ligneResultat[6] );
			this.nb_exprimés_T2 = Double.parseDouble( ligneResultat[7] );
		}		
	}

	private String id;
	private int code_dpt;
	private int code_commune;
	private int num_bureau;
	
	private double nb_inscrits;
	private double nb_votants_T1;
	private double nb_exprimés_T1;
	
	private double nb_votants_T2;
	private double nb_exprimés_T2;
	
	private int nb_voix_reportées = 0;
	private Map< candidat, Double > liste_candidats_T1 = new HashMap< candidat, Double>();
	private Map< candidat, Double > liste_candidats_T2 = new HashMap< candidat, Double>();
	private List< report > liste_reports = new ArrayList< report >();
	
	public void addCandidat( candidat newCandidat, int num_tour, double nb_voix ){
		//System.out.println( num_tour );
		if( 1 == num_tour )
			{ liste_candidats_T1.put( newCandidat, nb_voix ); }
		else
			{ liste_candidats_T2.put( newCandidat, nb_voix ); }
	}

	public void calculReportV1() {
		System.out.println( "BEGIN" );
		this.liste_candidats_T1.forEach(( candidat1 , nb_voix1 ) -> {
			System.out.println( "THEN" );
			if( false == liste_candidats_T2.containsKey( candidat1 )) {
				System.out.println( "ELSE" );
				this.liste_candidats_T2.forEach(( candidat2, nb_voix2 ) -> {
					System.out.println( "TOTO" );
					// on considère que les votants pour les candidats T2 ont aussi voté pour eux au T1
						report newReport = new report( candidat1.getNuance(), candidat2.getNuance(), nb_voix2*nb_voix1/nb_exprimés_T2 );
						liste_reports.add( newReport );
						nb_voix_reportées += nb_voix1;
						System.out.println( Double.toString( nb_voix1 ));
				});
			}
		});
		
		liste_reports.forEach((report) -> {
			report.setRatio_report( report.getRatio_report()/nb_voix_reportées );
			System.out.println( "" );
			System.out.println( Double.toString( report.getRatio_report() ));
		});
	}

	public int getCode_dpt() {
		return code_dpt;
	}

	public void setCode_dpt(int code_dpt) {
		this.code_dpt = code_dpt;
	}

	public int getCode_commune() {
		return code_commune;
	}

	public void setCode_commune(int code_commune) {
		this.code_commune = code_commune;
	}

	public int getNum_bureau() {
		return num_bureau;
	}

	public void setNum_bureau(int num_bureau) {
		this.num_bureau = num_bureau;
	}
}
 