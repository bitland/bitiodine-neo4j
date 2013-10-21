package bitiodinetest.plugins;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import bitiodine.domain.model.Block;
import bitiodine.domain.service.BlockLocalServiceUtil;
import bitiodine.plugins.GraphPlugin;
import bitiodine.plugins.GraphPluginImpl;

/**
 * Unit test for simple App.
 */
@RunWith(JUnit4.class)
public class GraphPluginTransactionTest
{

	@Test
    public void testGraphPlugin()
    {
    	System.out.println("Testing GraphPlugin");
        
        /*
        //Simple adding nodes and relationships
        Node n1 = gp.addAddressNode(graphDb, "address1");
        Node n2 = gp.addAddressNode(graphDb, "address2");
        Node n3 = gp.addClusterNode(graphDb, "cluster1");        
        Relationship r13 = gp.linkAddressToCluster(graphDb, "address1", "cluster1");
        Relationship r23 = gp.linkAddressToCluster(graphDb, "address2", "cluster1");
        */
        
        //Adding sample transactions
        //        gp.addTransactionNode(graphDb, txHash, 
        //        		txIns, amountsIn, 
        //        		txPrevs, 
        //        		txOuts, amountsOut, 
        //        		block_hash, timestamp)
        graphPlugin.addTransactionNode(graphDb, "tx-1", 
        		Arrays.asList("addr1","addr2","addr3"), Arrays.asList(new Long(10),new Long(10), new Long(20)),
        		Arrays.asList("tx1","tx2","tx3"), 
        		Arrays.asList("addr1","addr4"), Arrays.asList(new Long(20), new Long(20)), 
        		"block-1", new Long(10));
        
        graphPlugin.addTransactionNode(graphDb, "tx-2", 
        		Arrays.asList("addr2"), Arrays.asList(new Long(20)),
        		Arrays.asList("tx1"), 
        		Arrays.asList("addr1","addr4"), Arrays.asList(new Long(10),new Long(10)), 
        		"block-2", new Long(20));
        
        graphPlugin.addTransactionNode(graphDb, "tx-3", 
        		Arrays.asList("addr3"), Arrays.asList(new Long(10)),
        		Arrays.asList("tx1"), 
        		Arrays.asList("addr3","addr4"), Arrays.asList(new Long(5),new Long(5)), 
        		"block-6", new Long(5));
        
        Block b = BlockLocalServiceUtil.getFirst(graphDb);
        System.out.println(b.getHash());

        assertTrue(true);
    }
	
	
	@BeforeClass 
	public static void startGraphDb(){
    	graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        graphPlugin = new GraphPluginImpl();
	}
	
	@AfterClass 
	public static void shutdownGraphDb(){
		graphDb.shutdown();
	}
	
    
    private static final String DB_PATH = "target/data/graph.db";
    private static GraphDatabaseService graphDb = null;
    private static GraphPlugin graphPlugin = null;
}
