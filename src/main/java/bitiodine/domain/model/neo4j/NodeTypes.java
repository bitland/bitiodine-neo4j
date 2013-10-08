package bitiodine.domain.model.neo4j;

import org.neo4j.graphdb.Label;

public enum NodeTypes implements Label {
	ADDRESS,
	CLUSTER,
	TRANSACTION,
	BLOCK
}
