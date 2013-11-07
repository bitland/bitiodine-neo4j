package virex4bitcoin.test.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sqlite.SQLiteJDBCLoader;

import virex4bitcoin.Virex4Bitcoin;
import virex4bitcoin.sqlite.Virex4BitcoinSqliteImpl;

@RunWith(Parameterized.class)
public class SqliteBalanceTest {
		private static final String SQLITE_PATH="../blockchain/blockchain.sqlite";
	
		final static Logger logger = Logger.getLogger("Neo4jBalanceTest");
				
	    private static Connection sqliteConnection;
	    private static Virex4Bitcoin virex4bitcoin = null;
	    
	    private String address;
	    private Long attime;
	    
		@BeforeClass 
		public static void startGraphDb(){
			logger.info("starting mongoClient...");
		    try {
		    	if (!SQLiteJDBCLoader.isNativeMode())
		    		throw new Exception("Please use a native SQLiteJDBC driver");
				Class.forName("org.sqlite.JDBC");
				sqliteConnection = DriverManager
						.getConnection("jdbc:sqlite:"+SQLITE_PATH);
				sqliteConnection.setAutoCommit(false);
			} catch (Exception e) {
				manageException(e);
			}
	        virex4bitcoin = new Virex4BitcoinSqliteImpl(sqliteConnection);
		}
		
		private static void manageException(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		
		@AfterClass 
		public static void shutdownGraphDb(){
			logger.info("shutting down sqliteConnection...");
			try {
				sqliteConnection.close();
			} catch (SQLException e) {
				manageException(e);
			}
		}
	    
	    public SqliteBalanceTest(String address, long attime){
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
