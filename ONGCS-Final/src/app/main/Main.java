package app.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.access.*;
import app.access.impl.*;
import app.constants.RackState;
import app.constants.ServerState;
import app.constants.VMState;
import app.execution.Execution;
import app.model.CPU;
import app.model.HDD;
import app.model.RAM;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.monitoring.Data;
import app.monitoring.Monitoring;

public class Main {

	final static Logger logger = LoggerFactory.getLogger(Main.class);

	
	public void startInitialization() throws Exception {
		
		//Ade
				ProcessBuilder builder = new ProcessBuilder(
		 	            "cmd.exe", "/c", " mysql -u root licenta < init_script.sql "
		 	            );
		
		//Delia
	/*	ProcessBuilder builder = new ProcessBuilder(
 	            "cmd.exe", "/c", " mysql --user=root --password=password licenta < init_script.sql "
 	            );
 	*/
	 	builder.redirectErrorStream(true);
	     Process p = builder.start();
	     BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	     String line;
	     while (true) {
	         line = r.readLine();
	         if (line == null) { break; }
	     }
	     
	}
	
	public void startMonitoring() throws Exception{
		
		VirtualMachineDAO vmDAO = new VirtualMachineDAOImpl();
		ServerDAO serverDAO = new ServerDAOImpl();
		String command;
		  Data data = new Data();
		  Monitoring monitoring = new Monitoring();
		  int length = data.getData().length();
		     String[] toParse = new String[length];
		     toParse = data.getData().split("\\r?\\n");
		     for(int i=0; i<toParse.length; i++)
		    	 System.out.println(toParse[i]);
		     for(int i=0; i<toParse.length; i++){
		    	 VirtualMachine vm = null;
		    	 Server server = null;
		    	 
		    	 if(((toParse[i].split(" "))[0]).equals("VM")){
		    		vm = vmDAO.getVirtualMachineById(Integer.parseInt(toParse[i].split(" ")[1]));
		    		command = toParse[i].split(" ")[2];
		    		monitoring.addToQueue(vm, command);
		    	 }
		    	 
		   /* 	 else if(((toParse[i].split(" "))[0]).equals("SERVER")){
			    		server = serverDAO.getServerById(Integer.parseInt(toParse[i].split(" ")[1]));
			    		command = toParse[i].split(" ")[2];
			    		monitoring.addToQueue(server, command);
			    	 }*/
		    	 
		     }
		     monitoring.startMonitoring();
	}
	
	public static void main(String[] args) throws Exception {
		
		Main main = new Main();
		
		main.startInitialization();  
		
		main.startMonitoring();
	  
		
		
		
		
		
		
		
		
	/*	GenericDAO gg = new GenericDAOImpl();
		GenericDAOImpl g = new GenericDAOImpl();
		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
		VirtualMachine vm = new VirtualMachine();
		vm = vmDAO.getVirtualMachineById(1);
		System.out.println("\n\n\n"+vm.toString());
		gg.createInstance(vm);
		vmDAO.deleteVirtualMachineById(1);
	//	VirtualMachineDAO vmDao= new VirtualMachineDAO();
/*
		ServerDAOImpl srv = new ServerDAOImpl();

		CPU cpu = new CPU();

		cpu.setCpu_utilization(88);
		cpu.setFrequency(88);
		cpu.setName("generic");
		cpu.setNr_cores(3);
		srv.createInstance(cpu);

		RAM ram = new RAM();
		ram.setName("ram2");
		ram.setCapacity(99);
		srv.createInstance(ram);

		HDD hdd = new HDD();
		hdd.setCapacity(120);
		hdd.setName("hdd2");
		srv.createInstance(hdd);

		Rack r = new Rack();
		Server s = new Server();

		// RACK
		r.setCapacity(45);
		r.setCoolingValue(23);
		r.setName("rack1");
		r.setPowerValue(543);
		r.setRackId(1);
		r.setServers(null);
		r.setState(RackState.OFF.getValue());
		r.setUtilization(20);

		srv.createInstance(r);

		// /SERVER
		s.setCoolingValue(56);
		s.setIdleEnergy(23);
		s.setName("server1");
		s.setUtilization(54);
		s.setPowerValue(22);
		s.setCpu(cpu);
		s.setRam(ram);
		s.setHdd(hdd);
		s.setState(ServerState.OFF.getValue());
		s.setCorrespondingVMs(null);
		s.setRack(r);

		srv.createInstance(s);

		// VM
		List<VirtualMachine> correspVms = new ArrayList<VirtualMachine>();
		for (int i = 0; i < 40; i++) {

			VirtualMachine vm = new VirtualMachine();
			vm.setCpu(cpu);
			vm.setHdd(hdd);
			vm.setRam(ram);
			vm.setName("vm" + i);
			vm.setPowerValue(34);
			vm.setState(VMState.SHUT_DOWN);
			vm.setServer(s);
			srv.createInstance(vm);

			correspVms.add(vm);
		}
		// now persists the family person relationship
		s.setCorrespondingVMs(correspVms);
		srv.createInstance(s);

		srv.deleteInstance(r);
		cpu.setName("updated");
		srv.updateInstance(cpu);

		srv.getAllServers();
		srv.getServerById(3);
		srv.deleteInstance(srv.getServerById(6));
*/
	}

}
