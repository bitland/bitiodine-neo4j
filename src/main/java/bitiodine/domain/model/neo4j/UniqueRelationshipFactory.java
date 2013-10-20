package bitiodine.domain.model.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.RelationshipIndex;

public class UniqueRelationshipFactory{
	public UniqueRelationshipFactory(GraphDatabaseService graphDb, String indexname){
		this.graphDb=graphDb;
		uniqueRelationshipIndex = graphDb.index().forRelationships(indexname);
	}
	
	public Relationship getOrCreate(Node startingNode, Node endNode, 
			RelationshipType relType, Object id){
		Relationship r = uniqueRelationshipIndex.get("rel_id", id).getSingle();
		if (r==null){
			r = startingNode.createRelationshipTo(endNode, relType);
			r.setProperty("rel_id", id);
			uniqueRelationshipIndex.add(r, "rel_id", id);
		}
		return r;
		
	}
	
	public Relationship get(Object id){
		return uniqueRelationshipIndex.get("rel_id", id).getSingle();	
	}
	
	GraphDatabaseService graphDb;
	RelationshipIndex uniqueRelationshipIndex;

}
