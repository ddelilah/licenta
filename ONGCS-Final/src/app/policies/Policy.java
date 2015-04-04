package app.policies;

import app.constants.PolicyType;

public abstract class Policy {

	private PolicyType policyType;
	protected boolean isViolated;

	public PolicyType getPolicyType() {
		return policyType;
	}

	public void setPolicyType(PolicyType policyType) {
		this.policyType = policyType;
	}

	public boolean isViolated() {
		return isViolated;
	}

	public void setViolated(boolean isViolated) {
		this.isViolated = isViolated;
	}

	public Policy(PolicyType policyType, boolean isViolated) {
		super();
		this.policyType = policyType;
		this.isViolated = isViolated;
	}

	public abstract boolean evaluatePolicy();

}
