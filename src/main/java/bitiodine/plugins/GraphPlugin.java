package bitiodine.plugins;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public interface GraphPlugin {
	public Node addAddressNode( GraphDatabaseService graphDb, String address);
	public Node addClusterNode( GraphDatabaseService graphDb, String cluster_id);
	
	//Methods for Address Nodes
	public Relationship linkAddressToCluster(GraphDatabaseService graphDb, 
			String address, String cluster_id);
	
}
