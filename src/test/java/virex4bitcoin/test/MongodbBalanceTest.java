package virex4bitcoin.test;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test; // JUnit4
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import virex4bitcoin.Virex4Bitcoin;
import virex4bitcoin.mongodb.Virex4BitcoinMongodbImpl;

import com.mongodb.MongoClient;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongodbBalanceTest extends Virex4BitcoinTest{
		final static Logger logger = Logger.getLogger("MongodbBalanceTest");
				
	    private static MongoClient mongoClient = null;
	    private static Virex4Bitcoin virex4bitcoin = null;
	    
	    private String address;
	    private Long attime;
	    
	    public MongodbBalanceTest(String address, long attime){
	    	this.address=address;
	    	this.attime= new Long(attime);
	    }
	    
		@BeforeClass 
		public static void startup(){
			logger.info("starting mongoClient...");
			try {
				mongoClient = new MongoClient();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(0);
			}
	        virex4bitcoin = new Virex4BitcoinMongodbImpl(mongoClient);
		}
		
		@AfterClass 
		public static void shutdown(){
			logger.info("shutting down mongoClient...");
			mongoClient.close();
		}
	     
	    @Parameters
	    public static Collection<Object[]> testSet(){
	    	return Virex4BitcoinTest.testSet();
	    }
	    
		private void calculateBalance(){
	    	System.out.println("testMongodbBalance address:"+address+" at time:"+attime+"...");
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
