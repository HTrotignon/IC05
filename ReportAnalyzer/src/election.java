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
				    // penser Ã  modifier le séparateur en fonction du fichier Ã  traiter //
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
	    	// on met Ã  jour le nombre de votants dans le bureau. Réalisé 1 fois par bureau
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
	
	private List< bureau > getBureauxDpt( String numDpt ){
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
			writerRatio.append( "\t\t<edges>\n" );
			
			//ajout des infos
			this.liste_bureau.forEach( ( id, bureau ) -> {
				bureau.getListe_reports().forEach( ( report ) -> {
					try {
						writerRatio.append( "\t\t\t<edge  source=\"" + report.getNuance_origine() + "\" target=\"" + report.getNuance_cible() + "\" weight=\"" + Double.toString( report.getRatio_report()) + "\" start=\"" + this.getDateDebut() + "\" fin=\"" + this.getDateFin() + "\"/>\n");
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

	private void exportToCSV( String fichierCible, List< bureau > listeBureau ) {
		System.out.println( "DÃ©but export" );
		
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
			writerRatio.append( "date dÃ©but" );
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
//						writerNbVoix.append( Integer.toString( report.getNb_voix_reportÃ©es() ));
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
	
	public static void main( String args[] ) {
		
		election CN01 = new election( "CN01", "C:\\Users\\Piers Barrios\\Dropbox\\IC05 - Reports de voix\\BDD Clean\\CN01.txt", "2001-01-01", "2001-12-31" );
//		election CN04 = new election( "CN04", "/tempo/IC05-workSets/CN04/CN04.txt", "2004-01-01", "2004-12-31" );
//		election CN08 = new election( "CN08", "/tempo/IC05-workSets/CN08/CN08.txt", "2008-01-01", "2008-12-31" );
//		election CN11 = new election( "CN11", "/tempo/IC05-workSets/CN11/CN11.txt", "2011-01-01", "2011-12-31" );
//		election DP15 = new election( "DP15", "/tempo/IC05-workSets/DP15/DP15.txt", "2015" );
//		election LG02 = new election( "LG02", "/tempo/IC05-workSets/LG02/LG02.txt", "2002" );
//		election LG07 = new election( "LG07", "/tempo/IC05-workSets/LG07/LG07.txt", "2007" );
//		election LG12 = new election( "LG12", "/tempo/IC05-workSets/LG12/LG12.txt", "2012" );
//		election MN14 = new election( "MN14", "/tempo/IC05-workSets/MN14/MN14.txt", "2014" );
//		election RG04 = new election( "RG04", "/tempo/IC05-workSets/RG04/RG04.txt", "2004" );
//		election RG10 = new election( "RG10", "/tempo/IC05-workSets/RG10/RG10.txt", "2010" );
		
		List< election > daList = new ArrayList< election >();
		
		daList.add( CN01 );
//		daList.add( CN04 );
//		daList.add( CN08 );
//		daList.add( CN11 );
//		daList.add( DP15 );
//		daList.add( LG02 );
//		daList.add( LG07 );
//		daList.add( LG12 );
//		daList.add( MN14 );
//		daList.add( RG04 );
//		daList.add( RG10 );
		
		List< bureau > daListeDpt = new ArrayList< bureau >();
		
		daList.forEach( (election) -> {

			election.liste_bureau.forEach((id, bureau) -> bureau.calculReportV1() );
			//election.exportToCSV( "C:\\Piers\\UV UTC\\IC05\\CSV\\CN01.csv" );
			election.exportToGEXF("C:\\Piers\\UV UTC\\IC05\\GEXF\\CN01.gexf");
			
//			daListeDpt.addAll( election.getBureauxDpt( "60" ));
//			election.liste_bureau.forEach((id, bureau) -> bureau.calculReportV1() );
//			election.exportToCSV( "/tempo/IC05-workSets/CN04.csv" );
//			election.exportToCSV( "/tempo/IC05-workSets/CN04_60.csv", daListeDpt );
		});
		
		
		
	}
}

