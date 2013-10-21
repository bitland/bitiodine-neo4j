package bitiodine.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.index.lucene.unsafe.batchinsert.LuceneBatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndex;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.sqlite.SQLiteJDBCLoader;

import bitiodine.domain.model.Address;
import bitiodine.domain.model.Block;
import bitiodine.domain.model.Transaction;
import bitiodine.domain.model.TxInOut;
import bitiodine.domain.model.neo4j.NodeTypes;
import bitiodine.domain.model.neo4j.RelTypes;

public class Neo4jBatchImporter {
	private final String NEO4J_PATH="blockchain/graph.db";
	private final String SQLITE_PATH="blockchain/blockchain.sqlite";
	
	private BatchInserter inserter =  null;
	private BatchInserterIndexProvider indexProvider = null;
	private BatchInserterIndex blocks, blockTimeline, transactions, addresses;
	
	
	private Connection sqliteConnection;
	private int limit=300000;

	public void start_import(){
		
		long startTime = System.currentTimeMillis();
		
		deleteOldDatabase();
		System.out.println("Old database deleted successfully, initiating neo4jDb...");
		initBatchInserter();
		initIndexes();
		System.out.println("neo4jDb initiated, initiating sqliteDb...");
		initSqlite();
		System.out.println("sqliteDb configured successfully, importing blocks...");
		importBlocks();
		System.out.println("blocks imported, importing transactions...");
		importTransactions();
		System.out.println("transactions imported, importing addresses (and txouts)...");
		importTxOuts();
		System.out.println("addresses imported, importing txins...");
		importTxIns();
		System.out.println("everything imported successfully, shutting down...");
		shutdown();
		
	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("Graph successfully imported in "+elapsedTime+" millis :)");
	}
	
	private void shutdown(){
		try {
			sqliteConnection.close();
		} catch (SQLException e) {
			manageException(e);
		}
		
		indexProvider.shutdown();
		inserter.shutdown();
	}
	
	private void importBlocks(){
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
				long node = inserter.createNode( properties, NodeTypes.BLOCK);
				
				// Add to index
				blocks.add(node, properties);

		    }

