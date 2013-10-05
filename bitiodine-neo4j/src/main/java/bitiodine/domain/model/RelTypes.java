package bitiodine.domain.model;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType{
	SAMEOWNER,
	CLUSTER
}
