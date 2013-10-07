package bitiodine.plugins;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

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
    	System.out.println("Hello World test");
        GraphDatabaseService graphDb = 
        		new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        this.registerShutdownHook(graphDb);
        GraphPlugin gp = new GraphPluginImpl();
        Node n1 = gp.addAddressNode(graphDb, "address1");
        Node n2 = gp.addAddressNode(graphDb, "address2");
        Node n3 = gp.addClusterNode(graphDb, "cluster1");
        
        Relationship r13 = gp.linkAddressToCluster(graphDb, "address1", "cluster1");
        Relationship r23 = gp.linkAddressToCluster(graphDb, "address2", "cluster1");
        
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
    
    private static final String DB_PATH = "target/neo4j-community-2.0.0-M05/data/graph.db";
}