			@Override
			public void flushIndex() {
				blocks.flush();
			}
		});
	}
	
	private void importTransactions(){
		String queryCount = "SELECT COUNT(*) AS COUNT FROM TX";
		String queryNoLimit ="SELECT * FROM TX";
		managePages(queryCount, queryNoLimit, new SqlToGraphTranslator(){
		    public void createFromResultSet(ResultSet rs) throws SQLException
		    {	
		    	Map<String,Object> properties = MapUtil.map(
						Transaction.getIdPropertyName(), rs.getLong("tx_id"), 
						Transaction.getHashPropertyName(), rs.getString("tx_hash")
						);
				long node = inserter.createNode( properties, NodeTypes.TRANSACTION);
				inserter.createRelationship(node, 
						blocks.get(Block.getIdPropertyName(),
								rs.getLong("block_id")).getSingle(), RelTypes.BLOCK, null);
				
				// Add to index
				transactions.add(node, properties);
				
		    }

			@Override
			public void flushIndex() {
				transactions.flush();
			}
		});
	}
	
	private void importTxOuts(){
		String queryCount = "SELECT COUNT(*) AS COUNT FROM TXOUT";
		String queryNoLimit ="SELECT * FROM TXOUT";
		managePages(queryCount, queryNoLimit, new SqlToGraphTranslator(){
		    public void createFromResultSet(ResultSet rs) throws SQLException
		    {	
		    	String address = rs.getString("address");
		    	Long node = null;
		    	Map<String,Object> properties = MapUtil.map(
						Address.getAddressPropertyName(), address 
						);
		    	node = addresses.get(Address.getAddressPropertyName(),address).getSingle();
		    	if (node==null){
		    		node = inserter.createNode( properties, NodeTypes.ADDRESS);
		    		// Add to index
		    		addresses.add(node, properties);
		    	}
		    	
				Map<String,Object> rproperties = MapUtil.map(
						TxInOut.getTxOutIdPropertyName(), rs.getLong("txout_id"),
						TxInOut.getAmountPropertyName(), rs.getInt("txout_value")
						);
				inserter.createRelationship(
						transactions.get(
								Transaction.getIdPropertyName(),rs.getLong("tx_id"))
								.getSingle(), node, RelTypes.TXOUT, rproperties);		
				
				addresses.flush();
		    }

			@Override
			public void flushIndex() {
			}
		});
	}
	
	private void importTxIns(){
		String queryCount = "SELECT COUNT(*) AS COUNT FROM TXIN";
		String queryNoLimit ="SELECT TXIN.txin_id, TXIN.tx_id, TXIN.txin_pos, TXOUT.address, "+
							"TXOUT.txout_value, TXOUT.tx_id AS tx_prev, TXOUT.txout_pos" +
							" FROM TXIN LEFT JOIN TXOUT ON TXIN.txout_id=TXOUT.txout_id";
		managePages(queryCount, queryNoLimit, new SqlToGraphTranslator(){
		    public void createFromResultSet(ResultSet rs) throws SQLException
		    {		    		
		    	Map<String,Object> r1properties = MapUtil.map(
		    			TxInOut.getAmountPropertyName(), rs.getInt("txout_value") 
						);
		    	inserter.createRelationship( 
		    			addresses.get(Address.getAddressPropertyName(), 
		    					rs.getString("address")).getSingle(),
		    			transactions.get(Transaction.getIdPropertyName(),
		    					rs.getLong("tx_id")).getSingle(), 
						RelTypes.TXIN, r1properties);
		    	
				Map<String,Object> r2properties = MapUtil.map(
						Address.getAddressPropertyName(), rs.getString("address"),
						TxInOut.getAmountPropertyName(), rs.getInt("txout_value"),
						TxInOut.getTxInPositionPropertyName(), rs.getInt("txin_pos"),
						TxInOut.getTxOutPositionPropertyName(), rs.getInt("txout_pos")
						);
				inserter.createRelationship( transactions.get(
							Transaction.getIdPropertyName(),rs.getLong("tx_prev")).getSingle(), 
						transactions.get(Transaction.getIdPropertyName(),
		    					rs.getLong("tx_id")).getSingle(),
						RelTypes.NEXTTX, r2properties);
		    }

			@Override
			public void flushIndex() {		
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
				g.flushIndex();
				
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
		//TODO tuning config
		config.put( "neostore.nodestore.db.mapped_memory", "512M" );
		config.put( "neostore.relationshipstore.db.mapped_memory", "1G");
		config.put( "neostore.propertystore.db.mapped_memory","50M");
		config.put( "neostore.propertystore.db.strings.mapped_memory","100M");
		config.put( "neostore.propertystore.db.arrays.mapped_memory","0M");
		inserter = BatchInserters.inserter( NEO4J_PATH, config );
	}
	
	private void initIndexes(){
		indexProvider =
		        new LuceneBatchInserterIndexProvider( inserter );
		
		blocks = indexProvider.nodeIndex(Block.getBlocksIndexName(),
				MapUtil.stringMap("type","exact"));
		blocks.setCacheCapacity(Block.getIdPropertyName(), 300000);
		//TODO block timeline
		
		transactions = indexProvider.nodeIndex(Transaction.getTransactionsIndexName(),
				MapUtil.stringMap("type","exact"));
		transactions.setCacheCapacity(Transaction.getIdPropertyName(), 30000000);
		
		addresses = indexProvider.nodeIndex(Address.getAddressesIndexName(),
				MapUtil.stringMap("type","exact"));
		addresses.setCacheCapacity(Address.getAddressPropertyName(), 50000000);
		
		
	}
	
	private void manageException(Exception e){
		System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    System.exit(0);
	}
	
	public static void main(String[] args) {
		(new Neo4jBatchImporter()).start_import();
	}

}
