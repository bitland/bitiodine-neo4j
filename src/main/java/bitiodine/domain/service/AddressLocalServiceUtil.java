package bitiodine.domain.service;

import org.neo4j.graphdb.GraphDatabaseService;

import bitiodine.domain.model.Address;
import bitiodine.domain.service.impl.AddressLocalServiceImpl;

public class AddressLocalServiceUtil {
	public static Address addAddress(GraphDatabaseService graphDb, String address){
		return getService(graphDb).addAddress(address);
	}
	
	public static Address linkAddressToCluster(GraphDatabaseService graphDb, 
			String address, String cluster_id) {
		return getService(graphDb).linkAddressToCluster(address, cluster_id);
	}
	
	public static Address findAddressByAddress(GraphDatabaseService graphDb, String address){
		return getService(graphDb).findAddressByAddress(address);
	}
	
	public static AddressLocalService getService(GraphDatabaseService graphDb){
		if (_service == null) {
			_service = new AddressLocalServiceImpl(graphDb);
		}
		return _service;
	}
	
	private static AddressLocalService _service = null;
}
