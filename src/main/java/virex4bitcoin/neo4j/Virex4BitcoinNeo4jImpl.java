package virex4bitcoin.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

import virex4bitcoin.Virex4Bitcoin;
import virex4bitcoin.neo4j.service.VirexBitcoinLocalServiceUtil;

public class Virex4BitcoinNeo4jImpl implements Virex4Bitcoin {

	private GraphDatabaseService graphDb = null;
	
	public Virex4BitcoinNeo4jImpl(GraphDatabaseService graphDb) {
		super();
		this.graphDb = graphDb;
	}

	@Override
	public Long flow(String payer, String payee) {
		return VirexBitcoinLocalServiceUtil.flow(graphDb, payer, payee);
	}

	@Override
	public Long flow(String payer, String payee, Long fromdate, Long todate) {
		return VirexBitcoinLocalServiceUtil.flow(graphDb, payer, payee, fromdate, todate);
	}

	@Override
	public Long balance(String address) {
		return VirexBitcoinLocalServiceUtil.balance(graphDb, address);
	}

	@Override
	public Long balance(String address, Long attime) {
		return VirexBitcoinLocalServiceUtil.balance(graphDb, address, attime);
	}

}
