package bitiodine.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.UniqueFactory;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.index.lucene.LuceneTimeline;
import org.neo4j.index.lucene.TimelineIndex;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.Block;
import bitiodine.domain.model.Transaction;
import bitiodine.domain.model.TxInOut;
import bitiodine.domain.model.neo4j.NodeTypes;
import bitiodine.domain.model.neo4j.RelTypes;

public class Neo4jImporter {
	static GraphDatabaseService graphDb;
	static Schema schema = null;
    static Connection c = null;
	static UniqueFactory<Node> uniqueBlockFactory = null;
	static UniqueFactory<Node> uniqueTransactionFactory = null;
	static UniqueFactory<Node> uniqueAddressFactory = null;
	static TimelineIndex<Node> blockTimelineIndex = null;
	static int limit=200000;
	
	public static void main(String[] args) {
		try {
			Process p = Runtime.getRuntime().exec("rm -rf blockchain/graph.db");
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
			schema = graphDb.schema();
			//Configure graphDb for Blocks
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
			
			schema.indexFor(NodeTypes.BLOCK).on( Block.getHashPropertyName() )
                    .create();
			
			//Configure graphDb for Transactions
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
		    
		    schema.indexFor(NodeTypes.TRANSACTION)
		    		.on( Transaction.getHashPropertyName() )
		    		.create();
		    
		    // Configure graphDb for Addresses
		    uniqueAddressFactory = new 
		    		UniqueFactory.UniqueNodeFactory( graphDb, Address.getUniqueIndexName() ) {
						@Override
						protected void initialize(Node created,
								Map<String, Object> properties) {
							created.setProperty( Address.getAddressPropertyName(), 
									properties.get( Address.getAddressPropertyName() ) );	
						}
		    };
		    
		    // Configure graphDb for TxOuts
		    		
		    // Configure graphDb for TxIns
		    
		    //Connect to sqlite database
		    Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:blockchain/blockchain_small.sqlite");
			c.setAutoCommit(false);
		}
		catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		};
	    
