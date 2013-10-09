package bitiodine.domain.model;

import org.neo4j.graphdb.Node;

import bitiodine.domain.model.neo4j.Neo4jDomainEntity;

public class Block extends Neo4jDomainEntity {

	public Block(Node underlyingNode) {
		super(underlyingNode);
	}
	
	public String getHash(){
		return (String) super.getProperty(Block.getHashPropertyName());
	}
	
	//Property names static methods
	public static String getHashPropertyName(){
		return "hash";
	}
	public static String getTimestampPropertyName(){
		return "timestamp";
	}
	
	//Index names static methods
	public static String getUniqueIndexName(){
		return "blocks";
	}
	public static String getTimelineIndexName(){
		return "timestamps";
	}

}
