package virex4bitcoin.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import virex4bitcoin.Virex4Bitcoin;

public class Virex4BitcoinSqliteImpl implements Virex4Bitcoin {

	private Connection sqliteConnection = null;
	
	public Virex4BitcoinSqliteImpl(Connection sqliteConnection) {
		this.sqliteConnection = sqliteConnection;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long balance(String address, Long attime) {
		long balance = 0;
		String posquery = "SELECT sum(txout_value) AS POS "
				+ "FROM TXOUT JOIN TX ON TXOUT.tx_id=TX.tx_id JOIN BLOCKS ON TX.block_id=BLOCKS.block_id "
				+ "WHERE address=\""+address+"\" AND time<"+attime;
		String negquery = "SELECT ifnull(sum(-txout_value),0) AS NEG "
				+ "FROM TXIN LEFT JOIN TXOUT ON TXIN.txout_id=TXOUT.txout_id JOIN TX ON TXOUT.tx_id=TX.tx_id JOIN BLOCKS ON TX.block_id=BLOCKS.block_id "
				+ "WHERE address=\""+address+"\" AND time<"+attime;
		try {
			Statement stmt = sqliteConnection.createStatement();
			ResultSet rs = stmt.executeQuery(posquery);
			Long pos = rs.getLong("POS");
			rs = stmt.executeQuery(negquery);
			balance = pos - rs.getLong("NEG");
			stmt.close();
		} catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		};
		return new Long(balance);
	}

}
