package virex4bitcoin.neo4j.model;

import org.neo4j.graphdb.Node;

public class Block extends Neo4jDomainEntity {

	public Block(Node underlyingNode) {
		super(underlyingNode);
	}
	
	public String getHash(){
		return (String) super.getProperty(Block.getHashPropertyName());
	}
	
	//Property names static methods
	public static String getIdPropertyName(){
		return "block_id";
	}
	public static String getHashPropertyName(){
		return "block_hash";
	}
	public static String getTimestampPropertyName(){
		return "time";
	}
	
	//Index names static methods

	public static String getTimelineIndexName(){
		return "blocktimeline";
	}
	
}
