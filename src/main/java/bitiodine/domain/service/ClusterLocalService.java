package bitiodine.domain.service;

import bitiodine.domain.model.Cluster;

public interface ClusterLocalService {
	//CRUD
	public Cluster getOrCreateCluster(String cluster_id);
	
	//Other operations
	
	//Finder methods
	public Cluster findClusterById(String cluster_id);
}
