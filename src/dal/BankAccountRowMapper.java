package dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import bal.dtos.BankAccount;

public class BankAccountRowMapper implements RowMapper<BankAccount> {

	@Override
	public BankAccount mapRow(ResultSet rs) throws SQLException {
		BankAccount account = new BankAccount();
		account.setCode(rs.getInt("Code"));
		account.setSolde(rs.getDouble("Solde"));
		account.setCreationDate(rs.getDate("CreationDate"));
        return account;
	}
}