		try{
			System.out.println("Databases configured successfully, importing blocks...");
			
			// ********** Import Blocks **********
			Statement stmt = c.createStatement();;
			ResultSet rs = stmt.executeQuery( "SELECT COUNT(*) AS COUNT FROM BLOCKS;" );
			int rowCount = rs.getInt("COUNT"); int rowCounter=0; int offset=0;
			
			rowCount = rs.getInt("COUNT");
			rs.close();
			stmt.close();
			while (rowCounter<rowCount){
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
		    				String.format("%1$,.0f", (double) rowCounter/rowCount*100)+ "%"+
		    				" - Writing to disk...");
				}
				offset+=limit;
			}	
		
			System.out.println("Blocks imported, importing transactions...");
			
			// ********** Import Transactions **********
			stmt = c.createStatement();;
			rs = stmt.executeQuery( "SELECT COUNT(*) AS COUNT FROM TX;" );
			rowCount = rs.getInt("COUNT"); rowCounter=0; offset=0;
			rs.close();
			stmt.close();
			while (rowCounter<rowCount){
				try (org.neo4j.graphdb.Transaction tx = graphDb.beginTx()) {
					stmt = c.createStatement();
					rs = stmt.executeQuery( "SELECT * FROM TX LIMIT "+limit+
							" OFFSET "+offset+";" );
					while ( rs.next() ) {
						Node n = uniqueTransactionFactory.getOrCreate( 
								Transaction.getIdPropertyName(), rs.getLong("tx_id") );
			    		n.addLabel(NodeTypes.TRANSACTION);
			    		n.setProperty(Transaction.getHashPropertyName(), rs.getString("tx_hash"));
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
		    				String.format("%1$,.0f", (double) rowCounter/rowCount*100)+ "%"+
		    				" - Writing to disk...");
				}
				offset+=limit;
			}
			
			System.out.println("Transactions imported, importing Addresses (and txouts)...");
			
			// ********** Import Addresses (and txouts) **********
			stmt = c.createStatement();;
			rs = stmt.executeQuery( "SELECT COUNT(*) AS COUNT FROM TXOUT;" );
			rowCount = rs.getInt("COUNT"); rowCounter=0; offset=0;
			rs.close();
			stmt.close();
			while (rowCounter<rowCount){
				try (org.neo4j.graphdb.Transaction tx = graphDb.beginTx()) {
					stmt = c.createStatement();
					rs = stmt.executeQuery( "SELECT * FROM TXOUT LIMIT "+limit+
							" OFFSET "+offset+";" );
					while ( rs.next() ) {
						Node n = uniqueAddressFactory.getOrCreate( 
								Address.getAddressPropertyName(), 
								rs.getLong("address"));
			    		n.addLabel(NodeTypes.ADDRESS);
			    		
			    		Node t = uniqueTransactionFactory.getOrCreate(
			    				Transaction.getIdPropertyName(),rs.getLong("tx_id"));
			    		
			    		Relationship r = t.createRelationshipTo(n, 
			    				RelTypes.TXOUT);
			    		r.setProperty(TxInOut.getTxOutIdPropertyName(), 
			    				rs.getLong("txout_id"));
			    		r.setProperty(TxInOut.getAmountPropertyName(), 
			    				rs.getInt("txout_value"));
			    		rowCounter++;
					}
					rs.close();
					stmt.close();
					tx.success();
		    		System.out.println("Total "+rowCount+" Added "+rowCounter+" - "+
		    				String.format("%1$,.0f", (double) rowCounter/rowCount*100)+ "%"+
		    				" - Writing to disk...");
				}
				offset+=limit;
			}
			
			System.out.println("Addresses imported, importing txins...");
			
			// ********** Import txins **********
			stmt = c.createStatement();
			rs = stmt.executeQuery( "SELECT COUNT(*) AS COUNT FROM TXIN;" );
			rowCount = rs.getInt("COUNT"); rowCounter=0; offset=0;
			rs.close();
			stmt.close();
			while (rowCounter<rowCount){
				try (org.neo4j.graphdb.Transaction tx = graphDb.beginTx()) {
					stmt = c.createStatement();
					rs = stmt.executeQuery( "SELECT "+
							"TXIN.txin_id, TXIN.tx_id, TXIN.txin_pos, TXOUT.address, "+
							"TXOUT.txout_value, TXOUT.tx_id AS tx_prev, TXOUT.txout_pos" +
							" FROM TXIN LEFT JOIN TXOUT ON TXIN.txout_id=TXOUT.txout_id LIMIT "+limit+
							" OFFSET "+offset+";" );
					
					while ( rs.next() ) {
						Node tprev = uniqueTransactionFactory.getOrCreate(
								Transaction.getIdPropertyName(), rs.getLong("tx_prev"));
			    		Node t = uniqueTransactionFactory.getOrCreate(
			    				Transaction.getIdPropertyName(), rs.getLong("tx_id"));
			    		Node address = uniqueAddressFactory.getOrCreate(
			    				Address.getIdPropertyName(), rs.getString("address"));
			    		Relationship r = address.createRelationshipTo(t, RelTypes.TXIN);
			    		r.setProperty(TxInOut.getAmountPropertyName(), 
			    				rs.getInt("txout_value"));
			    		
			    		Relationship r2 = tprev.createRelationshipTo(t, RelTypes.NEXTTX);
			    		r2.setProperty(Address.getAddressPropertyName(), 
			    				rs.getString("address"));
			    		r2.setProperty(TxInOut.getAmountPropertyName(), 
			    				rs.getInt("txout_value"));
			    		r2.setProperty(TxInOut.getTxInPositionPropertyName(),
			    				rs.getInt("txin_pos"));
			    		r2.setProperty(TxInOut.getTxOutPositionPropertyName(),
			    				rs.getInt("txout_pos"));
			    		rowCounter++;
					}
					rs.close();
					stmt.close();
					tx.success();
		    		System.out.println("Total "+rowCount+" Added "+rowCounter+" - "+
		    				String.format("%1$,.0f", (double) rowCounter/rowCount*100)+ "%"+
		    				" - Writing to disk...");
				}
				offset+=limit;
			}
			
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
		
		System.out.println("Txins imported, shutting down...");
		
		try (org.neo4j.graphdb.Transaction tx = graphDb.beginTx()){
			c.close();
		    //Wait for indexes
		    schema.awaitIndexesOnline(30, TimeUnit.SECONDS);
		    tx.success();    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		graphDb.shutdown();

		System.out.println("Operation done successfully :)");
	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime);
	}

}
