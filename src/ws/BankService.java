package ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import bal.business.BankServiceBusiness;
import bal.dtos.BankAccount;

@WebService(serviceName="BankService")
public class BankService {
	
	BankServiceBusiness bsb = new BankServiceBusiness();
	
	@WebMethod(operationName="ConvertEurToLbp")
	public double convertEurToLbp(@WebParam double amount) {
		return bsb.convertEurToLbp(amount);
	}
	
	@WebMethod
	public BankAccount getBankAcccountByCode (@WebParam(name="code") int code) {
		return bsb.getBankAcccountByCode(code);
	}
	
	@WebMethod
	@WebResult(name = "compte")
	public List<BankAccount> getAllBankAccounts(){
		return bsb.getAllBankAccounts();
	}
	
	@WebMethod
	public BankAccount updateBankAccount (@WebParam(name="bankAccount") BankAccount bankAccount) {
		return bsb.updateBankAccount(bankAccount);
	}
	
}