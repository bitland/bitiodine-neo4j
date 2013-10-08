package bitiodine.domain.service;

import org.neo4j.graphdb.GraphDatabaseService;

import bitiodine.domain.model.Block;
import bitiodine.domain.service.impl.BlockLocalServiceImpl;


public class BlockLocalServiceUtil {
	
	public static Block getOrCreateBlock(GraphDatabaseService graphDb, String blockHash){
		return getService(graphDb).getOrCreateBlock(blockHash);
	}
	
	public static Block getOrCreateBlock(GraphDatabaseService graphDb, String blockHash, Long timestamp){
		return getService(graphDb).getOrCreateBlock(blockHash,timestamp);
	}
	
	public static Block getFirst(GraphDatabaseService graphDb){
		return getService(graphDb).getFirst();
	}
	
	public static BlockLocalService getService(GraphDatabaseService graphDb){
		if (_service == null) {
			_service = new BlockLocalServiceImpl(graphDb);
		}
		return _service;
	}
	
	private static BlockLocalService _service = null;

}
