package virex4bitcoin.test.neo4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test; // JUnit4
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import virex4bitcoin.Virex4Bitcoin;
import virex4bitcoin.neo4j.Virex4BitcoinNeo4jImpl;

@RunWith(Parameterized.class)
public class Neo4jBalanceTest 
 {
	final static Logger logger = Logger.getLogger("Neo4jBalanceTest");
			
	private static final String DB_PATH = "../blockchain/graph.db";
    private static GraphDatabaseService graphDb = null;
    private static Virex4Bitcoin virex4bitcoin = null;
    
    private String address;
    private Long attime;
    
    public Neo4jBalanceTest(String address, long attime){
    	this.address=address;
    	this.attime= new Long(attime);
    }
    
    @Parameters
    public static Collection<Object[]> testSet(){
    	return Arrays.asList(new Object[][] {
    			{"1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3", 1383264000},
    			{"1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3", 1306886400},
    			{"1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3", 1233446400},
    			{"1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", 1383264000},
    			{"1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", 1306886400},
    			{"1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", 1233446400},
    			{"1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp", 1383264000},
    			{"1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp", 1306886400},
    			{"1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp", 1233446400},
    	});
    }
    
	@BeforeClass 
	public static void startGraphDb(){
		logger.info("starting GraphDb...");
    	graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
            	logger.info("shutting down GraphDb...");
                graphDb.shutdown();
            }
        } );
        virex4bitcoin = new Virex4BitcoinNeo4jImpl(graphDb);
	}
	
	@AfterClass 
	public static void shutdownGraphDb(){
		logger.info("shutting down GraphDb...");
		graphDb.shutdown();
	}
	
	private void calculateBalance(){
    	System.out.println("testNeo4jBalance address:"+address+" at time:"+attime+"...");
	   	Long balance = virex4bitcoin.balance(address,attime);
	    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance) ;
	}
	
    @Test
 	public void testBalance_0() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testBalance_1() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testBalance_2() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testBalance_3() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testBalance_4() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testBalance_5() {
    	this.calculateBalance();
  	}
	


}
