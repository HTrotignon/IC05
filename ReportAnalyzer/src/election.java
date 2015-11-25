import java.io.File;
import java.io.FileReader;
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
				 
				    System.out.println( line );
					//faites ici votre traitement
				    
				    List< String > newLine = new ArrayList< String >();
				    				    
				    int indexBureau = this.liste_bureau.indexOf( newLine );
				    
				    if( -1 != indexBureau ){
				    	this.liste_bureau.get( indexBureau ).addCandidat();
				    	
				    	
				    }
				    
				}
			} finally {
			// dans tous les cas, on ferme nos flux
				scanner.close();
			}
		} catch( IOException ioe ) {
		// erreur de fermeture des flux
			System.out.println( "Erreur --" + ioe.toString() );
		}
		//////////////////////////////////////////////
		// on donne un fichier résultat				//
		// extraction ligne par ligne				//
		// check si un bureau est dans la liste		//
		// 		ajout du candidat + résultat		//
		// sinon création d'un nouveau bureau		//
		//		ajout candidat + résultat			//
		// 											//
		//////////////////////////////////////////////
		
	}

	private String nom;
	private List< bureau > liste_bureau = new ArrayList< bureau >();
	private Map< String, String > liste_nuances;

	public void getReportVoix(){
	}
	
	public bureau getNomBureau(){
		return null;
	}
	

}

