import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
				    String[] parts = line.split( ";" );
				    bureau newBureau = new bureau( parts );
				    
				    // Debug
				    System.out.println( line );
				    				    
				    int indexBureau = this.liste_bureau.indexOf( Integer.parseInt( parts[1] ) );
				    
				    if( -1 != indexBureau ){
				    	candidat newCandidat = new candidat( parts[8], parts[11] );
				    	this.liste_bureau.get( indexBureau ).addCandidat( newCandidat, parts[0] );
				    }
				    else {
				    	this.liste_bureau.add( newBureau );
				    }
				    
//				    if( false != this.liste_nuances.containsKey( parts[11] )){
//				    	this.liste_nuances.put( parts[11], value );
//				    }
				    
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

	private String nom;
	private List< bureau > liste_bureau = new ArrayList< bureau >();
	private 
	Map< String, Map< String, Integer >> liste_reports; // nuance origine, nuance cible, % de report
}

