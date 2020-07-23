package gov.sam.status.tracker.pojo;

import java.util.ArrayList;
import java.util.List;

public class StatusTrackerResponse {

	private Entity entity = new Entity();
	private Exclusion exclusion = new Exclusion();
	private List<Contract> contracts = new ArrayList<Contract>();
	private List<ContractOpps> contractOpportunities = new ArrayList<ContractOpps>();

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Exclusion getExclusion() {
		return exclusion;
	}

	public void setExclusion(Exclusion exclusion) {
		this.exclusion = exclusion;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	public List<ContractOpps> getContractOpportunities() {
		return contractOpportunities;
	}

	public void setContractOpportunities(List<ContractOpps> contractOpportunities) {
		this.contractOpportunities = contractOpportunities;
	}
}