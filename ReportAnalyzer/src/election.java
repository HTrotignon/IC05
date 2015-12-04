import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class election {
	
	public election( String nomElection, String nomFichier, String dateD, String dateF ) {
		this.nom = nomElection;
		this.date_début = dateD;
		this.date_fin = dateF;
		try {
			Scanner scanner = new Scanner( new File( nomFichier ));
			try {
				// Lecture du fichier ligne par ligne. Cette boucle se termine quand la méthode retourne la valeur null.
				while( scanner.hasNextLine() ) {
				    String line = scanner.nextLine();
				    // Debug
				    //System.out.println( line );
				    
				    //////////////////////////////////////////////////////////////////////
				    // penser à modifier le séparateur en fonction du fichier à traiter //
				    //////////////////////////////////////////////////////////////////////
				    
				    // séparateur "\\s" : CN01, CN04, CN08, CN11, DP15, LG02, LG07, LG12, RG04, RG10
				    // séparateur ";"	: MN08, 
				    // séparateur "tab"	: MN14, 
				    String[] parts = line.split( "\\s" );
				    
				    checkAndAddBureau( parts );
				    
				    
				    if( -1 == this.liste_nuances.indexOf( parts[9] ) )
				    	{ this.liste_nuances.add( parts[9] ); }
				}
			} finally {
			// dans tous les cas, on ferme nos flux
				scanner.close();
			}
		} catch( IOException ioe ) {
		// erreur de fermeture des flux
			System.out.println( "Erreur --" + ioe.toString() );
		}
	}

	private void checkAndAddBureau( String[] sBureau ) {
		candidat newCandidat = new candidat( sBureau[8], sBureau[9] );
		
	    if( liste_bureau.containsKey( sBureau[0] )) {
	    	// on met à jour le nombre de votants dans le bureau. Réalisé 1 fois par bureau
	    	if(( 1 == Integer.parseInt( sBureau[1] )) & ( 0 == liste_bureau.get( sBureau[0] ).getNb_votants_T1() ))
	    	{
	    		liste_bureau.get( sBureau[0] ).setNb_votants_T1( Integer.parseInt( sBureau[6] ));
	    		liste_bureau.get( sBureau[0] ).setNb_exprimés_T1( Integer.parseInt( sBureau[7] ));
	    		liste_bureau.get( sBureau[0] ).addAbstention( 1 );
	    	}
	    	else if(( 2 == Integer.parseInt( sBureau[1] )) & ( 0 == liste_bureau.get( sBureau[0] ).getNb_votants_T2() ))
	    	{
	    		liste_bureau.get( sBureau[0] ).setNb_votants_T2( Integer.parseInt( sBureau[6] ));
	    		liste_bureau.get( sBureau[0] ).setNb_exprimés_T2( Integer.parseInt( sBureau[7] ));
	    		liste_bureau.get( sBureau[0] ).addAbstention( 2 );	    		
	    	}
	    	
	    	//System.out.println( "id bureau existant. Ajour candidat" );
	    	liste_bureau.get( sBureau[0] ).addCandidat(newCandidat, Integer.parseInt( sBureau[1] ), Integer.parseInt( sBureau[10] ));
	    }
	    else {
	    	bureau newBureau = new bureau ( sBureau );
	    	if( 1 == Integer.parseInt( sBureau[1] ))
	    	{
	    		newBureau.setNb_votants_T1( Integer.parseInt( sBureau[6] ));
	    		newBureau.setNb_exprimés_T1( Integer.parseInt( sBureau[7] ));
	    	}
	    	else if( 2 == Integer.parseInt( sBureau[1] ))
	    	{
	    		newBureau.setNb_votants_T2( Integer.parseInt( sBureau[6] ));
	    		newBureau.setNb_exprimés_T2( Integer.parseInt( sBureau[7] ));
	    	}
	    	//System.out.println( "nouveau bureau, ajout dans la map" );
	    	
	    	liste_bureau.put( sBureau[0], newBureau );
	    }
	}

	Map< String, bureau > liste_bureau = new HashMap< String, bureau >();
	Map< String, report > liste_reports = new HashMap< String, report >();
	private List< String > liste_nuances = new ArrayList< String >();
	private String nom;
	private String date_début;
	private String date_fin;
	
	public String getDateDebut() {
		return date_début;
	}
	public void setDateDebut(String date) {
		this.date_début = date;
	}
	public String getDateFin() {
		return date_fin;
	}
	public void setDateFin(String date) {
		this.date_fin = date;
	}
	public String getNom() {
		return nom;
	}
	
	private void sommeReports() {
		this.liste_bureau.forEach( ( id, bureau ) -> {
			bureau.getListe_reports().forEach( ( report ) ->{
				String idReport = report.getNuance_origine() + report.getNuance_cible();
				if( this.liste_reports.containsKey( idReport )){
					this.liste_reports.get( idReport ).setNb_voix_reportées( liste_reports.get( idReport ).getNb_voix_reportées() + report.getNb_voix_reportées());
				}
				else {
					this.liste_reports.put( idReport, report );
				}
			});;
		});
	}

	private List< bureau > getBureauxDpt( String numDpt ) {
		System.out.println( "getBureauDpt :: BEGIN" );
		List< bureau > listeDpt = new ArrayList< bureau >();
		
		this.liste_bureau.forEach( ( id, bureau ) -> {
			if( numDpt.equals( bureau.getCode_dpt() )) {
//				System.out.println( bureau.getCode_dpt() );
				listeDpt.add( bureau );
			}
		});
		
		listeDpt.forEach( (bureau) -> bureau.calculReportV1());
		
		System.out.println( "getBureauDpt :: END" );
		return listeDpt;
	}
	
	private void exportToCSV( String fichierCible ) {
		System.out.println( "Début export" );
		
//		String fileRatio = "/tempo/IC05-workSets/" + fichierCible + "/ratio_" + fichierCible + ".csv";
//		String fileNbVoix = "/tempo/IC05-workSets/" + fichierCible + "/nbVoix_" + fichierCible + ".csv";
		
		try {
			FileWriter writerRatio = new FileWriter( fichierCible, true );
//			FileWriter writerNbVoix = new FileWriter( fileNbVoix );
			
			//ajout des labels colonnes
			writerRatio.append( "Source" );
			writerRatio.append(',');
			writerRatio.append( "Target" );
			writerRatio.append(',');
			writerRatio.append( "pourcentage report" );
			writerRatio.append(',');
			writerRatio.append( "date début" );
			writerRatio.append(',');
			writerRatio.append( "date fin" );
			writerRatio.append('\n');
			
//			writerNbVoix.append( "Source" );
//			writerNbVoix.append(',');
//			writerNbVoix.append( "Target" );
//			writerNbVoix.append(',');
//			writerNbVoix.append( "nombre report" );
//			writerNbVoix.append('\n');
			
			this.liste_bureau.forEach( ( id, bureau ) -> {
				bureau.getListe_reports().forEach( ( report ) -> {
					try {
						writerRatio.append( report.getNuance_origine() );
						writerRatio.append(',');
						writerRatio.append( report.getNuance_cible() );
						writerRatio.append(',');
						writerRatio.append( Double.toString( report.getRatio_report() ));
						writerRatio.append(',');
						writerRatio.append( this.getDateDebut() );
						writerRatio.append(',');
						writerRatio.append( this.getDateFin() );
						writerRatio.append('\n');
						
//						writerNbVoix.append( report.getNuance_origine() );
//						writerNbVoix.append(',');
//						writerNbVoix.append( report.getNuance_cible() );
//						writerNbVoix.append(',');
//						writerNbVoix.append( Integer.toString( report.getNb_voix_reportées() ));
//						writerNbVoix.append(',');
//						writerNbVoix.append( this.getDate() );
//						writerNbVoix.append('\n');
					} catch(IOException e) {
					     e.printStackTrace();
					}
				});
			});
			writerRatio.flush();
			writerRatio.close();
			
//			writerNbVoix.flush();
//			writerNbVoix.close();
			
			System.out.println( "Fin export" );
		} catch(IOException e) {
		     e.printStackTrace();
		}
	}
	
	private void exportToGEXF( String fichierCible ) {
		System.out.println( "Début export to GEXF" );
		try {
			FileWriter writerRatio = new FileWriter( fichierCible, true );
			
			//ajout des en-tetes
			writerRatio.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
			writerRatio.append( "<gexf xmlns=\"http://www.gexf.net/1.2draft\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\" version=\"1.2\">\n" );
			writerRatio.append( "\t<meta lastmodifieddate=\"2015-12-04\">\n" );
			writerRatio.append( "\t\t<creator>IC05 - TROTIGNON Hugo, DELESALLE Amaury, BARRIOS Piers</creator>\n" );
			writerRatio.append( "\t\t<description>Un graphe pour analyser les reports de voix depuis 1999</description>\n" );
			writerRatio.append( "\t</meta>\n" );
			writerRatio.append( "\t<graph mode=\"dynamic\" defaultedgetype=\"directed\" timeformat=\"date\">\n" );
			writerRatio.append( "\t\t<nodes>\n" );
			writerRatio.append( "\t\t\t<node id=\"Extreme Droite\" label=\"MNR, EXD, LEXD, BC-EXD\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Front National\" label=\"FN, LFN, BC-FN\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Divers Droite\" label=\"RPF, DVD, DL, MPF, LDVD, PRV, BC-UD, BC-DLF, BC-DVD\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Union de la droite\" label=\"RPR, UMP, MAJ, LDD, LMAJ, M-NC, M, LUMP, BC-UMP\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Centre\" label=\"UDF, MDC, PRG, PREP, RDG, LDR, UDFD, LCMD, LMC, MGC, MODM, CEN, ALLI, NCE, LMDM, LUC, LUDI, BC-MDM, BC-UC, BC-UDI\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Divers\" label=\"DIV, CPNT, LDV, LCP, AUT, LAUT, LDIV, BC-DIV\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Régionalistes\" label=\"REG, LREG\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Ecologistes\" label=\"ECO, VEC, LEC, LVE, LVEC, BC-VEC\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Parti Socialiste\" label=\"SOC, LGA, LSOC, BC-SOC\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Divers Gauche\" label=\"DVG, LDG, LUG, LDVG, BC-UG, BC-RDG, BC-DVG\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Front de gauche\" label=\"COM, LCOM, LCOP, PG, FG, LFG, LPG, BC-FG, BC-PG, BC-COM\"/> \n");
			writerRatio.append( "\t\t\t<node id=\"Extreme Gauche\" label=\"EXG, LEXG, LCR, LO, LXG, BC-EXG\"/> \n");
			writerRatio.append( "\t\t</nodes>\n" );
			writerRatio.append( "\t\t<attributes class=\"edge\" mode=\"dynamic\">\n" );
			writerRatio.append( "\t\t\t<attribute id=\"weight\" title=\"weight\" type=\"double\"/>\n" );
			writerRatio.append( "\t\t</attributes>\n" );
			writerRatio.append( "\t\t<edges>\n" );
			
			//ajout des infos
			this.liste_bureau.forEach( ( id, bureau ) -> {
				bureau.getListe_reports().forEach( ( report ) -> {
					try {
						writerRatio.append( "\t\t\t<edge  source=\"" + report.getNuance_origine() + "\" target=\"" + report.getNuance_cible() + "\" weight=\"" + Double.toString( report.getNb_voix_reportées()) + "\" start=\"" + this.getDateDebut() + "\" fin=\"" + this.getDateFin() + "\"/>\n");
					} catch(IOException e) {
					     e.printStackTrace();
					}
				});
			});
			
			//ajout de la fermeture d'en-tetes
			writerRatio.append( "\t\t</edges>\n" );
			writerRatio.append( "\t</graph>\n" );
			writerRatio.append( "</gexf>\n" );
			
			writerRatio.flush();
			writerRatio.close();
			
		System.out.println( "Fin export" );
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void exportToCSV( String fichierCible, List< bureau > listeBureau ) {
		System.out.println( "Début export" );
		
//		String fileRatio = "/tempo/IC05-workSets/" + fichierCible + "/ratio_" + fichierCible + ".csv";
//		String fileNbVoix = "/tempo/IC05-workSets/" + fichierCible + "/nbVoix_" + fichierCible + ".csv";
		
		try {
			FileWriter writerRatio = new FileWriter( fichierCible, true );
//			FileWriter writerNbVoix = new FileWriter( fileNbVoix );
			
			//ajout des labels colonnes
			writerRatio.append( "Source" );
			writerRatio.append(',');
			writerRatio.append( "Target" );
			writerRatio.append(',');
			writerRatio.append( "pourcentage report" );
			writerRatio.append(',');
			writerRatio.append( "date début" );
			writerRatio.append(',');
			writerRatio.append( "date fin" );
			writerRatio.append('\n');
			
//			writerNbVoix.append( "Source" );
//			writerNbVoix.append(',');
//			writerNbVoix.append( "Target" );
//			writerNbVoix.append(',');
//			writerNbVoix.append( "nombre report" );
//			writerNbVoix.append('\n');
			
			listeBureau.forEach(( bureau ) -> {
				bureau.getListe_reports().forEach( ( report ) -> {
					try {
						writerRatio.append( report.getNuance_origine() );
						writerRatio.append(',');
						writerRatio.append( report.getNuance_cible() );
						writerRatio.append(',');
						writerRatio.append( Double.toString( report.getRatio_report() ));
						writerRatio.append(',');
						writerRatio.append( this.getDateDebut() );
						writerRatio.append(',');
						writerRatio.append( this.getDateFin() );
						writerRatio.append('\n');
						
//						writerNbVoix.append( report.getNuance_origine() );
//						writerNbVoix.append(',');
//						writerNbVoix.append( report.getNuance_cible() );
//						writerNbVoix.append(',');
//						writerNbVoix.append( Integer.toString( report.getNb_voix_reportées() ));
//						writerNbVoix.append(',');
//						writerNbVoix.append( this.getDate() );
//						writerNbVoix.append('\n');
					} catch(IOException e) {
					     e.printStackTrace();
					}
				});
			});
			writerRatio.flush();
			writerRatio.close();
			
//			writerNbVoix.flush();
//			writerNbVoix.close();
			
			System.out.println( "Fin export" );
		} catch(IOException e) {
		     e.printStackTrace();
		}
	}
	
}

