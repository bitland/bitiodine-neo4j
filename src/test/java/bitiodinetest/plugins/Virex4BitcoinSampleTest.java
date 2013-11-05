package bitiodinetest.plugins;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import bitiodine.plugins.VirexBitcoinPlugin;
import bitiodine.plugins.VirexBitcoinPluginImpl;

/**
 * Unit test for simple App.
 */
@RunWith(JUnit4.class)
public class Virex4BitcoinSampleTest
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
    public void testCase()
    {
		assertTrue(true);
    }

}
