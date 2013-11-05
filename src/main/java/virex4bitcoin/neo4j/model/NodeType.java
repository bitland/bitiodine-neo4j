package virex4bitcoin.neo4j.model;

import org.neo4j.graphdb.Label;

public enum NodeType implements Label {
	ADDRESS,
	CLUSTER,
	TRANSACTION,
	BLOCK
}
