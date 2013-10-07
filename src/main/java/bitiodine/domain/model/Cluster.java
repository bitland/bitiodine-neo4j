package bitiodine.domain.model;

import org.neo4j.graphdb.Node;

public class Cluster {
    
	public Cluster( Node underlyingNode ) {
        this.underlyingNode = underlyingNode;
    }
	
    public Node getUnderlyingNode() {
		return this.underlyingNode;
	}
	
    private  Node underlyingNode = null;
    
}
