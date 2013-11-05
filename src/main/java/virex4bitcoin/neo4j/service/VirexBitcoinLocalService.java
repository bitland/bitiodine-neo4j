package virex4bitcoin.neo4j.service;


public interface VirexBitcoinLocalService {
	Long balance(String address);
	Long balance(String address, Long atdate);
	Long flow(String payer, String payee);
	Long flow(String payer, String payee, Long fromdate, Long todate);
}
