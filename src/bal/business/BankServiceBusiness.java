package bal.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bal.dtos.BankAccount;
import dal.Dal;
import dal.QueryType;
import dal.BankAccountRowMapper;
import utils.ConfigManager;

public class BankServiceBusiness {
	
	ConfigManager config = ConfigManager.getInstance(); 
	Dal dal = new Dal(config.getDatabaseUrl(), config.getDatabaseUser(), config.getDatabasePassword());
	
	public double convertEurToLbp(double amount) {
		return amount * 11;
	}
	
	public BankAccount getBankAcccountByCode (int code) {
		BankAccount retVal = new BankAccount();
		
		String spCall = "{call dbo.usp_GetBankAccountByCode(?)}";

		Map<Integer, Object> params = new HashMap<>();
		params.put(1, code);

		try {
			retVal = dal.executeSqlQuerySingleRow(spCall, params, QueryType.SELECT, new BankAccountRowMapper());
		} catch (SQLException e) {
			retVal.setCode(-1);
		}
		
		return retVal;
	}
	
	public List<BankAccount> getAllBankAccounts(){
		List<BankAccount> retVal;
		
		String spCall = "{call dbo.usp_GetAllBankAccounts()}";

		Map<Integer, Object> params = null;

		try {
			retVal = dal.executeSqlQueryMultiRows(spCall, params, QueryType.SELECT, new BankAccountRowMapper());
		} catch (SQLException e) {
			retVal = new ArrayList<>();
		}
		
		return retVal;
	}
	
	public BankAccount updateBankAccount(BankAccount updatedBankAccount) {
		BankAccount retVal = new BankAccount();
		
		String spCall = "{call dbo.usp_UpdateBankAccount(?, ?, ?)}";

		Map<Integer, Object> params = new HashMap<>();
		params.put(1, updatedBankAccount.getCode());
		params.put(2, updatedBankAccount.getSolde());
		params.put(3, updatedBankAccount.getCreationDate());

		try {
			retVal = dal.executeSqlQuerySingleRow(spCall, params, QueryType.UPDATE, new BankAccountRowMapper());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			retVal.setCode(-1);
		}
		
		return retVal;
	}
	
}