package bitiodine.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.UniqueFactory;
import org.neo4j.index.lucene.LuceneTimeline;
import org.neo4j.index.lucene.TimelineIndex;

import bitiodine.domain.model.Block;
import bitiodine.domain.model.Transaction;
import bitiodine.domain.model.neo4j.NodeTypes;
import bitiodine.domain.model.neo4j.RelTypes;

public class Neo4jImporter {
	static GraphDatabaseService graphDb;
    static Connection c = null;
	static UniqueFactory<Node> uniqueBlockFactory = null;
	static UniqueFactory<Node> uniqueTransactionFactory = null;
	static TimelineIndex<Node> blockTimelineIndex = null;
	static int limit=100000;
	
	public static void main(String[] args) {
		Process p;
		try {
			p = Runtime.getRuntime().exec("rm -rf blockchain/graph.db");
			p.waitFor();
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		System.out.println("Old database deleted successfully");
		
		long startTime = System.currentTimeMillis();
		
		// Initialize
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( "blockchain/graph.db");	
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() ) 
		{
			//Configure graphDb for blocks
			uniqueBlockFactory = new UniqueFactory.UniqueNodeFactory( graphDb, 
		    				Block.getUniqueIndexName()) 
			{
						@Override
						protected void initialize(Node created,Map<String, Object> properties) 
						{
							created.setProperty( Block.getIdPropertyName(), 
									properties.get( Block.getIdPropertyName() ) );	
						}
		    };
			blockTimelineIndex = new LuceneTimeline<Node>(graphDb, 
					graphDb.index().forNodes(Block.getTimelineIndexName()));
			
			//Configure graphDb for transactions
			uniqueTransactionFactory = new 
		    		UniqueFactory.UniqueNodeFactory( graphDb, 
		    				Transaction.getUniqueIndexName() ) 
			{
					@Override
					protected void initialize(Node created,
							Map<String, Object> properties) 
					{
						created.setProperty( Transaction.getIdPropertyName(), 
								properties.get( Transaction.getIdPropertyName() ) );	
					}
		    };
		    
		    //Connect to sqlite database
		    Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:blockchain/blockchain_small.sqlite");
			c.setAutoCommit(false);
		}
		catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		};
		
		System.out.println("Sqlite database opened successfully, importing blocks...");
		
		try{
			// Import blocks
			Statement stmt = c.createStatement();;
			ResultSet rs = stmt.executeQuery( "SELECT COUNT(*) AS COUNT FROM BLOCKS;" );
			int rowCount = rs.getInt("COUNT"); int rowCounter=0;
			rowCount = rs.getInt("COUNT");
			rs.close();
			stmt.close();
			
			int offset=0;
			while (offset<rowCount){
				try (org.neo4j.graphdb.Transaction tx = graphDb.beginTx()) {
					stmt = c.createStatement();
					rs = stmt.executeQuery( "SELECT * FROM BLOCKS LIMIT "+limit+
							" OFFSET "+offset+";" );
					while ( rs.next() ) {
						Node n = uniqueBlockFactory.getOrCreate( 
								Block.getIdPropertyName(), rs.getLong("block_id") );
			    		n.addLabel(NodeTypes.BLOCK);
			    		n.setProperty(Block.getHashPropertyName(), rs.getString("block_hash"));
			    		n.setProperty(Block.getTimestampPropertyName(), rs.getLong("time"));
			    		blockTimelineIndex.add(n, rs.getLong("time"));
			    		
			    		rowCounter++;
					}
					rs.close();
					stmt.close();
					tx.success();
		    		System.out.println("Total "+rowCount+" Added "+rowCounter+" - "+
		    				String.format("%1$,.0f", (double) rowCounter/rowCount*100)+ "%");
				}
				offset+=limit;
			}	
		}
		catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		
		System.out.println("Blocks imported, importing transactions...");
			
		try{
			// Import transactions
			Statement stmt = c.createStatement();;
			ResultSet rs = stmt.executeQuery( "SELECT COUNT(*) AS COUNT FROM TX;" );
			int rowCount = rs.getInt("COUNT"); int rowCounter=0;
			rs.close();
			stmt.close();
			
			int offset=0;
			while (offset<rowCount){
				try (org.neo4j.graphdb.Transaction tx = graphDb.beginTx()) {
					stmt = c.createStatement();
					rs = stmt.executeQuery( "SELECT * FROM TX LIMIT "+limit+
							" OFFSET "+offset+";" );
					while ( rs.next() ) {
						Node n = uniqueTransactionFactory.getOrCreate( 
								Transaction.getIdPropertyName(), rs.getLong("tx_id") );
			    		n.addLabel(NodeTypes.TRANSACTION);
			    		n.setProperty(Block.getHashPropertyName(), rs.getString("tx_hash"));
			    		n.createRelationshipTo(
			    				uniqueBlockFactory.getOrCreate(
			    						Block.getIdPropertyName(), rs.getLong("block_id")), 
			    				RelTypes.BLOCK);
			    		
			    		rowCounter++;
					}
					rs.close();
					stmt.close();
					tx.success();
		    		System.out.println("Total "+rowCount+" Added "+rowCounter+" - "+
		    				String.format("%1$,.0f", (double) rowCounter/rowCount*100)+ "%");
				}
				offset+=limit;
			}
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
		System.out.println("Shutting down...");
		
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		graphDb.shutdown();
		
		System.out.println("Operation done successfully");
	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime);
	}

}
