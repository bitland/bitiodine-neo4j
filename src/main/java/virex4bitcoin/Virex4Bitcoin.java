package virex4bitcoin;

public interface Virex4Bitcoin {
	
	Long flow(String payer, String payee);
	Long flow(String payer, String payee, Long fromdate, Long todate);

	Long balance(String address);
	Long balance(String address, Long attime);
	
}
