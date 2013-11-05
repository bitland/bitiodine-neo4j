package virex4bitcoin.neo4j.service;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

public class VirexBitcoinLocalServiceImpl implements VirexBitcoinLocalService {
	private GraphDatabaseService graphDb = null;
	private ExecutionEngine executionEngine = null;

	public VirexBitcoinLocalServiceImpl(GraphDatabaseService graphDb) {
		this.graphDb=graphDb;
		executionEngine = new ExecutionEngine(graphDb);
	}

	@Override
	public Long balance(String address) {
		/*
		 * MATCH (a:ADDRESS)-[a2t]-(t:TRANSACTION) 
		 * WHERE a.address="1933phfhK3ZgFQNLGSDXvqCn32k2buXY8a" 
		 * RETURN sum(a2t.amount * CASE WHEN type(a2t)="TXOUT" THEN +1 ELSE -1 END)/100000000 as balance
		 */
		
		String cypher_query = "MATCH (a:ADDRESS)-[a2t]-(t:TRANSACTION) WHERE a.address=\""+address+"\" "
				+ "RETURN a.address as ID, "
				+ "sum(a2t.amount * CASE WHEN type(a2t)=\"TXOUT\" THEN 1 ELSE -1 END) as balance";
		System.out.println(cypher_query);
		ResourceIterator<Long> iterator = executionEngine.execute(cypher_query).columnAs("balance");
		Long balance = iterator.next();
		iterator.close();
		
		return balance;
	}


	
	@Override
	public Long balance(String address, Long atdate) {
		Long balance = new Long(0);
		if (atdate>=0){
			try (Transaction t = graphDb.beginTx()) {
				
				String cypher_query = "MATCH (a:ADDRESS)-[a2t]-(t:TRANSACTION), (t)-[:BLOCK]->(b) "
					+ "WHERE a.address=\""+address+"\" AND b.time<"+atdate
					+ " RETURN a.address as ID,"
					+ " sum(a2t.amount * CASE WHEN type(a2t)=\"TXOUT\" THEN 1 ELSE -1 END) as balance";
				ResourceIterator<Long> iterator = executionEngine.execute(cypher_query).columnAs("balance");
				if (iterator.hasNext())
					balance = iterator.next();
				iterator.close();
							
//				Node address_node = graphDb.findNodesByLabelAndProperty(Address.getLabel(), 
//						Address.getAddressPropertyName(), address).iterator().next();
//				Iterator <Relationship> edges = address_node.getRelationships().iterator();
//				while (edges.hasNext()){
//					Relationship r = edges.next();
//					if (r.isType(RelType.TXOUT))
//						balance+= (Long) r.getProperty(TxInOut.getAmountPropertyName());
//					else
//						balance-= (Long) r.getProperty(TxInOut.getAmountPropertyName());
//				}
				
			}	
		}
		return balance;
	}

	@Override
	public Long flow(String payer, String payee) {
		return this.flow(payer, payee, 0L, System.currentTimeMillis());
	}
	
	@Override
	public Long flow(String payer, String payee, Long fromdate,
			Long todate) {
		/*
		 * MATCH (payer:ADDRESS), (payee:ADDRESS), 
		 * (payer)-[:TXIN]->(t)-[r2:TXOUT]->(payee), 
		 * (t)-[:BLOCK]->(b)
		 * WHERE payer.address="1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3" 
		 * AND payee.address="12S8yhyGaTdVtE31zdnPNFsLQ9aAitQRYA" 
		 * AND b.time<1383140328 AND  b.time>0
		 * RETURN sum(r2.amount)
		 */
		
		String cypher_query = "MATCH (payer:ADDRESS), (payee:ADDRESS), "
				+ "(payer)-[:TXIN]->(t)-[r2:TXOUT]->(payee), (t)-[:BLOCK]->(b) "
				+ "WHERE payer.address=\""+payer+"\" AND payee.address=\""+payee+"\" "
				+ "AND b.time>"+fromdate+" AND  b.time<"+todate+" "
				+ "RETURN sum(r2.amount) AS flow";
		ResourceIterator<Long> iterator = executionEngine.execute(cypher_query).columnAs("flow");
		Long flow = iterator.next();
		iterator.close();
		return flow;
	}

}
