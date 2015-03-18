package tests;

import java.util.ArrayList;
import java.util.List;

import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

import config.OpenNebulaConfigurationManager;
import client.OpenNebulaClient;
import os.ServerOperations;
import exceptions.ServiceCenterAccessException;
import models.DatacenterON;
import models.Puppeteer;
import models.ServerModel;
import models.ServerModelON;
import models.TemplateModel;
import models.TemplateModelON;
import models.VMModel;
import models.VMModelON;
import services.ServerService;
import services.ServerServiceImpl;
import services.VMService;
import services.VMServiceImpl;
import tcp.TCPServer;

public class Test1 {
	private ServerService service = new ServerServiceImpl();
	private VMService vmService = new VMServiceImpl();
	
	public void getVMByID(int id) {
		try {
			VMModel vm = vmService.getById(id);
			System.out.println(vm.getId());
		} catch (ServiceCenterAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public void getAllS() {

		List<ServerModel> servers = new ArrayList<ServerModel>();

		try {
			servers = service.getAll();
			System.out.println(servers.size());
		} catch (ServiceCenterAccessException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void getSById(int id) {
		try {
			ServerModel server = service.getById(id);
			System.out.println(server.getName());
			System.out.println(server.getId());
			System.out.println(service.contains(server));
		} catch (ServiceCenterAccessException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void getAll() {
		try {
			List<VMModel> vms = vmService.getAll();
			System.out.println(vms.size());
			for (VMModel model : vms) {
				System.out.println(model.getId());
			}
			VMModel vm = vmService.getById(514);
			System.out.println(vm.getName());
			ServerModel server = service.getById(52);
			vmService.migrate(vm, server);
		} catch (ServiceCenterAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startServer(int serverId) {

		try {
			System.out.println("Starting server " + serverId);
			ServerModel server = service.getById(serverId);
			System.out.println(server.getName());
			ServerOperations.wakeUp(server);
		} catch (ServiceCenterAccessException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void enableServer(int serverId) {
		ServerModel server = null;
		try {
			server = service.getById(serverId);
		} catch (ServiceCenterAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.enable(server);
	}

	public void migrate(int vmId, int serverId) {
		ServerModel server = null;
		VMModel vm = null;
		try {
			server = service.getById(serverId);
			vm = vmService.getById(vmId);
			vmService.migrate(vm, server);
		} catch (ServiceCenterAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void contains(int vmId) {
		try {
			System.out.println(vmService.contains(vmService.getById(vmId)));
		} catch (ServiceCenterAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void intermigrate() {
		TCPServer server = new TCPServer();
		Thread t = new Thread(server);
		t.start();
		VirtualMachinePool virtualMachinePool = new VirtualMachinePool(
				OpenNebulaClient.getInstance());
		virtualMachinePool.info();
		VirtualMachine virtualMachine = virtualMachinePool.getById(527);
		String imagePath = OpenNebulaConfigurationManager.getIMAGE_PATH_LOCATION();
		Puppeteer puppeteer = new Puppeteer(OpenNebulaConfigurationManager.getDatastoreUsername(),
				                            OpenNebulaConfigurationManager.getDatastorePassword(), 
				                            imagePath,
				                            OpenNebulaConfigurationManager.getDatastoreIp(), 
				                            OpenNebulaConfigurationManager.getListeningPort());
		VMModel model = new VMModelON(Integer.parseInt(virtualMachine.getId()),
				virtualMachine.getName());
		DatacenterON dataCenter = new DatacenterON(puppeteer, null, 1);
		vmService.interCloudMigrate(model, dataCenter);
	}

}
