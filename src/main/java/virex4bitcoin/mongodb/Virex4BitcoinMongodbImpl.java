package virex4bitcoin.mongodb;

import virex4bitcoin.Virex4Bitcoin;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Virex4BitcoinMongodbImpl implements Virex4Bitcoin{
	
	DBCollection transactions;
	
	public Virex4BitcoinMongodbImpl(MongoClient mongoClient){
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
		DBObject fields = new BasicDBObject();
		fields.put("participants.address", address);
		fields.put("time", new BasicDBObject("$lte",attime));
		DBObject match = new BasicDBObject("$match",fields);

		fields = new BasicDBObject();
		fields.put("_id", 0);
		fields.put("participants", 1);
		DBObject project = new BasicDBObject("$project",fields);
		
		DBObject unwind = new BasicDBObject("$unwind","$participants");
		
		fields = new BasicDBObject();
		fields.put("_id", "$participants.address");
		fields.put("balance", new BasicDBObject("$sum","$participants.value"));
		DBObject group = new BasicDBObject("$group",fields);
		
		fields = new BasicDBObject();
		fields.put("_id", address);
		DBObject match2 = new BasicDBObject("$match",fields);
		
		BasicDBList result = (BasicDBList) transactions
				.aggregate(match,project,unwind,group,match2)
				.getCommandResult()
				.get("result");
		
		if (!result.isEmpty())
			balance = (long) ((BasicDBObject) result.get(0)).getLong("balance");
		
		return new Long(balance); 
	}

}
