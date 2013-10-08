package bitiodine.domain.model.neo4j;

import org.neo4j.graphdb.Node;

public abstract class Neo4jDomainEntity {

    public Neo4jDomainEntity( Node underlyingNode ) {
        this.underlyingNode = underlyingNode;
    }
    
    public Node getUnderlyingNode() {
    	return this.underlyingNode;
    }
    
    protected Object getProperty( String key ){
    	Object o = null;
    	try (org.neo4j.graphdb.Transaction tx = underlyingNode.getGraphDatabase().beginTx()){
    		o = underlyingNode.getProperty( key );
    		tx.success();
    	}
    	return o;
    }
    
    private  Node underlyingNode = null;
    
}
