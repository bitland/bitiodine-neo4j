package virex4bitcoin.test.mongodb;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test; // JUnit4
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import virex4bitcoin.Virex4Bitcoin;
import virex4bitcoin.mongodb.Virex4BitcoinMongoImpl;

import com.mongodb.MongoClient;

@RunWith(Parameterized.class)
public class MongoBalanceTest {
	final static Logger logger = Logger.getLogger("Neo4jBalanceTest");
			
    private static MongoClient mongoClient = null;
    private static Virex4Bitcoin virex4bitcoin = null;
    
    private String address;
    private Long attime;
    
	@BeforeClass 
	public static void startGraphDb(){
		logger.info("starting mongoClient...");
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		}
        virex4bitcoin = new Virex4BitcoinMongoImpl(mongoClient);
	}
	
	@AfterClass 
	public static void shutdownGraphDb(){
		logger.info("shutting down mongoClient...");
		mongoClient.close();
	}
    
    public MongoBalanceTest(String address, long attime){
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
    

	
	private void calculateBalance(){
    	System.out.println("testNeo4jBalance address:"+address+" at time:"+attime+"...");
	   	Long balance = virex4bitcoin.balance(address,attime);
	    System.out.println("BalanceTest address:"+address+" at time:"+attime+" balance:"+balance) ;
	}
	
    @Test
 	public void testNeo4jBalance_0() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testNeo4jBalance_1() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testNeo4jBalance_2() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testNeo4jBalance_3() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testNeo4jBalance_4() {
    	this.calculateBalance();
  	}
    
    @Test
 	public void testNeo4jBalance_5() {
    	this.calculateBalance();
  	}
	


}
