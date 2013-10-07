package bitiodine.domain.model;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class Address
{    
    public Address( Node underlyingNode ) {
        this.underlyingNode = underlyingNode;
    }

    public Node getUnderlyingNode() {
    	return this.underlyingNode;
    }

	// Getters: delegate-to-the-node
    public String getAddress() {	
    	String address = null;
    	try (org.neo4j.graphdb.Transaction tx = underlyingNode.getGraphDatabase().beginTx()){
    		address = (String)underlyingNode.getProperty( "address" );
    		tx.success();
    	}
    	return address;
    }
    
    public Relationship getClusterRelationship(){
    	Relationship clusterRelationship = null;
    	try (org.neo4j.graphdb.Transaction tx = underlyingNode.getGraphDatabase().beginTx()){
    		clusterRelationship = this.underlyingNode
    				.getSingleRelationship(RelTypes.CLUSTER,Direction.OUTGOING);
    		tx.success();
    	}
    	return clusterRelationship;
    }
    
    private  Node underlyingNode = null;
}