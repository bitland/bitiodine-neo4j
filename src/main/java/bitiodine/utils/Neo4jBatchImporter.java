package bitiodine.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.sqlite.SQLiteJDBCLoader;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.Block;
import bitiodine.domain.model.Transaction;
import bitiodine.domain.model.TxInOut;
import bitiodine.domain.model.neo4j.NodeType;
import bitiodine.domain.model.neo4j.RelType;

public class Neo4jBatchImporter {
	private final String NEO4J_PATH="blockchain/graph.db";
	private final String SQLITE_PATH="blockchain/blockchain_small.sqlite";
	
	private BatchInserter inserter =  null;
	
	private Map<Long,Long> blockNeo4jIds = null;
	private Map<Long,Long> transactionNeo4jIds = null;
	private Map<String,Long> addressNeo4jIds = null;
	
	private Connection sqliteConnection;
	private int limit=600000;

	public void start_import(){
		System.out.println("Import currently disabled for safety");
		/*
		long startTime = System.currentTimeMillis();
		System.out.println("Deleting old database...");
		deleteOldDatabase();
		System.out.println("Initiating databases...");
		initBatchInserter();
		initSqlite();
		System.out.println("Importing blocks...");
		importBlocks();
		System.out.println("Importing transactions...");
		importTransactions();
		System.out.println("Importing addresses...");
		importTxOuts();
		System.out.println("Importing txins...");
		importTxIns();
		System.out.println("Shutting down...");
		shutdown();
		System.out.println("Creating indexes...");
		createIndexes();
		
	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Graph successfully imported in "+elapsedTime+" millis :)");
	    */
	}
	
	private void importBlocks(){
		blockNeo4jIds = new HashMap<Long,Long>();
		
		String queryCount = "SELECT COUNT(*) AS COUNT FROM BLOCKS";
		String queryNoLimit ="SELECT * FROM BLOCKS";
		managePages(queryCount, queryNoLimit, new SqlToGraphTranslator(){
			@Override
			public void createFromResultSet(ResultSet rs) throws SQLException
		    {
		    	Map<String,Object> properties = MapUtil.map(
						Block.getIdPropertyName(), rs.getLong("block_id"), 
						Block.getHashPropertyName(), rs.getString("block_hash"),
						Block.getTimestampPropertyName(), rs.getLong("time")
						);
				long node = inserter.createNode( properties, NodeType.BLOCK);
				
				//Add node id to map
				blockNeo4jIds.put(rs.getLong("block_id"), node);

		    }

		});
	}
	
	private void importTransactions(){
		transactionNeo4jIds = new HashMap<Long,Long>();
		
		String queryCount = "SELECT COUNT(*) AS COUNT FROM TX";
		String queryNoLimit ="SELECT * FROM TX";
		managePages(queryCount, queryNoLimit, new SqlToGraphTranslator(){
		    public void createFromResultSet(ResultSet rs) throws SQLException
		    {	
		    	Map<String,Object> properties = MapUtil.map(
						Transaction.getIdPropertyName(), rs.getLong("tx_id"), 
						Transaction.getHashPropertyName(), rs.getString("tx_hash")
						);
				long node = inserter.createNode( properties, NodeType.TRANSACTION);
				inserter.createRelationship(node, blockNeo4jIds.get(rs.getLong("block_id")),
						RelType.BLOCK, null);
				
				// Add to map
				transactionNeo4jIds.put(rs.getLong("tx_id"), node);
		    }

		});
	}
	
	private void importTxOuts(){
		addressNeo4jIds = new HashMap<String,Long>();
		
		//String queryCount = "SELECT 61921025 AS COUNT";
		String queryCount = "SELECT COUNT(*) AS COUNT FROM TXOUT";
		String queryNoLimit ="SELECT * FROM TXOUT";
		managePages(queryCount, queryNoLimit, new SqlToGraphTranslator(){
		    public void createFromResultSet(ResultSet rs) throws SQLException
		    {	
		    	String address = rs.getString("address");
		    	long node = 0;
		    	if (addressNeo4jIds.containsKey(address))
		    		node = addressNeo4jIds.get(address);
		    	else {	
			    	Map<String,Object> properties = MapUtil.map(
							Address.getAddressPropertyName(), address 
							);
					node = inserter.createNode( properties, NodeType.ADDRESS);
					
					// Add address to map
					addressNeo4jIds.put(rs.getString("address"), node);
		    	}
				Map<String,Object> rproperties = MapUtil.map(
						TxInOut.getTxOutIdPropertyName(), rs.getLong("txout_id"),
						TxInOut.getAmountPropertyName(), rs.getLong("txout_value")
						);
				inserter.createRelationship( transactionNeo4jIds.get(rs.getLong("tx_id")), 
						node, RelType.TXOUT, rproperties);		
		    }

		});
	}
	
