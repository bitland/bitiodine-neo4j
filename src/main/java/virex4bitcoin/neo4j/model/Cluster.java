package virex4bitcoin.neo4j.model;

import org.neo4j.graphdb.Node;

public class Cluster extends Neo4jDomainEntity{
    
	public Cluster( Node underlyingNode ) {
        super(underlyingNode);
    }
	
	//Property names static methods
	public static String getIdPropertyName(){
		return "cluster";
	}
	
}
