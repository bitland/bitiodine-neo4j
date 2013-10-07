package bitiodine.domain.service;

import org.neo4j.graphdb.GraphDatabaseService;

import bitiodine.domain.model.Cluster;
import bitiodine.domain.service.impl.ClusterLocalServiceImpl;

public class ClusterLocalServiceUtil {
	
	public static Cluster getOrCreateCluster(GraphDatabaseService graphDb, String cluster_id){
		return getService(graphDb).getOrCreateCluster(cluster_id);
	}
	
	public static Cluster findClusterById(GraphDatabaseService graphDb, String cluster_id){
		return getService(graphDb).findClusterById(cluster_id);
	}
	
	public static ClusterLocalService getService(GraphDatabaseService graphDb){
		if (_service == null) {
			_service = new ClusterLocalServiceImpl(graphDb);
		}
		return _service;
	}
	
	private static ClusterLocalService _service = null;

}
