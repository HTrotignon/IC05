import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jdom2.*;
import org.jdom2.output.*;

public class election {
	
	public election( String nomElection, String nomFichier, String dateD, String dateF ) {
		this.nom = nomElection;
		this.nomFichier = nomFichier;
		this.date_début = dateD;
		this.date_fin = dateF;
	}

	private void creerElection(){
		try {
			Scanner scanner = new Scanner( new File( this.nomFichier ));
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
				    // séparateur "\\t"	: MN14, 
				    String[] parts = {"",""};
				    switch( this.nom ) {
				    	case "CN01" :
				    	case "CN04" :
				    	case "CN08" :
				    	case "CN11" :
				    	case "CP15" :
				    	case "LG02" :
				    	case "LG07" :
				    	case "LG12" :
				    	case "RG04" :
				    	case "RG10" :
				    		parts = line.split( "\\s" );
				    		break;
				    	case "MN08" :
				    		parts = line.split( ";" );
				    		break;
				    	case "MN14" :
				    		parts = line.split( "\\t" );
				    		break;
				    }
				    				    
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
	};

	public void addCandidatsSecondTour( Map< candidat, List< String >> Candidats ) {
		Candidats.forEach( (candidat, listeCommunes ) -> {
			listeCommunes.forEach( (commune) -> {
				this.liste_bureau.forEach( (id, bureau) -> {
					if( bureau.getCode_dpt().equals( commune )){
						bureau.addCandidat( candidat, 2, 0);
					}
				});
			});
		});
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
	Map< String, candidat > estimations_candidats = new HashMap< String, candidat >();
	private List< String > liste_nuances = new ArrayList< String >();
	private String nom;
	private String nomFichier;
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
	
	public void sommeResultats( String fichierReports ){
		this.liste_bureau.forEach( ( id, bureau ) -> {
			bureau.calculEstimationReports( bureau.lectureEstimationReports( fichierReports ));
			bureau.getListe_candidats_T2().forEach( ( candidat, nb_voix ) -> {
				if( this.estimations_candidats.containsKey( candidat.getId() )){
					this.estimations_candidats.get( candidat.getId() ).setScore( estimations_candidats.get( candidat.getId()).getScore() + nb_voix ); 
				}
				else {
					this.estimations_candidats.put( Integer.toString( candidat.getId() ), candidat );
				}
			});
		});
	}
	
	public void sommeReports() {
		this.liste_bureau.forEach( ( id, bureau ) -> {
			bureau.calculReportV1();
			bureau.getListe_reports().forEach( ( report ) -> {
				if( this.liste_reports.containsKey( report.getId() )){
					this.liste_reports.get( report.getId() ).setNb_voix_reportées( liste_reports.get( report.getId() ).getNb_voix_reportées() + report.getNb_voix_reportées());
					this.liste_reports.get( report.getId() ).setRatio_report( ( liste_reports.get( report.getId() ).getRatio_report() + report.getRatio_report())/2 );
				}
				else {
					this.liste_reports.put( report.getId(), report );
				}
			});;
		});
	}	

	public void getBureauxDpt( List< String > numDpt ) {
		System.out.println( "getBureauDpt :: BEGIN" );
		Map< String, bureau > listeDpt = new HashMap< String, bureau >();

		this.liste_bureau.forEach( ( id, bureau ) -> {
			numDpt.forEach( (num) -> {
				//				System.out.println( num );
				if( num.equals( bureau.getCode_dpt() )) {
//					System.out.println( bureau.getCode_dpt() );
					listeDpt.put( id, bureau );
				}
			});
		});

		this.liste_bureau = listeDpt;
		//listeDpt.forEach( (bureau) -> bureau.calculReportV1());


		System.out.println( "getBureauDpt :: END" );
	}

	public void exportEstimationResultatsToCSV( String fichierCible ){
		System.out.println( "Début export" );

		try {
			FileWriter writerRatio = new FileWriter( fichierCible );

			//ajout des labels colonnes
			writerRatio.append( "nuance politique" );
			writerRatio.append(',');
			writerRatio.append( "nombre de voix" );
			writerRatio.append('\n');

			this.estimations_candidats.forEach( ( id, candidat ) -> {
				try {
					writerRatio.append( candidat.getNuance() );
					writerRatio.append(',');
					writerRatio.append( Integer.toString( candidat.getScore() ));
					writerRatio.append('\n');
				} catch(IOException e) {
					e.printStackTrace();
				}
			});
			writerRatio.flush();
			writerRatio.close();

			System.out.println( "Fin export" );
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void exportRatioReportToCSV( String fichierCible, List< election > listeElection ) {
		System.out.println( "Début export" );
		
		try {
			FileWriter writerRatio = new FileWriter( fichierCible );
//			FileWriter writerNbVoix = new FileWriter( fileNbVoix );
			
			//ajout des labels colonnes
			writerRatio.append( "Source" );
			writerRatio.append(',');
			writerRatio.append( "Target" );
			writerRatio.append(',');
			writerRatio.append( "pourcentage report" );
			writerRatio.append('\n');
			
			listeElection.forEach( (election) -> {
			election.liste_reports.forEach( ( id, report ) -> {
					try {
						writerRatio.append( report.getNuance_origine() );
						writerRatio.append(',');
						writerRatio.append( report.getNuance_cible() );
						writerRatio.append(',');
						writerRatio.append( Double.toString( report.getRatio_report() ));
						writerRatio.append('\n');
						
					} catch(IOException e) {
					     e.printStackTrace();
					}
				});
			});
			writerRatio.flush();
			writerRatio.close();
			
			System.out.println( "Fin export" );
		} catch(IOException e) {
		     e.printStackTrace();
		}
	}
	
	private static Element createNode( String id, String label, String r, String g, String b ){		
		Element myElement = new Element( "node" );
		myElement.setAttribute( "id",  id );
		myElement.setAttribute( "label",  label );
		
		Element color = new Element( "color" );
		myElement.addContent( color );
		color.setAttribute( "r", r );
		color.setAttribute( "g", g );
		color.setAttribute( "b", b );
		
		return myElement;
	}
	
	public static void createGEXF( List< election > listeElection , String fichierCible, String sNumDpt ){
			Map< String, Element > liensAjoutés = new HashMap< String, Element >();
			Map< String, Element > spellsAjoutés = new HashMap< String, Element >();
			List< String > iNumDpt = new ArrayList< String >();

			String[] parts = sNumDpt.split( "\\s" );
			for( String  toto : parts){
//				System.out.println( toto );
				iNumDpt.add( toto );
			}
			
			Element racine = exportHeaderGEXF();
			Element graph = racine.getChild( "graph" );
			Element edges = new Element( "edges" );
			graph.addContent( edges );
			
			listeElection.forEach( (election) -> {
				election.creerElection();
				if ( !iNumDpt.equals( "" )){
					election.getBureauxDpt( iNumDpt );
				}
				election.sommeReports();
				election.liste_reports.forEach( ( id, report ) -> {
					election.addEdge( edges, report, liensAjoutés, spellsAjoutés);
				});
			});
			
			saveGEXF( fichierCible, racine );
		
	}
	
	public static void createGEXF( List< election > listeElection , String fichierCible ){
		
	}
	
	public static void saveGEXF( String fichierCible, Element racine ) {
		try
		   {
		      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		      org.jdom2.Document document = new Document(racine);
		      
		      sortie.output( document, new FileOutputStream( fichierCible ));
		   }
		   catch (java.io.IOException e){}
	}
	
	public static Element exportHeaderGEXF() {
		Namespace ns = Namespace.getNamespace("http://www.gexf.net/1.2draft");
		Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		Namespace viz = Namespace.getNamespace("viz", "http://www.gexf.net/1.1draft/viz");
		
		// la racine
		Element racine = new Element( "gexf", ns );
		racine.addNamespaceDeclaration( xsi );
		racine.addNamespaceDeclaration( viz );
		racine.setAttribute( "schemaLocation","http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd", xsi );
		racine.setAttribute( "version","1.2" );
		
		// meta
		Element meta = new Element("meta");
		racine.addContent( meta );
		meta.setAttribute( "lastmodifieddate" , "2015-12-04" );

		Element creator = new Element("creator");
		meta.addContent( creator );
		creator.setText( "IC05 - TROTIGNON Hugo, DELESALLE Amaury, BARRIOS Piers" );		
		Element description = new Element("description");
		meta.addContent( description );
		description.setText( "Un graphe pour analyser les reports de voix depuis 1999" );

		// graph
		Element graph = new Element("graph");
		racine.addContent( graph );
		graph.setAttribute( "mode" , "dynamic" );
		graph.setAttribute( "defaultedgetype" , "directed" );
		graph.setAttribute( "timeformat" , "date" );
		graph.setAttribute( "start", "2000-01-01" );
		graph.setAttribute( "end", "2016-12-31" );

		// nodes
		Element nodes = new Element("nodes");
		graph.addContent( nodes );
		nodes.addContent( createNode( "Extreme Droite", "A", "0", "0", "179" )); //MNR, EXD, LEXD, BC-EXD
		nodes.addContent( createNode( "Front National", "B", "0", "0", "255" )); //FN, LFN, BC-FN
		nodes.addContent( createNode( "Divers Droite", "C", "117", "119", "255" )); //RPF, DVD, DL, MPF, LDVD, PRV, BC-UD, BC-DLF, BC-DVD
		nodes.addContent( createNode( "Union de la Droite", "D", "102", "230", "255" )); //RPR, UMP, MAJ, LDD, LMAJ, M-NC, M, LUMP, BC-UMP
		nodes.addContent( createNode( "Centre", "E", "255", "179", "17" )); //UDF, MDC, PRG, PREP, RDG, LDR, UDFD, LCMD, LMC, MGC, MODM, CEN, ALLI, NCE, LMDM, LUC, LUDI, BC-MDM, BC-UC, BC-UDI
		nodes.addContent( createNode( "Divers", "F", "199", "195", "199" )); //DIV, CPNT, LDV, LCP, AUT, LAUT, LDIV, BC-DIV
		nodes.addContent( createNode( "Regionalistes", "G", "179", "107", "59" )); //REG, LREG
		nodes.addContent( createNode( "Ecologistes", "H", "68", "207", "39" )); //ECO, VEC, LEC, LVE, LVEC, BC-VEC
		nodes.addContent( createNode( "Parti Socialiste", "I", "255", "89", "158" )); //SOC, LGA, LSOC, BC-SOC
		nodes.addContent( createNode( "Divers Gauche", "J", "255", "155", "208" )); //DVG, LDG, LUG, LDVG, BC-UG, BC-RDG, BC-DVG
		nodes.addContent( createNode( "Front de Gauche", "K", "255", "128", "128" )); //COM, LCOM, LCOP, PG, FG, LFG, LPG, BC-FG, BC-PG, BC-COM
		nodes.addContent( createNode( "Extreme Gauche", "L", "255", "0", "0" )); //EXG, LEXG, LCR, LO, LXG, BC-EXG
		nodes.addContent( createNode( "Blancs et Nuls", "M", "217", "217", "217" )); //Blancs et Nuls
		nodes.addContent( createNode( "Abstention", "N", "59", "59", "59" )); //Abstention
		
		//Attributes
		Element attributes = new Element( "attributes" );
		graph.addContent( attributes );
		attributes.setAttribute( "class", "edge" );
		attributes.setAttribute( "mode", "dynamic" );

		Element attribute = new Element( "attribute" );
		attributes.addContent( attribute );
		attribute.setAttribute( "id", "weight" );
		attribute.setAttribute( "title", "Weight" );
		attribute.setAttribute( "type", "float" );

		//		org.jdom2.Document document = new Document(racine);
		//		   try
		//		   {
		//		      //On utilise ici un affichage classique avec getPrettyFormat()
		//		      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		//		      sortie.output(document, System.out);
		//		   }
		//		   catch (java.io.IOException e){}
		return racine;
	}

	public void addEdge( Element edges, report myReport, Map< String, Element > liensAjoutés, Map< String, Element > spellsAjoutés ){
		if( 0 != myReport.getNb_voix_reportées() ) {
			Element attvalues;
			Element spells;
			
			if( liensAjoutés.containsKey( myReport.getId()) ) {
				attvalues = liensAjoutés.get( myReport.getId() );
				spells = spellsAjoutés.get( myReport.getId() );
			}
			else {
				Element edge = new Element( "edge" );
				edges.addContent( edge );
				edge.setAttribute( "source", myReport.getNuance_origine() );
				edge.setAttribute( "target", myReport.getNuance_cible() );
	//			edge.setAttribute( "start", "2000-01-01" );
	//			edge.setAttribute( "end", "2016-12-31" );
	
				attvalues = new Element( "attvalues" );
				edge.addContent( attvalues );
				liensAjoutés.put( myReport.getId(), attvalues );
				
				spells = new Element( "spells" );
				edge.addContent( spells );
				spellsAjoutés.put( myReport.getId(), spells );
			}
			
			Element attvalue = new Element( "attvalue" );
			attvalues.addContent( attvalue );
			attvalue.setAttribute( "for", "weight" );
			attvalue.setAttribute( "value", Integer.toString( myReport.getNb_voix_reportées()) );
			attvalue.setAttribute( "start", this.getDateDebut() );
			attvalue.setAttribute( "end", this.getDateFin() );
			
			Element spell = new Element( "spell" );
			spells.addContent( spell );
			spell.setAttribute( "start", this.getDateDebut() );
			spell.setAttribute( "end", this.getDateFin() );
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

