package app.policies;

import app.model.VirtualMachine;

public class VmPolicy{

	private VirtualMachine vm;
	
	public VmPolicy(VirtualMachine vm){
		this.vm=vm;
	}

	
	public boolean evaluatePolicy(){
		System.out.println(vm.getServer());
		return vm.getServer() == null;
	}	
}
