package gov.sam.status.tracker.pojo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class Entity {

	private String ueiDUNS;
	private String legalBusinessName;
	private String cageCode;
	private String registartionStatus;
	private String expirationDate;
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<String> naicsList = new ArrayList<String>();

	public String getUeiDUNS() {
		return ueiDUNS;
	}

	public void setUeiDUNS(String ueiDUNS) {
		this.ueiDUNS = ueiDUNS;
	}

	public String getLegalBusinessName() {
		return legalBusinessName;
	}

	public void setLegalBusinessName(String legalBusinessName) {
		this.legalBusinessName = legalBusinessName;
	}

	public String getCageCode() {
		return cageCode;
	}

	public void setCageCode(String cageCode) {
		this.cageCode = cageCode;
	}

	public String getRegistartionStatus() {
		return registartionStatus;
	}

	public void setRegistartionStatus(String registartionStatus) {
		this.registartionStatus = registartionStatus;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public List<String> getNaicsList() {
		return naicsList;
	}

	public void setNaicsList(List<String> naicsList) {
		this.naicsList = naicsList;
	}
}