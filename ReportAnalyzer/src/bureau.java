import java.util.List;
import java.util.Map;


public class bureau {
	private int code_dpt;
	private int code_commune;
	private int num_bureau;
	
	private int bn_inscrits;
	private int nb_votants_T1;
	private int nb_exprimés_T1;
	
	private int nb_votants_T2;
	private int nb_exprimés_T2;
	
	
	Map< candidat, Long > liste_candidats_tour1;
	Map< candidat, Long > liste_candidats_tour2;
	
	public void addCandidat(){
		
	}
	
}
 