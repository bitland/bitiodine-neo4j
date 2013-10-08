package bitiodine.domain.model;

import org.neo4j.graphdb.Node;

import bitiodine.domain.model.neo4j.Neo4jDomainEntity;

public class Transaction extends Neo4jDomainEntity{
	
	public Transaction( Node underlyingNode ) {
        super(underlyingNode);
    }
	
	public String getHash(){
    	return (String) super.getProperty("tx-hash");
	}

}
