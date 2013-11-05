package bitiodine.plugins;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public interface VirexBitcoinPlugin {
	
	Node addTransactionNode(GraphDatabaseService graphDb, String txHash,
			List<String> txIns, List<Long> amountsIn, List<String> txPrevs,
			List<String> txOuts, List<Long> amountsOut, String block_hash,
			Long timestamp);
	
	Long flow(GraphDatabaseService graphDb, String payer, String payee);
	Long flow(GraphDatabaseService graphDb, String payer, String payee, Long fromdate, Long todate);

	Long balance(GraphDatabaseService graphDb, String address);
	Long balance(GraphDatabaseService graphDb, String address, Long attime);
	
}
