package bitiodine.domain.service;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;

import bitiodine.domain.model.Transaction;
import bitiodine.domain.service.impl.TransactionLocalServiceImpl;

public class TransactionLocalServiceUtil {
	public static Transaction getOrCreateTransaction(GraphDatabaseService graphDb,
			String txHash, List<String> txIns, List<Long> amountsIn,
			List<String> txPrevs, List<String> txOuts,
			List<Long> amountsOut, String block_hash, Long timestamp){
		return getService(graphDb).getOrCreateTransaction(txHash, txIns, amountsIn, 
				txPrevs, txOuts, amountsOut, block_hash, timestamp);
	}
	
	public static Transaction getOrCreateTransaction(GraphDatabaseService graphDb, String txHash){
		return getService(graphDb).getOrCreateTransaction(txHash);
	}
	
	public static Transaction getFirt(GraphDatabaseService graphDb){
		return getService(graphDb).getFirst();
	}
	
	public static TransactionLocalService getService(GraphDatabaseService graphDb){
		if (_service == null) {
			_service = new TransactionLocalServiceImpl(graphDb);
		}
		return _service;
	}
	
	private static TransactionLocalService _service = null;
}
