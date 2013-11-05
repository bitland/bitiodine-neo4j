package bitiodinetest.plugins;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test; // JUnit4
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import bitiodine.plugins.VirexBitcoinPlugin;
import bitiodine.plugins.VirexBitcoinPluginImpl;


public class Virex4BitcoinBalanceTest 
 {
	private static final String DB_PATH = "blockchain/graph.db";
    private static GraphDatabaseService graphDb = null;
    private static VirexBitcoinPlugin graphPlugin = null;
    
	@BeforeClass 
	public static void startGraphDb(){
    	graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        graphPlugin = new VirexBitcoinPluginImpl();
	}
	
	@AfterClass 
	public static void shutdownGraphDb(){
		graphDb.shutdown();
	}
	
	@Test
 	public void testSequence_1() 
{ String address = "1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3";
   Long attime = new Long(1383264000) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_2() 
{ String address = "1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3";
   Long attime = new Long(1233446400) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_3() 
{ String address = "1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3";
   Long attime = new Long(1306886400) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_4() 
{ String address = "abcde";
   Long attime = new Long(1383264000) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_5() 
{ String address = "abcde";
   Long attime = new Long(1233446400) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_6() 
{ String address = "abcde";
   Long attime = new Long(1306886400) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_7() 
{ String address = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa";
   Long attime = new Long(1383264000) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_8() 
{ String address = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa";
   Long attime = new Long(1233446400) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_9() 
{ String address = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa";
   Long attime = new Long(1306886400) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_10() 
{ String address = "1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp";
   Long attime = new Long(1383264000) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_11() 
{ String address = "1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp";
   Long attime = new Long(1233446400) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    
			
  	}

    @Test
 	public void testSequence_12() 
{ String address = "1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp";
   Long attime = new Long(1306886400) ;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+"...") ;
   long startTime = System.currentTimeMillis() ;
   Long balance = graphPlugin.balance(graphDb,address,attime) ;
   long stopTime = System.currentTimeMillis() ;
   long elapsedTime = stopTime - startTime;
    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance+" ET:"+elapsedTime+" ms") ;
    			
  	}

	

}
