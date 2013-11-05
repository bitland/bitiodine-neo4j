package bitiodine.domain.service;

import org.neo4j.graphdb.GraphDatabaseService;

import bitiodine.domain.service.impl.VirexBitcoinLocalServiceImpl;

public class VirexBitcoinLocalServiceUtil {
	private static VirexBitcoinLocalService _service = null;
	
	private static VirexBitcoinLocalService getService(GraphDatabaseService graphDb){
		if (_service == null) {
			_service = new VirexBitcoinLocalServiceImpl(graphDb);
		}
		return _service;
	}
	
	public static Long flow(GraphDatabaseService graphDb, String payer,
			String payee) {
		return getService(graphDb).flow(payer, payee);
	}
	
	public static Long flow(GraphDatabaseService graphDb, 
			String payer, String payee, Long fromdate, Long todate){
		return getService(graphDb).flow(payer, payee, fromdate, todate);
	}
	
	public static Long balance(GraphDatabaseService graphDb, 
			String address){
		return getService(graphDb).balance(address);
	}

	public static Long balance(GraphDatabaseService graphDb, String address,
			Long attime) {
		return getService(graphDb).balance(address,attime);
	}
}
