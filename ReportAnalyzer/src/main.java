import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main {
	static Map< String, report > daListeReports = new HashMap< String, report >();
	static Map< candidat, List< String >> candidatsT2 = new HashMap< candidat, List< String >>();
	static election predictedElection;
	static String dptACA = "08 10 51 52 67 68 54 55 57 88";				//alsace champagne ardenne lorraine
	static String dptALP = "16 17 19 23 24 33 40 47 64 79 86 87";		//aquitaine limousin poitou charente
	static String dptARA = "01 03 07 15 26 38 42 43 63 69 73 74";		//auvergne rhone alpes
	static String dptBFC = "21 25 39 58 70 71 89 90";					//bourgogne franche comté
	static String dptBRE = "22 29 35 56";								//bretagne
	static String dptCVL = "18 28 36 37 41 45";							//centre val de loire
	static String dptIDF = "75 77 78 91 92 93 94 95";					//ile de france
	static String dptLRM = "09 11 12 30 31 32 34 46 48 65 66 81 82";	//languedoc roussillon midi pyrénées
	static String dptNPC = "60 02 59 62 80";							//nord picardie
	static String dptNOR = "14 27 50 61 76";							//normandie
	static String dptPDL = "44 49 53 72 85";							//pays de la loire
	static String dptPAC = "04 05 06 13 83 84";							//provence alpes cote d'azur
	static String dptCOR = "2A 2B";										//corse
	

	public static void main( String args[] ) {

//		election CN01 = new election( "CN01", "/tempo/IC05-workSets/CN01/CN01.txt", "2001-01-01", "2002-01-01" );
//		election CN04 = new election( "CN04", "/tempo/IC05-workSets/CN04/CN04.txt", "2004-01-01", "2004-06-30" );
//		election CN08 = new election( "CN08", "/tempo/IC05-workSets/CN08/CN08.txt", "2008-01-01", "2008-06-30" );
//		election CN11 = new election( "CN11", "/tempo/IC05-workSets/CN11/CN11.txt", "2011-01-01", "2012-01-01" );
//		election DP15 = new election( "DP15", "/tempo/workspace-IC05/IC05-workSets/DP15/DP15.txt", "2015-01-01", "2016-01-01" );
//		election LG02 = new election( "LG02", "/tempo/IC05-workSets/LG02/LG02.txt", "2002-01-01", "2003-01-01" );
//		election LG07 = new election( "LG07", "/tempo/IC05-workSets/LG07/LG07.txt", "2007-01-01", "2008-01-01" );
		election LG12 = new election( "LG12", "/tempo/workspace-IC05/IC05-workSets/LG12/LG12.txt", "2012-01-01", "2013-01-01" );
//		election MN08 = new election( "MN08", "/tempo/IC05-workSets/MN08/MN08.txt", "2008-07-01", "2009-01-01" );
//		election MN14 = new election( "MN14", "/tempo/IC05-workSets/MN14/MN14.txt", "2014-01-01", "2015-01-01" );
//		election RG04 = new election( "RG04", "/tempo/workspace-IC05/IC05-workSets/RG04/RG04.txt", "2004-07-01", "2005-01-01" );
//		election RG042 = new election( "RG042", "/tempo/workspace-IC05/IC05-workSets/RG10/RG10.txt", "2010-01-01", "2011-01-01" );
//		election RG10 = new election( "RG10", "/tempo/IC05-workSets/RG10/RG10.txt", "2010-01-01", "2011-01-01" );
		

		List< election > daList = new ArrayList< election >();
//		election LG02 = new election( "LG02", "/tempo/IC05-workSets/LG02/LG02.txt", "2002-01-01", "2003-01-01" );
//		daList.add( LG02 );
//		election LG07 = new election( "LG07", "/tempo/IC05-workSets/LG07/LG07.txt", "2007-01-01", "2008-01-01" );
//		daList.add( LG07 );
//		election LG12 = new election( "LG12", "/tempo/IC05-workSets/LG12/LG12.txt", "2012-01-01", "2013-01-01" );
//		daList.add( LG12 );
		
//		daList.add( CN01 );
//		daList.add( CN04 );
//		daList.add( CN08 );
//		daList.add( CN11 );
//		daList.add( DP15 );
//		daList.add( LG02 );
//		daList.add( LG07 );
		daList.add( LG12 );
//		daList.add( MN08 );
//		daList.add( MN14 );
//		daList.add( RG04 );
//		daList.add( RG10 );

		//		List< bureau > daListeDpt = new ArrayList< bureau >();
		
		estimationT2( "RG15", "/tempo/workspace-IC05/IC05-workSets/Estimated/RG15_T1.txt", "2015-12-06", "2015-12-13" );
//		daList.add( predictedElection );
//
//		
//		predictedElection.createGEXF( daList, "/tempo/workspace-IC05/IC05-workSets/Estimated/test.gexf" );
		
		
//		election.createGEXF( daList, "/tempo/workspace-IC05/Estimated/LG12.gexf" );
//		LG12.exportRatioReportToCSV( "/tempo/workspace-IC05/IC05-workSets/Estimated/reportsLG12.csv" );
		
//		RG042.sommeResultats( "/tempo/IC05-workSets/Estimated/listReports.csv" );
//		RG042.exportEstimationResultatsToCSV( "/tempo/IC05-workSets/Estimated/estimations.csv" );
	}
	
	public static void estimationT2( String nomElection, String nomFichier, String dateD, String dateF ){
		predictedElection = new election( nomElection, nomFichier, dateD, dateF );
		createListeT2();
		predictedElection.creerElection();
		predictedElection.addCandidatsSecondTour( candidatsT2 );
		predictedElection.sommeReports();
		
		predictedElection.sommePredictedResultats( "/tempo/workspace-IC05/IC05-workSets/Estimated/reports.csv" );
		
		predictedElection.exportEstimationResultatsToCSV( "/tempo/workspace-IC05/IC05-workSets/Estimated/test.txt" );
	}
	
	public static void addCandidatRegion( String idCandidat, String nuance, String departements ){
		String[] parts = departements.split( "\\s" );
		List< String > listeDpt = new ArrayList< String >();
		
		for( String toto : parts ){
			listeDpt.add( toto );
		}
		candidat newCandidat = new candidat( idCandidat, nuance );
		candidatsT2.put( newCandidat, listeDpt );
	}
	
	public static void createListeT2(){
		addCandidatRegion( "7", "Parti Socialiste", dptACA );
		addCandidatRegion( "6", "Front National", dptACA );
		addCandidatRegion( "8", "Union de la Droite", dptACA );
		
		addCandidatRegion( "12", "Parti Socialiste", dptALP );
		addCandidatRegion( "7", "Union de la Droite", dptALP );
		addCandidatRegion( "3", "Front National", dptALP );
		
		addCandidatRegion( "7", "Front National", dptARA );
		addCandidatRegion( "9", "Union de la Droite", dptARA );
		addCandidatRegion( "12", "Parti Socialiste", dptARA );
		
		addCandidatRegion( "6", "Front National", dptBFC );
		addCandidatRegion( "8", "Union de la Droite", dptBFC );
		addCandidatRegion( "2", "Parti Socialiste", dptBFC );
		
		addCandidatRegion( "6", "Parti Socialiste", dptBRE );
		addCandidatRegion( "4", "Union de la Droite", dptBRE );
		addCandidatRegion( "2", "Front National", dptBRE );
		
		addCandidatRegion( "10", "Parti Socialiste", dptCVL );
		addCandidatRegion( "7", "Union de la Droite", dptCVL );
		addCandidatRegion( "4", "Front National", dptCVL );
		
		addCandidatRegion( "15", "Parti Socialiste", dptIDF );
		addCandidatRegion( "10", "Union de la Droite", dptIDF );
		addCandidatRegion( "7", "Front National", dptIDF );
		
		addCandidatRegion( "14", "Parti Socialiste", dptLRM );
		addCandidatRegion( "9", "Union de la Droite", dptLRM );
		addCandidatRegion( "4", "Front National", dptLRM );
		
		addCandidatRegion( "9", "Union de la Droite", dptNPC );
		addCandidatRegion( "3", "Front National", dptNPC );
		
		addCandidatRegion( "11", "Parti Socialiste", dptNOR );
		addCandidatRegion( "7", "Union de la Droite", dptNOR );
		addCandidatRegion( "3", "Front National", dptNOR );
		
		addCandidatRegion( "13", "Parti Socialiste", dptPDL );
		addCandidatRegion( "5", "Union de la Droite", dptPDL );
		addCandidatRegion( "7", "Front National", dptPDL );
		
		addCandidatRegion( "8", "Union de la Droite", dptPAC );
		addCandidatRegion( "5", "Front National", dptPAC );
		
		addCandidatRegion( "16", "Divers Gauche", dptCOR );
		addCandidatRegion( "15", "Union de la Droite", dptCOR );
		addCandidatRegion( "17", "Regionalistes", dptCOR );
		addCandidatRegion( "2", "Front National", dptCOR );
	}
}
