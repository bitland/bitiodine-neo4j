package bitiodine.domain.model;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import bitiodine.domain.model.neo4j.Neo4jDomainEntity;
import bitiodine.domain.model.neo4j.RelTypes;

public class Address extends Neo4jDomainEntity
{    

	public Address(Node underlyingNode) {
		super(underlyingNode);
	}

	// Getters: delegate-to-the-node
    public String getAddress() {	
    	return (String) super.getProperty("address");
    }
    
    public Relationship getClusterRelationship(){
    	Relationship clusterRelationship = null;
    	try (org.neo4j.graphdb.Transaction tx = this.getUnderlyingNode().getGraphDatabase().beginTx()){
    		clusterRelationship = this.getUnderlyingNode()
    				.getSingleRelationship(RelTypes.CLUSTER,Direction.OUTGOING);
    		tx.success();
    	}
    	return clusterRelationship;
    }
    
}