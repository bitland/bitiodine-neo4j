package bitiodine.domain.model;

import org.neo4j.graphdb.Node;

public class Cluster {
    
    public Node getUnderlyingNode() {
		return underlyingNode;
	}

	public void setUnderlyingNode(Node underlyingNode) {
		this.underlyingNode = underlyingNode;
	}

	public Cluster( Node underlyingNode ) {
        this.underlyingNode = underlyingNode;
    }
	
    //Underlying node
    private  Node underlyingNode = null;
    
}
