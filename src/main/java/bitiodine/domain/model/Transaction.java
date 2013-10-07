package bitiodine.domain.model;

import java.util.List;

import org.neo4j.graphdb.Node;

public class Transaction {
	
	public Transaction( Node underlyingNode ) {
        this.underlyingNode = underlyingNode;
    }
	
	public Node getUnderlyingNode() {
    	return this.underlyingNode;
    }
	
	public String getTransactionHash() {
		return (String) this.underlyingNode.getProperty("hash");
	}
	
	public List<Address> getInputAddresses(){
		return null;
	}
	
	public List<Long> getInputAmounts(){
		return null;
	}
	
	
	private  Node underlyingNode = null;
}
