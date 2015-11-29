import java.io.File;
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
				    String[] parts = line.split( ";" );

				    // Debug
				    //System.out.println( line );

				    checkAndAddBureau( parts );

//				    int indexBureau = this.liste_bureau.indexOf( newBureau );
//				    this.liste_bureau.contains( newBureau );
//				    
//				    
//				    if( false == this.liste_bureau.contains( newBureau )) {
//				    	System.out.println( "if" );
//				    	newBureau.addCandidat( newCandidat, Integer.parseInt( parts[0] ), Double.parseDouble( parts[12] ));
//				    	this.liste_bureau.add( newBureau );
//				    }
//				    else
//				    	{ System.out.println( "else" );
//				    	this.liste_bureau.get( indexBureau ).addCandidat( newCandidat, Integer.parseInt( parts[0] ), Double.parseDouble( parts[12] )); }
			
				    if( -1 == this.liste_nuances.indexOf( parts[11] ) )
				    	{ this.liste_nuances.add( parts[11] ); }
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
		System.out.println( "\ncheck" );
		// TODO Auto-generated method stub
		bureau newBureau = new bureau( sBureau );
	    candidat newCandidat = new candidat( sBureau[8], sBureau[11] );
	    Boolean isBureauPresent = false;
	    
	    if( liste_bureau.isEmpty() ){
	    	System.out.println( "empty" );
	    	newBureau.addCandidat( newCandidat, Integer.parseInt( sBureau[0] ), Double.parseDouble( sBureau[12] ));
			liste_bureau.add( newBureau );
	    }
	    else {
	    	liste_bureau.forEach( (bureau) -> {
	    		if( bureau.getId().equals( newBureau.getId() )) {
	    			System.out.println( "bureau present" );
	    			isBureauPresent = true;
	    			bureau.addCandidat(newCandidat, Integer.parseInt( sBureau[0] ), Double.parseDouble( sBureau[12] ));
	    		}
	    	});
	    	
	    	System.out.println( "not empty" );
	    	for( int i = 0; i < liste_bureau.size(); i++){
	    		System.out.println( "for" );
	    		System.out.println( "id liste = " + liste_bureau.get( i ).getId() );
	    		System.out.println( "id bureau = " + newBureau.getId() );
	    		
	    		if( liste_bureau.get( i ).getId().equals( newBureau.getId() ) )
	    			{ 
	    			System.out.println( "bureau present" );
	    			isBureauPresent = true;
	    			liste_bureau.get( i ).addCandidat(newCandidat, Integer.parseInt( sBureau[0] ), Double.parseDouble( sBureau[12] ));
	    			
	    			}
	    		
	    	}
	    	
	    	if( false == isBureauPresent ) {
	    		System.out.println( "bureau absent" );
	    		newBureau.addCandidat( newCandidat, Integer.parseInt( sBureau[0] ), Double.parseDouble( sBureau[12] ));
		    	this.liste_bureau.add( newBureau );
	    	}
	    	
//		    int indexBureau = this.liste_bureau.indexOf( newBureau );
//		    if( -1 == indexBureau ) {
//		    	System.out.println( "if" );
//		    	newBureau.addCandidat( newCandidat, Integer.parseInt( sBureau[0] ), Double.parseDouble( sBureau[12] ));
//		    	this.liste_bureau.add( newBureau );
//		    }
//		    else {
//		    	System.out.println( "else" );
//		    	this.liste_bureau.get( indexBureau ).addCandidat( newCandidat, Integer.parseInt( sBureau[0] ), Double.parseDouble( sBureau[12] ));
//		    }
	    }
	    System.out.println( "end-check" );
	    
	    
//	    liste_bureau.forEach( (bureau) -> {
//	    	System.out.println( "itération sur la liste" );
//			String pwet = Integer.toString( bureau.getCode_dpt() ) + Integer.toString( bureau.getCode_commune() ) + Integer.toString( bureau.getNum_bureau() );
//			if( ( sBureau[1] + sBureau[2] + sBureau[4] )  == pwet)
//				{ 
//				System.out.println( "bureau présent" );
//				bureau.addCandidat(newCandidat, Integer.parseInt( sBureau[0] ), Double.parseDouble( sBureau[12] )); }
//			else {
//				System.out.println( "new bureau" );
//				newBureau.addCandidat( newCandidat, Integer.parseInt( sBureau[0] ), Double.parseDouble( sBureau[12] ));
//				liste_bureau.add( newBureau );
//			}
//		});
	}

	private String nom;
	Map< String, bureau > liste_bureau = new HashMap< String, bureau >();

	private List< String > liste_nuances = new ArrayList< String >();
	private Map< String, Map< String, Integer >> liste_reports; // nuance origine, nuances cibles, % de report
	
	public static void main( String args[] ) {
		election myElection = new election( "toto", "/tempo/workSets/LG07-bureaux/10-01-001-0001.txt" );
		myElection.liste_bureau.forEach((id, bureau) -> bureau.calculReportV1() ); 
	}
}

