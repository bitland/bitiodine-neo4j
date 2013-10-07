package bitiodine.domain.model;

import org.neo4j.graphdb.Node;

public class Cluster {
    
	public Cluster( String id) {
        this.id = id;
    }
    
    public Node getUnderlyingNode() {
		return underlyingNode;
	}

	public void setUnderlyingNode(Node underlyingNode) {
		this.underlyingNode = underlyingNode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Cluster( Node underlyingNode ) {
        this.underlyingNode = underlyingNode;
    }
	
    //Underlying node
    private  Node underlyingNode = null;
    
	//Local address fiels
    private String id = null;
}