	private void importTxIns(){
		//String queryCount = "SELECT 54600900 AS COUNT";
		String queryCount = "SELECT COUNT(*) AS COUNT FROM TXIN";
		String queryNoLimit ="SELECT TXIN.txin_id, TXIN.tx_id, TXIN.txin_pos, TXOUT.address, "+
							"TXOUT.txout_value, TXOUT.tx_id AS tx_prev, TXOUT.txout_pos" +
							" FROM TXIN LEFT JOIN TXOUT ON TXIN.txout_id=TXOUT.txout_id";
		managePages(queryCount, queryNoLimit, new SqlToGraphTranslator(){
		    public void createFromResultSet(ResultSet rs) throws SQLException
		    {		    		
		    	Map<String,Object> r1properties = MapUtil.map(
		    			TxInOut.getTxInIdPropertyName(), rs.getLong("txin_id"),
		    			TxInOut.getAmountPropertyName(), rs.getLong("txout_value") 
						);
		    	inserter.createRelationship( addressNeo4jIds.get(rs.getString("address")),
		    			transactionNeo4jIds.get(rs.getLong("tx_id")), 
						RelType.TXIN, r1properties);
		    	
				Map<String,Object> r2properties = MapUtil.map(
						Address.getAddressPropertyName(), rs.getString("address"),
						TxInOut.getAmountPropertyName(), rs.getLong("txout_value"),
						TxInOut.getTxInPositionPropertyName(), rs.getInt("txin_pos"),
						TxInOut.getTxOutPositionPropertyName(), rs.getInt("txout_pos")
						);
				inserter.createRelationship( transactionNeo4jIds.get(rs.getLong("tx_prev")), 
						transactionNeo4jIds.get(rs.getLong("tx_id")),
						RelType.NEXTTX, r2properties);
		    }

		});
	}
	
	private void managePages(String queryCount, String queryNoLimit, SqlToGraphTranslator g){
		Statement stmt; ResultSet rs;
		int rowCount=0; int rowCounter=0; int offset=0;
		try {
			stmt = sqliteConnection.createStatement();
			rs = stmt.executeQuery(queryCount);
			rowCount = rs.getInt("COUNT");
			rs.close(); stmt.close();
		} catch (SQLException e) {
			manageException(e);
		};
		
		while (rowCounter<rowCount){
			try {
				stmt = sqliteConnection.createStatement();
				rs = stmt.executeQuery( queryNoLimit+" LIMIT "+limit+
						" OFFSET "+offset+";" );
				
				while ( rs.next() ) {
					g.createFromResultSet(rs);
		    		rowCounter++;
				}
				
				rs.close(); stmt.close();
	    		System.out.println("Total "+rowCount+" Added "+rowCounter+" - "+
	    				String.format("%1$,.0f", (double) rowCounter/rowCount*100)+ "%...");
			}
			catch (SQLException e){
				manageException(e);
			}
			offset+=limit;
		}	
		
	}
	
	private void deleteOldDatabase(){
		try {
			Process p = Runtime.getRuntime().exec("rm -rf "+NEO4J_PATH);
			p.waitFor();
		} catch (Exception e) {
			manageException(e);
		}

	}
	
	private void initSqlite(){
	    try {
	    	if (!SQLiteJDBCLoader.isNativeMode())
	    		throw new Exception("Please use a native SQLiteJDBC driver");
			Class.forName("org.sqlite.JDBC");
			sqliteConnection = DriverManager
					.getConnection("jdbc:sqlite:"+SQLITE_PATH);
			sqliteConnection.setAutoCommit(false);
		} catch (Exception e) {
			manageException(e);
		}
	}
	
	private void initBatchInserter(){
		Map<String,String> config = new HashMap<String,String>();
		config.put( "neostore.nodestore.db.mapped_memory", "100M" );
		config.put( "neostore.relationshipstore.db.mapped_memory", "512M");
		config.put( "neostore.propertystore.db.mapped_memory","50M");
		config.put( "neostore.propertystore.db.strings.mapped_memory","100M");
		config.put( "neostore.propertystore.db.arrays.mapped_memory","0M");
		inserter = BatchInserters.inserter( NEO4J_PATH, config );
	}
	
	private void createIndexes(){
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( NEO4J_PATH );
        //	private BatchInserterIndex blocks, blockTimeline, transactions, addresses;
		try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() )
        {
            Schema schema = graphDb.schema();
            schema.indexFor( NodeType.ADDRESS )
                    .on(Address.getAddressPropertyName())
                    .create();
            tx.success();
        }
        try ( org.neo4j.graphdb.Transaction tx = graphDb.beginTx() )
        {
        	Schema schema = graphDb.schema();
            schema.awaitIndexesOnline(1, TimeUnit.HOURS);
        }
        
        graphDb.shutdown();
	}
	
	private void shutdown(){
		try {
			sqliteConnection.close();
		} catch (SQLException e) {
			manageException(e);
		}
		
		inserter.shutdown();
	}
	
	private void manageException(Exception e){
		System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    System.exit(0);
	}
	
	public static void main(String[] args) {
		(new Neo4jBatchImporter()).start_import();
	}

}
