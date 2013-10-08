package bitiodinetest.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import bitiodine.domain.model.Block;
import bitiodine.domain.model.Transaction;
import bitiodine.domain.service.BlockLocalServiceUtil;
import bitiodine.domain.service.TransactionLocalServiceUtil;
import bitiodine.plugins.GraphPlugin;
import bitiodine.plugins.GraphPluginImpl;

/**
 * Unit test for simple App.
 */
public class GraphPluginTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GraphPluginTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GraphPluginTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testGraphPlugin()
    {
    	System.out.println("Testing GraphPlugin");
        
    	GraphDatabaseService graphDb = 
        		new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        this.registerShutdownHook(graphDb);
        GraphPlugin gp = new GraphPluginImpl();
        
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
        gp.addTransactionNode(graphDb, "tx-1", 
        		Arrays.asList("addr1","addr2","addr3"), Arrays.asList(10,10,20),
        		(List<String>) new ArrayList<String>(), 
        		Arrays.asList("addr1","addr4"), Arrays.asList(20,20), 
        		"block-1", new Long(10));
        
        gp.addTransactionNode(graphDb, "tx-2", 
        		Arrays.asList("addr2"), Arrays.asList(20),
        		(List<String>) new ArrayList<String>(), 
        		Arrays.asList("addr1","addr4"), Arrays.asList(10,10), 
        		"block-2", new Long(20));
        
        gp.addTransactionNode(graphDb, "tx-3", 
        		Arrays.asList("addr3"), Arrays.asList(10),
        		(List<String>) new ArrayList<String>(), 
        		Arrays.asList("addr3","addr4"), Arrays.asList(5,5), 
        		"block-6", new Long(5));
        
        Block b = BlockLocalServiceUtil.getFirst(graphDb);
        System.out.println(b.getHash());
        
        graphDb.shutdown();
        assertTrue( true );
    }
    
    private void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        });
    }
    
    private static final String DB_PATH = "neo4j-community-2.0.0-M05/data/graph.db";
}
