package bitiodine.domain.model.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum RelType implements RelationshipType{
	SAMEOWNER,
	CLUSTER,
	TXIN,
	TXOUT,
	NEXTTX,
	BLOCK
}
