package virex4bitcoin.neo4j.model;

import org.neo4j.graphdb.RelationshipType;

public enum RelType implements RelationshipType{
	SAMEOWNER,
	CLUSTER,
	TXIN,
	TXOUT,
	NEXTTX,
	BLOCK
}
