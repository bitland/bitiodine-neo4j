package virex4bitcoin.neo4j.model;

public class TxInOut {
	
	public static String getTxOutIdPropertyName(){
		return "txout_id";
	}
	
	public static String getTxInIdPropertyName(){
		return "txin_id";
	}
	
	public static String getTxOutPositionPropertyName(){
		return "txout_pos";
	}
	
	public static String getTxInPositionPropertyName(){
		return "txin_pos";
	}
	
	public static String getAmountPropertyName(){
		return "amount";
	}
	
	public static String getUniqueTxOutIndexName(){
		return "txouts";
	}
	
	public static String getUniqueTxInIndexName(){
		return "txins";
	}
}
