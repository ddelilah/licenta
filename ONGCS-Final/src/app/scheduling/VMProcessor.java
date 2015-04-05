package app.scheduling;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.model.*;

public class VMProcessor implements Comparator<VirtualMachine> {

	private List<VirtualMachine> vmList;
	
	public VMProcessor(List<VirtualMachine> vmList){
		this.vmList=vmList;
	}
	
	public VMProcessor() {}

	@Override
	public int compare(VirtualMachine vm1, VirtualMachine vm2) {
		return vm1.getVmMips() > vm2.getVmMips() ? -1 : vm1.getVmMips() == vm2.getVmMips() ? 0 : 1;
	}
	
	public List sortVMListDescending(){
		Collections.sort(vmList, new VMProcessor());
		return vmList;
	}
	
}
