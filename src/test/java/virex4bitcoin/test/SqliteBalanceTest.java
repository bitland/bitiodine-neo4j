package virex4bitcoin.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sqlite.SQLiteJDBCLoader;

import virex4bitcoin.Virex4Bitcoin;
import virex4bitcoin.sqlite.Virex4BitcoinSqliteImpl;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SqliteBalanceTest extends Virex4BitcoinTest{
		private static final String SQLITE_PATH="../blockchain/blockchain_small_ext.sqlite";
	
		final static Logger logger = Logger.getLogger("Neo4jBalanceTest");
				
	    private static Connection sqliteConnection;
	    private static Virex4Bitcoin virex4bitcoin = null;
	    
	    private String address;
	    private Long attime;
	    
	    public SqliteBalanceTest(String address, long attime){
	    	this.address=address;
	    	this.attime= new Long(attime);
	    }
	    
		@BeforeClass 
		public static void startup(){
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
		
		@AfterClass 
		public static void shutdown(){
			logger.info("shutting down sqliteConnection...");
			try {
				sqliteConnection.close();
			} catch (SQLException e) {
				manageException(e);
			}
		}
			
		private static void manageException(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
	    
	    @Parameters
	    public static Collection<Object[]> testSet(){
	    	return Virex4BitcoinTest.testSet();
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
