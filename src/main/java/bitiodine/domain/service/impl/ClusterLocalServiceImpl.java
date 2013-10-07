package bitiodine.domain.service.impl;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import bitiodine.domain.model.Cluster;
import bitiodine.domain.model.NodeTypes;
import bitiodine.domain.service.ClusterLocalService;

public class ClusterLocalServiceImpl implements ClusterLocalService {
	
	public ClusterLocalServiceImpl(GraphDatabaseService graphDb) {
		this.graphDb=graphDb;
	}
	
	@Override
	public Cluster addCluster(String cluster_id) {
		Cluster c = null;
		try ( Transaction tx = graphDb.beginTx() ) {
			//TODO Check if the cluster is already present
    		Node n = graphDb.createNode();
    		n.addLabel(NodeTypes.CLUSTER);
    		n.setProperty("cluster", cluster_id);
    		c = new Cluster(n);
    		tx.success();
        }
		catch(Exception e){
			e.printStackTrace();
		}
		return c;
	}
	
	@Override
	public Cluster findClusterById(String cluster_id){
		Cluster c = null;
		try ( Transaction tx = graphDb.beginTx() ) {
			c = new Cluster (graphDb
					.findNodesByLabelAndProperty(NodeTypes.CLUSTER, "cluster", cluster_id)
					.iterator().next());
			tx.success();
		}
		return c;
	}
	
	private GraphDatabaseService graphDb = null;

}
