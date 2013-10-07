package bitiodine.domain.model;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class Address
{
    public Address( String address ) {
        this.address = address;
    }
    
    public Address( Node underlyingNode ) {
        this.underlyingNode = underlyingNode;
    }

    public Node getUnderlyingNode() {
    	return this.underlyingNode;
    }
    
    public Relationship getClusterRelationship(){
    	return this.clusterRelationship;
    }
    
    public void setClusterRelationship(Relationship clusterRelationship) {
		this.clusterRelationship = clusterRelationship;
	}

	// Getters: delegate-to-the-node
    public String getAddress() {	
    	if (underlyingNode != null)
        	address = (String)underlyingNode.getProperty( "address" );
    	return address;
    }
    
    public Cluster getCluster() {
    	return null;
    }

    // Setters
    
    // START SNIPPET: override
    @Override
    public int hashCode()
    {
        return underlyingNode.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        return o instanceof Address &&
                underlyingNode.equals( ( (Address)o ).getUnderlyingNode() );
    }

    @Override
    public String toString()
    {
        return "Address[" + getAddress() + "]";
    }

    // END SNIPPET: override
    
    
    //Underlying node
    private  Node underlyingNode = null;
    private Relationship clusterRelationship = null;
    
	//Local address fiels
    private String address = null;

}