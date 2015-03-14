package services;

import java.util.List;

import util.ResponseMessage;

import models.Datacenter;
import models.ServerModel;
import models.TemplateModel;
import models.VMModel;

/**
 * Performs operations related to virtual machines
 */
public abstract class VMService extends Service<VMModel>{	
	
	/**
	 * Create a new Virtual Machine
	 * @param template = the template describing the VM
	 * @return created Virtual Machine
	 */
	public abstract VMModel create(TemplateModel template);
				
	/**
	 * Retrieves all pending the virtual machines
	 * @return List of pending Virtual Machines
	 */
	public abstract List<VMModel> getPendingVirtualMachines();
			
	/**
	 * Deploys a Virtual Machine on the specified server
	 * @param vm = the virtual machine to be deployed
	 * @param server = the host where the vm will be deployed
	 * @return response message from the cloud manager
	 */
	public abstract VMModel deploy(VMModel vm, ServerModel server);
		
	/**
	 * Migrates a virtual machine to the specified server
	 * @param vm = the virtual machine to be migrated
	 * @param server = the host where the vm will be migrated
	 * @return VMModel = migrated vm
	 */
	public abstract VMModel migrate(VMModel vm, ServerModel server);
	
	/**
	 * Starts a Virtual Machine
	 * @param vm = the virtual machine to be started
	 * @return response message from the cloud manager
	 */
	public abstract ResponseMessage start(VMModel vm);
	
	/**
	 * Stops a Virtual Machine
	 * @param vm = the virtual machine to be stopped
	 * @return response message from the cloud manager
	 */
	public abstract ResponseMessage stop(VMModel vm);				
		
	/**
	 * Migrates a Virtual Machine to the specified data center
	 * @param vm = the VM to be migrated
	 * @param datacenter = the remote data center to migrate the VM to
	 * @return response message from the cloud manager
	 */
	public abstract ResponseMessage interCloudMigrate(VMModel vm, Datacenter datacenter);
	
	
	/**
	 * Allocates a virtual machine based on the specified template model
	 * @param templateModel = the template model based on which the VM will be created
	 * This is the template which is received from another data center when performing
	 * inter cloud migration
	 * @return response message from the cloud manager
	 */
	public abstract ResponseMessage allocateICVirtualMachine(TemplateModel templateModel);
}
