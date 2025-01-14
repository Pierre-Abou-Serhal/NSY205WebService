package bal.dtos;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="compte")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankAccount {
	@XmlElement
	private int code;
	@XmlAttribute
	private double solde;
	@XmlAttribute
	private Date creationDate;
	
	public BankAccount() {
		
	}
	
	public BankAccount(int code, double solde, Date dateCreation) {
		this.setCode(code);
		this.setSolde(solde);
		this.setCreationDate(dateCreation);
	}

	// Getter / Setters
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date dateCreation) {
		this.creationDate = dateCreation;
	}
	
	
}
