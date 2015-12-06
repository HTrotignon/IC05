import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class main {
	static Map< String, report > daListeReports = new HashMap< String, report >();

	public static void main( String args[] ) {

//		election CN01 = new election( "CN01", "/tempo/IC05-workSets/CN01/CN01.txt", "2001-01-01", "2002-01-01" );
//		election CN04 = new election( "CN04", "/tempo/IC05-workSets/CN04/CN04.txt", "2004-01-01", "2004-06-30" );
//		election CN08 = new election( "CN08", "/tempo/IC05-workSets/CN08/CN08.txt", "2008-01-01", "2008-06-30" );
//		election CN11 = new election( "CN11", "/tempo/IC05-workSets/CN11/CN11.txt", "2011-01-01", "2012-01-01" );
		election DP15 = new election( "DP15", "/tempo/IC05-workSets/DP15/DP15.txt", "2015-01-01", "2016-01-01" );
		election LG02 = new election( "LG02", "/tempo/IC05-workSets/LG02/LG02.txt", "2002-01-01", "2003-01-01" );
		election LG07 = new election( "LG07", "/tempo/IC05-workSets/LG07/LG07.txt", "2007-01-01", "2008-01-01" );
//		election LG12 = new election( "LG12", "/tempo/IC05-workSets/LG12/LG12.txt", "2012-01-01", "2013-01-01" );
//		election MN14 = new election( "MN14", "/tempo/IC05-workSets/MN14/MN14.txt", "2014-01-01", "2015-01-01" );
//		election RG04 = new election( "RG04", "/tempo/IC05-workSets/RG04/RG04.txt", "2004-07-01", "2005-01-01" );
//		election RG10 = new election( "RG10", "/tempo/IC05-workSets/RG10/RG10.txt", "2010-01-01", "2011-01-01" );
//		election MN08 = new election( "RG10", "/tempo/IC05-workSets/MN08/MN08.txt", "2008-07-01", "2009-01-01" );

		List< election > daList = new ArrayList< election >();

//		daList.add( CN01 );
//		daList.add( CN04 );
//		daList.add( CN08 );
//		daList.add( CN11 );
		daList.add( DP15 );
		daList.add( LG02 );
		daList.add( LG07 );
//		daList.add( LG12 );
//		daList.add( MN14 );
//		daList.add( MN08 );
//		daList.add( RG04 );
//		daList.add( RG10 );

		//		List< bureau > daListeDpt = new ArrayList< bureau >();

		
		election.createGEXF( daList, "/tempo/IC05-workSets/communales.gexf" );
		
		//election.exportToCSV( "C:\\Piers\\UV UTC\\IC05\\CSV\\CN01.csv" );
		//election.exportToGEXF("C:\\Piers\\UV UTC\\IC05\\GEXF\\CN01.gexf");

		//			daListeDpt.addAll( election.getBureauxDpt( "60" ));
		//			election.liste_bureau.forEach((id, bureau) -> bureau.calculReportV1() );
		//			election.exportToGEXF( "/tempo/IC05-workSets/CN04.gexf" );
		//			election.exportToCSV( "/tempo/IC05-workSets/CN04_60.csv", daListeDpt );
		//		});	
	}
}
