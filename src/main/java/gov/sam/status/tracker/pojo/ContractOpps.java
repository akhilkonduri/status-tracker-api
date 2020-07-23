package gov.sam.status.tracker.pojo;

public class ContractOpps {

	public ContractOpps() {
	}

	public ContractOpps(String possibleContractName) {
		this.possibleContractName = possibleContractName;
	}

	private String possibleContractName;

	public String getPossibleContractName() {
		return possibleContractName;
	}

	public void setPossibleContractName(String possibleContractName) {
		this.possibleContractName = possibleContractName;
	}
}
