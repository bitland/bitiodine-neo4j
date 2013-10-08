package bitiodine.domain.service.impl;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;

import bitiodine.domain.model.Cluster;
import bitiodine.domain.model.neo4j.NodeTypes;
import bitiodine.domain.service.ClusterLocalService;

public class ClusterLocalServiceImpl implements ClusterLocalService {
	
	public ClusterLocalServiceImpl(GraphDatabaseService graphDb) {
		this.graphDb=graphDb;
		try ( Transaction tx = graphDb.beginTx() ) {
		    uniqueClusterFactory = new 
		    		UniqueFactory.UniqueNodeFactory( graphDb, "clusters" ) {
					@Override
					protected void initialize(Node created,
							Map<String, Object> properties) {
						created.setProperty( "cluster", properties.get( "cluster" ) );	
					}
		    };
		    tx.success();
		}
	}
	
	@Override
	public Cluster getOrCreateCluster(String cluster_id) {
		Cluster c = null;
		try ( Transaction tx = graphDb.beginTx() ) {
			Node n = uniqueClusterFactory.getOrCreate( "cluster", cluster_id );
    		n.addLabel(NodeTypes.CLUSTER);
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
	private UniqueFactory<Node> uniqueClusterFactory = null;

}
