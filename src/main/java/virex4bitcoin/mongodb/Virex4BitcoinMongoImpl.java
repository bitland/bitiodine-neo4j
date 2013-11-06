package virex4bitcoin.mongodb;

import virex4bitcoin.Virex4Bitcoin;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Virex4BitcoinMongoImpl implements Virex4Bitcoin{
	
	DBCollection transactions;
	
	public Virex4BitcoinMongoImpl(MongoClient mongoClient){
		this.transactions = mongoClient.getDB("virex4bitcoin").getCollection("transactions");
	}

	@Override
	public Long flow(String payer, String payee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long flow(String payer, String payee, Long fromdate, Long todate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long balance(String address) {
		return this.balance(address,System.currentTimeMillis()/1000);
	}

	@Override
	public Long balance(String address, Long attime) {
		long balance = 0;
		balance = transactions.count();
		return new Long(balance);
	}

}
