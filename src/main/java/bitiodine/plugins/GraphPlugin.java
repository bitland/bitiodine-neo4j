package bitiodine.plugins;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public interface GraphPlugin {
	Node addAddressNode( GraphDatabaseService graphDb, String address);
	Node addClusterNode( GraphDatabaseService graphDb, String cluster_id);
	
	//Methods for Address Nodes
	Relationship linkAddressToCluster(GraphDatabaseService graphDb, 
			String address, String cluster_id);
	
	Node addTransactionNode(GraphDatabaseService graphDb, String txHash,
			List<String> txIns, List<Long> amountsIn, List<String> txPrevs,
			List<String> txOuts, List<Long> amountsOut, String block_hash,
			Long timestamp);
	
}
