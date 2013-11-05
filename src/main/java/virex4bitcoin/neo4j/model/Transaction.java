package virex4bitcoin.neo4j.model;

import org.neo4j.graphdb.Node;

public class Transaction extends Neo4jDomainEntity{
	
	public Transaction( Node underlyingNode ) {
        super(underlyingNode);
    }
	
	public String getHash(){
    	return (String) super.getProperty(Transaction.getHashPropertyName());
	}
	
	//Property names static methods
	public static String getIdPropertyName(){
		return "tx_id";
	}
	public static String getHashPropertyName(){
		return "tx_hash";
	}
	
}
