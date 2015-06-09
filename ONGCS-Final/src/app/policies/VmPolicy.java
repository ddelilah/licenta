package app.policies;

import app.constants.PolicyType;
import app.model.VirtualMachine;

public class VmPolicy extends Policy {

	private VirtualMachine vm;

	public VmPolicy(PolicyType policyType, boolean isViolated, VirtualMachine vm) {
		super(policyType, isViolated);
		this.vm = vm;
	}

	@Override
	public boolean evaluatePolicy() {
		return vm.getServer() == null;
	}

}
