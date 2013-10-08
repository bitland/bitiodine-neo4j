package bitiodine.domain.service.impl;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.UniqueFactory;
import org.neo4j.index.lucene.LuceneTimeline;
import org.neo4j.index.lucene.TimelineIndex;

import bitiodine.domain.model.Block;
import bitiodine.domain.model.neo4j.NodeTypes;
import bitiodine.domain.service.BlockLocalService;

public class BlockLocalServiceImpl implements BlockLocalService{

	public BlockLocalServiceImpl(GraphDatabaseService graphDb) {
		this.graphDb=graphDb;	
		
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
			//Unique block factory
			this.uniqueBlockFactory = new 
		    		UniqueFactory.UniqueNodeFactory( graphDb, "blocks" ) {
						@Override
						protected void initialize(Node created,
								Map<String, Object> properties) {
							created.setProperty( "block-hash", properties.get( "block-hash" ) );	
						}
		    };
		    
		    //Block timeline index
			blockTimelineIndex = new LuceneTimeline<Node>(graphDb, 
					graphDb.index().forNodes("blocks"));
		    
			tx.success();
		}
	}

	@Override
	public Block getOrCreateBlock(String blockHash) {
		Block b = null;
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
			Node n = uniqueBlockFactory.getOrCreate( "block-hash", blockHash );
    		n.addLabel(NodeTypes.BLOCK);
    		b = new Block(n);
    		tx.success();
        }
		catch(Exception e){
			e.printStackTrace();
		}
		return b;
	}
	
	@Override
	public Block getOrCreateBlock(String blockHash, Long timestamp) {
		Block b = null;
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
			Node n = uniqueBlockFactory.getOrCreate( "block-hash", blockHash );
    		n.addLabel(NodeTypes.BLOCK);
    		
    		if (n.hasProperty("timestamp"))
    			blockTimelineIndex.remove(n, (long) n.getProperty("timestamp"));
    		n.setProperty("timestamp", timestamp);
    		blockTimelineIndex.add(n, timestamp);
    		
    		b = new Block(n);
    		tx.success();
        }
		catch(Exception e){
			e.printStackTrace();
		}
		return b;
	}
	
	@Override
	public Block getFirst() {
		Block b = null;
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) {
			Node n = blockTimelineIndex.getFirst();
    		b = new Block(n);
			tx.success();
		}
		return b;
	}
	
	private GraphDatabaseService graphDb = null;
	private UniqueFactory<Node> uniqueBlockFactory = null;
	private TimelineIndex<Node> blockTimelineIndex = null;

}
