package bitiodine.domain.service.impl;

import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.UniqueFactory;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.Block;
import bitiodine.domain.model.Transaction;
import bitiodine.domain.model.TxInOut;
import bitiodine.domain.model.neo4j.NodeTypes;
import bitiodine.domain.model.neo4j.RelTypes;
import bitiodine.domain.model.neo4j.UniqueRelationshipFactory;
import bitiodine.domain.service.AddressLocalServiceUtil;
import bitiodine.domain.service.BlockLocalServiceUtil;
import bitiodine.domain.service.TransactionLocalService;
import bitiodine.domain.service.TransactionLocalServiceUtil;

public class TransactionLocalServiceImpl implements TransactionLocalService{
	
	public TransactionLocalServiceImpl(GraphDatabaseService graphDb) {
		this.graphDb=graphDb;	
		
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
			//Unique transaction factory
			this.uniqueTransactionFactory = new 
		    		UniqueFactory.UniqueNodeFactory( graphDb, 
		    				Transaction.getUniqueIndexName() ) 
			{
					@Override
					protected void initialize(Node created,
							Map<String, Object> properties) 
					{
						created.setProperty( Transaction.getHashPropertyName(), 
								properties.get( Transaction.getHashPropertyName() ) );	
					}
		    };
		    
		    this.uniqueRelationshipFactory = new UniqueRelationshipFactory(graphDb,
		    		"iorelationships");
		        
			tx.success();
		}
	}
	
	@Override
	public Transaction getOrCreateTransaction(String txHash){
		Transaction t = null;
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
    		// get-or-create node
			Node n = uniqueTransactionFactory.getOrCreate( Transaction.getHashPropertyName()
					, txHash );
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
			String blockHash, Long timestamp) {
		Transaction t = null;
		
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
    		// get-or-create node
			Node n = uniqueTransactionFactory.getOrCreate( Transaction.getHashPropertyName()
					, txHash );
    		n.addLabel(NodeTypes.TRANSACTION);
    		
    		// Link to input addresses and amounts
    		for (int i=0; i<txIns.size(); i++) {
    			//Get or Create the Address
    			Address a = AddressLocalServiceUtil.getOrCreateAddress(graphDb, txIns.get(i));
    			Transaction t2 = TransactionLocalServiceUtil.getOrCreateTransaction(graphDb,txPrev.get(i));
    			
    			Relationship r =uniqueRelationshipFactory.getOrCreate(a.getUnderlyingNode(), 
    					n, RelTypes.TXIN, txHash+" input #"+i+" = "+txIns.get(i));
    			r.setProperty(TxInOut.getAmountPropertyName(), amountsIn.get(i));
    			r.setProperty(TxInOut.getTxInPositionPropertyName(), i);
    			
    			uniqueRelationshipFactory.getOrCreate(n, t2.getUnderlyingNode(),
    					RelTypes.TXPREV, txHash+" input #"+i+" = "+txPrev.get(i));
    		}
    		
    		// Link to output addresses and amounts
    		for (int i=0; i<txOuts.size(); i++) {
    			Address a = AddressLocalServiceUtil.getOrCreateAddress(graphDb, txOuts.get(i));		
    			Relationship r = uniqueRelationshipFactory.getOrCreate(
    					n, a.getUnderlyingNode(), RelTypes.TXOUT, txHash+" output #"+i);
    			r.setProperty(TxInOut.getAmountPropertyName(), amountsOut.get(i));
    			r.setProperty(TxInOut.getTxOutPositionPropertyName(), i);
    		}
    		
    		Block b = BlockLocalServiceUtil.getOrCreateBlock(graphDb, blockHash, timestamp);
			if ( n.getSingleRelationship(RelTypes.BLOCK,Direction.OUTGOING) != null)
				n.getSingleRelationship(RelTypes.BLOCK,Direction.OUTGOING).delete();
			n.createRelationshipTo(b.getUnderlyingNode(), RelTypes.BLOCK);
    		
    		t = new Transaction(n);
			tx.success();
		}
		return t;
	}
	
	
	
	
	@Override
	public Transaction getFirst() {
		//TODO 
		return null;
	}
	
	
	private GraphDatabaseService graphDb = null;
	private UniqueFactory<Node> uniqueTransactionFactory = null;
	private UniqueRelationshipFactory uniqueRelationshipFactory = null;

}
