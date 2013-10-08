package bitiodine.domain.model;

import org.neo4j.graphdb.Node;

import bitiodine.domain.model.neo4j.Neo4jDomainEntity;

public class Block extends Neo4jDomainEntity {

	public Block(Node underlyingNode) {
		super(underlyingNode);
	}
	
	public String getHash(){
		return (String) super.getProperty("block-hash");
	}

}
