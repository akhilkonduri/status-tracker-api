package gov.sam.status.tracker.pojo;

public class Contract {

	private String dateSigned;
	private String actionObligation;
	private String contractName;
	private String principalNAICSCode;

	public String getDateSigned() {
		return dateSigned;
	}

	public void setDateSigned(String dateSigned) {
		this.dateSigned = dateSigned;
	}

	public String getActionObligation() {
		return actionObligation;
	}

	public void setActionObligation(String actionObligation) {
		this.actionObligation = actionObligation;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getPrincipalNAICSCode() {
		return principalNAICSCode;
	}

	public void setPrincipalNAICSCode(String principalNAICSCode) {
		this.principalNAICSCode = principalNAICSCode;
	}

}
