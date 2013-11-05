package bitiodine.domain.model;

import org.neo4j.graphdb.Node;

import bitiodine.domain.model.neo4j.Neo4jDomainEntity;

public class Cluster extends Neo4jDomainEntity{
    
	public Cluster( Node underlyingNode ) {
        super(underlyingNode);
    }
	
	//Property names static methods
	public static String getIdPropertyName(){
		return "cluster";
	}
	
}
