package bitiodine.domain.model;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import bitiodine.domain.model.neo4j.Neo4jDomainEntity;
import bitiodine.domain.model.neo4j.NodeType;
import bitiodine.domain.model.neo4j.RelType;

public class Address extends Neo4jDomainEntity
{    

	public Address(Node underlyingNode) {
		super(underlyingNode);
	}

	// Getters: delegate-to-the-node
    public String getAddress() {	
    	return (String) super.getProperty(Address.getAddressPropertyName());
    }
    
    public Relationship getClusterRelationship(){
    	Relationship clusterRelationship = null;
    	try (org.neo4j.graphdb.Transaction tx = this.getUnderlyingNode().getGraphDatabase().beginTx()){
    		clusterRelationship = this.getUnderlyingNode()
    				.getSingleRelationship(RelType.CLUSTER,Direction.OUTGOING);
    		tx.success();
    	}
    	return clusterRelationship;
    }
    
    
	//Property names static methods
	public static String getIdPropertyName(){
		return "address";
	}
	
	public static String getAddressPropertyName(){
		return "address";
	}
	
	public static Label getLabel(){
		return NodeType.ADDRESS;
	}
	
    
}