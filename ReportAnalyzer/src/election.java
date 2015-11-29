import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class election {
	
	public election( String nomElection, String nomFichier ) {
		this.nom = nomElection;
		try {
			Scanner scanner = new Scanner( new File( nomFichier ));
			try {
				// Lecture du fichier ligne par ligne. Cette boucle se termine
				// quand la méthode retourne la valeur null.
				while( scanner.hasNextLine() ) {
				    String line = scanner.nextLine();
				    String[] parts = line.split( "\\s" );

				    // Debug
				    //System.out.println( line );

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
	    	}
	    	else if(( 2 == Integer.parseInt( sBureau[1] )) & ( 0 == liste_bureau.get( sBureau[0] ).getNb_votants_T2() ))
	    	{
	    		liste_bureau.get( sBureau[0] ).setNb_votants_T2( Integer.parseInt( sBureau[6] ));
	    		liste_bureau.get( sBureau[0] ).setNb_exprimés_T2( Integer.parseInt( sBureau[7] ));
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

	private String nom;
	Map< String, bureau > liste_bureau = new HashMap< String, bureau >();

	private List< String > liste_nuances = new ArrayList< String >();
	//private Map< String, Map< String, Integer >> liste_reports; // nuance origine, nuances cibles, % de report
	
	private void exportToCSV( String fichierCible ) {
		System.out.println( "Début export" );
		
		String fileRatio = "/tempo/workSets/ratio_" + fichierCible;
		String fileNbVoix = "/tempo/workSets/nbVoix_" + fichierCible;
		
		try {
			FileWriter writerRatio = new FileWriter( fileRatio );
			FileWriter writerNbVoix = new FileWriter( fileNbVoix );
			
			writerRatio.append( "Source" );
			writerRatio.append(',');
			writerRatio.append( "Target" );
			writerRatio.append(',');
			writerRatio.append( "pourcentage report" );
			writerRatio.append('\n');
			
			this.liste_bureau.forEach( ( id, bureau ) -> {
				bureau.getListe_reports().forEach( ( report ) -> {
					try {
						writerRatio.append( report.getNuance_origine() );
						writerRatio.append(',');
						writerRatio.append( report.getNuance_cible() );
						writerRatio.append(',');
						writerRatio.append( Double.toString( report.getRatio_report() ));
						writerRatio.append('\n');
						
						writerNbVoix.append( report.getNuance_origine() );
						writerNbVoix.append(',');
						writerNbVoix.append( report.getNuance_cible() );
						writerNbVoix.append(',');
						writerNbVoix.append( Integer.toString( report.getNb_voix_reportées() ));
						writerNbVoix.append('\n');
					} catch(IOException e) {
					     e.printStackTrace();
					}
				});
			});
			writerRatio.flush();
			writerRatio.close();
			
			writerNbVoix.flush();
			writerNbVoix.close();
			
			System.out.println( "Fin export" );
		} catch(IOException e) {
		     e.printStackTrace();
		}
	}
	
	public static void main( String args[] ) {
		election myElection = new election( "toto", "/tempo/workSets/LG07.txt" );
		myElection.liste_bureau.forEach((id, bureau) -> bureau.calculReportV1() );
		myElection.exportToCSV( "1st_try.csv" );
	}
}

