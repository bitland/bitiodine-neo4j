package bitiodine.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqlToGraphTranslator {
	public void createFromResultSet(ResultSet rs) throws SQLException;
}
