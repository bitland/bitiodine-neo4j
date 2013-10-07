package bitiodine.domain.service.impl;

import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.UniqueFactory;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.NodeTypes;
import bitiodine.domain.model.RelTypes;
import bitiodine.domain.model.Transaction;
import bitiodine.domain.service.AddressLocalServiceUtil;
import bitiodine.domain.service.TransactionLocalService;
import bitiodine.domain.service.TransactionLocalServiceUtil;

public class TransactionLocalServiceImpl implements TransactionLocalService{
	
	public TransactionLocalServiceImpl(GraphDatabaseService graphDb) {
		this.graphDb=graphDb;
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
		    uniqueTransactionFactory = new 
		    		UniqueFactory.UniqueNodeFactory( graphDb, "transactions" ) {
					@Override
					protected void initialize(Node created,
							Map<String, Object> properties) {
						created.setProperty( "tx-hash", properties.get( "tx-hash" ) );	
					}
		    };
		}
	}
	
	@Override
	public Transaction getOrCreateTransaction(String txHash){
		Transaction t = null;
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
    		// get-or-create node
			Node n = uniqueTransactionFactory.getOrCreate( "tx-hash", txHash );
    		n.addLabel(NodeTypes.TRANSACTION);
    		t = new Transaction(n);
			tx.success();
		}
		return t;
	}
	
	@Override
	public Transaction getOrCreateTransaction(
			String txHash, List<String> txIns, List<Long> amountsIn,
			List<String> txPrev, List<String> txOuts, List<Long> amountsOut, 
			String block_hash, Long timestamp) {
		Transaction t = null;
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
    		// get-or-create node
			Node n = uniqueTransactionFactory.getOrCreate( "tx-hash", txHash );
    		n.addLabel(NodeTypes.TRANSACTION);
    		
    		// Link to input addresses and amounts
    		for (int i=0; i<txIns.size(); i++) {
    			AddressLocalServiceUtil.getOrCreateAddress(graphDb, txIns.get(i))
    				.getUnderlyingNode().createRelationshipTo(n, RelTypes.TXIN)
    				.setProperty("amount", amountsIn.get(i));
    		}
    		
    		// Link to output addresses and amounts
    		for (int i=0; i<txOuts.size(); i++) {
    			Address a = AddressLocalServiceUtil.getOrCreateAddress(graphDb, txOuts.get(i));
    			n.createRelationshipTo(a.getUnderlyingNode(), RelTypes.TXOUT)
    				.setProperty("amount", amountsOut.get(i));
    		}
    		
    		// Link to input transactions
    		for (int i=0; i<txPrev.size(); i++) {
    			Transaction t2 = TransactionLocalServiceUtil.getOrCreateTransaction(graphDb,txPrev.get(i));
    			n.createRelationshipTo(t2.getUnderlyingNode(), RelTypes.TXPREV);
    		}
    		
    		//TODO Link to block
    		n.setProperty("timestamp", timestamp);
    		
    		t = new Transaction(n);
			tx.success();
		}
		return t;
	}
	
	
	private GraphDatabaseService graphDb = null;
	private UniqueFactory<Node> uniqueTransactionFactory = null;
}
