package bitiodine.domain.model;

import org.neo4j.graphdb.Node;

import bitiodine.domain.model.neo4j.Neo4jDomainEntity;

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
	
	public static String getAmountPropertyName(){
		return "amount";
	}
	
	public static String getPositionPropertyName(){
		return "position";
	}
	
	//Index names static methods
	public static String getUniqueIndexName(){
		return "transactions";
	}

}
