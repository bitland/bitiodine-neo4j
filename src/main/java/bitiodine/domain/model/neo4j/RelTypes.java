package bitiodine.domain.model.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType{
	SAMEOWNER,
	CLUSTER,
	TXIN,
	TXOUT,
	NEXTTX,
	TXPREV,
	BLOCK
}
