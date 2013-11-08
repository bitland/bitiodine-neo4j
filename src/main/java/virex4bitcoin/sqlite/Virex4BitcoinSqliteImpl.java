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
		String query = "SELECT sum(txout_value) AS balance "
				+ "FROM TXEXTENDED "
				+ "WHERE address=\""+address+"\" AND time<"+attime;
		try {
			Statement stmt = sqliteConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			balance = rs.getLong("balance");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		};
		return new Long(balance);
	}

}
