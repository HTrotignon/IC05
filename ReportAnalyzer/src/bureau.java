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
	private double somme_reports = 0;
	private Map< candidat, Integer > liste_candidats_T1 = new HashMap< candidat, Integer >();
	private Map< candidat, Integer > liste_candidats_T2 = new HashMap< candidat, Integer >();
	private List< report > liste_reports = new ArrayList< report >();

	public void addAbstention( int numTour) {
		candidat abstention = new candidat( "666666", "Abstention" );
		candidat blancsNuls = new candidat( "7777777", "Blancs et Nuls" );
		
		if( 1 == numTour ) {
			this.addCandidat( abstention, numTour, this.getNb_inscrits()-this.getNb_votants_T1() ); 
			this.addCandidat( blancsNuls, numTour, this.getNb_votants_T1()-this.getNb_exprimés_T1() );
		}
		else {
			this.addCandidat( abstention, numTour, this.getNb_inscrits()-this.getNb_votants_T2() ); 
			this.addCandidat( blancsNuls, numTour, this.getNb_votants_T2()-this.getNb_exprimés_T2() );
		}
	}
	
	public void addCandidat( candidat newCandidat, int numTour, int nbVoix ){
		// putain de grosse dégueulasserie de l'enfer \o/
		
		switch( newCandidat.getNuance() ){
		case "MNR" :
		case "EXD" :
		case "LEXD" :
		case "BC-EXD" :
			newCandidat.setNuance( "Extreme Droite" );
			break;
		case "FN" :
		case "LFN" :
		case "BC-FN" :
			newCandidat.setNuance( "Front National" );
			break;
		case "RPF" :
		case "DVD" :
		case "DL" :
		case "MPF" :
		case "LDVD" :
		case "PRV" :
		case "BC-UD" :
		case "BC-DLF" :
		case "BC-DVD" :
			newCandidat.setNuance( "Divers Droite" );
			break;
		case "RPR" :
		case "UMP" :
		case "MAJ" :
		case "LDD" :
		case "LMAJ" :
		case "M-NC" :
		case "M" :
		case "LUMP" :
		case "BC-UMP" :
			newCandidat.setNuance( "Union de la Droite" );
			break;
		case "UDF" :
		case "MDC" :
		case "PRG" :
		case "PREP" :
		case "RDG" :
		case "LDR" :
		case "UDFD" :
		case "LCMD" :
		case "LMC" :
		case "LGC" :
		case "MODM" :
		case "CEN" :
		case "ALLI" :
		case "NCE" :
		case "LMDM" :
		case "LUC" :
		case "LUDI" :
		case "BC-MDM" :
		case "BC-UC" :
		case "BC-UDI" :	
			newCandidat.setNuance( "Centre" );
			break;
		case "DIV" :
		case "CPNT" :
		case "LDV" :
		case "LCP" :
		case "AUT" :
		case "LAUT" :
		case "LDIV" :
		case "BC-DIV" :
			newCandidat.setNuance( "Divers" );
			break;
		case "REG" :
		case "LREG" :
			newCandidat.setNuance( "Regionalistes" );
			break;
		case "ECO" :
		case "VEC" :
		case "LEC" :
		case "LVE" :
		case "LVEC" :
		case "BC-VEC" :
			newCandidat.setNuance( "Ecologistes" );
			break;
		case "SOC" :
		case "LGA" :
		case "LSOC" :
		case "BC-SOC" :
			newCandidat.setNuance( "Parti Socialiste" );
			break;
		case "DVG" :
		case "LDG" :
		case "LUG" :
		case "LDVG" :
		case "BC-UG" :
		case "BC-RDG" :	
		case "BC-DVG" :
			newCandidat.setNuance( "Divers Gauche" );
			break;
		case "COM" :	
		case "LCOM" :	
		case "LCOP" :	
		case "PG" :	
		case "FG" :	
		case "LFG" :	
		case "LPG" :	
		case "BC-FG" :	
		case "BC-PG" :	
		case "BC-COM" :	
			newCandidat.setNuance( "Front de Gauche" );
			break;
		case "EXG" :	
		case "LEXG" :	
		case "LCR" :	
		case "LO" :	
		case "LXG" :	
		case "BC-EXG" :
			newCandidat.setNuance( "Extreme Gauche" );
			break;
	}
		
		
		//System.out.println( num_tour );
		if( 1 == numTour )
			{ liste_candidats_T1.put( newCandidat, nbVoix ); }
		else
			{ liste_candidats_T2.put( newCandidat, nbVoix ); }
	}

	public List<report> getListe_reports() {
		return liste_reports;
	}

	public void calculReportV1() {
		//System.out.println( "BEGIN" );
		this.liste_candidats_T1.forEach(( candidat1 , nb_voix1 ) -> {
			//System.out.println( " T1 " + candidat1.getNuance() + " voix : " + nb_voix1 );
			if( false == liste_candidats_T2.containsKey( candidat1 )) {
				this.liste_candidats_T2.forEach(( candidat2, nb_voix2 ) -> {
						//System.out.println( " T2 " + candidat2.getNuance() + " voix : " + nb_voix2 );
					// on considère que les votants pour les candidats T2 ont aussi vot� pour eux au T1
						//System.out.println( nb_exprim�s_T2 );
						report newReport = new report( candidat1.getNuance(), candidat2.getNuance(), (double)nb_voix2*nb_voix1/nb_inscrits );
						liste_reports.add( newReport );
						nb_voix_reportées += nb_voix1;
						//System.out.println( Integer.toString( nb_voix1 ));
				});
			}
		});
		
		liste_reports.forEach((report) -> {
			report.setRatio_report( report.getRatio_report()/nb_voix_reportées );
			report.setNb_voix_reportées( (int)(nb_inscrits*report.getRatio_report()) );
			//somme_reports += report.getRatio_report();
			//System.out.println( Integer.toString( report.getNb_voix_reportées() ));
		});
		//System.out.println( somme_reports );
		//System.out.println( "END" );
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

	public int getNb_inscrits() {
		return nb_inscrits;
	}
	
	public String getCode_dpt() {
		return code_dpt;
	}
}
 