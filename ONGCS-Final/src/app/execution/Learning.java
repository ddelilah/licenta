package app.execution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.access.impl.CpuDAOImpl;
import app.access.impl.GenericDAOImpl;
import app.access.impl.HddDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.RamDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.constants.VMState;
import app.model.CPU;
import app.model.HDD;
import app.model.RAM;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class Learning {

	public boolean learning(List<VirtualMachine> allVMsToBeDeployed,int initialNumberOffServers, List<Server> allInitialServers, String fileName) throws Exception{
		
		String allocation = "", servers="", racks ="", initialServers ="[";
		List<String> foundExperiment = new ArrayList<String>();
		List<VirtualMachine> vmList = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	    	 String line = br.readLine();
	         StringBuilder sb = new StringBuilder();
	        
	         int[] vmNumber = getVmNumber(allVMsToBeDeployed);
	         ServerDAOImpl serverDAO = new ServerDAOImpl();
	         
	         int maxNumberOfServers = serverDAO.getAllServers().size();
	        while (line != null) {
	        	sb.append(line);
	            sb.append(System.lineSeparator());
	        	if(line.equals("VMs to be deployed")){
	        		line = br.readLine();
	        		boolean sameNbVMs = true;
	        		int i=0;
	        		while(i < 5){
		        		String[] toParse = line.split(" ");
		        		if(Integer.parseInt(toParse[0]) != vmNumber[i]){
		        			sameNbVMs = false;
		        		}
	        		i++;
	        		line = br.readLine();
	        		}
	        		if(!sameNbVMs){
	        			while(! line.equals("-------------------- EXPERIMENT --------------------") && line!=null){
	        				line = br.readLine();
	        			}
	        		}
	        		else{
	        			System.out.println("same nb of vms");
	        			String[] toParse = line.split(" ");
	        			/*
	        			 * If the number of Unused servers = max number of existent servers and same number
	        			 * 
	        			 * */
	        			if(Integer.parseInt(toParse[0]) == initialNumberOffServers && maxNumberOfServers == Integer.parseInt(toParse[0])){
	        				System.out.println("Found allocation \n\n\n\n");
	        				while(!line.equals("-------------------- Allocation --------------------")){
	        					line = br.readLine();
	        					
	        				}
	        				line = br.readLine();
	        				while(!line.equals("-------------------- Servers After Allocation --------------------")){
	        					allocation += line;
	        					allocation += System.lineSeparator();
	        					line = br.readLine();
	        					}
	        				line = br.readLine();
	        				while(! line.equals("-------------------- Racks After Allocation --------------------")){
	        					servers += line;
	        					servers += System.lineSeparator();
	        					line = br.readLine();
	        				}
	        				line = br.readLine();
	        				while(line!=null  && line.split(" ")[0].equals("Rack")){
	        					racks += line;
	        					racks += System.lineSeparator();
	        					line = br.readLine();
	        				}
	        				
	        				
	        				foundExperiment.add(allocation);
	        				foundExperiment.add(servers);
	        				foundExperiment.add(racks);
	        				vmList=	getObjectList(foundExperiment,allVMsToBeDeployed);
	        				return true;
	        			}
	        			/*
	        			 * If the number of Unused servers = our initial config
	        			 * 
	        			 * */
	        			else if(Integer.parseInt(toParse[0]) == initialNumberOffServers){
	        				line = br.readLine();
	        				line = br.readLine();
	        				while(!line.equals("-------------------- Allocation --------------------")){
	        					initialServers += line;
	        					initialServers +=", ";
	        					line = br.readLine();
	        				}
	        				StringBuilder sbb = new StringBuilder(initialServers);
	        				sbb.deleteCharAt(initialServers.length()-1);
	        				sbb.deleteCharAt(initialServers.length()-2);
	        				initialServers = sbb.toString();
	        				initialServers +="]";
	        				if(initialServers.equals(allInitialServers.toString())){
	        					System.out.println("Found allocation  2 \n\n\n\n");
		        				while(!line.equals("-------------------- Allocation --------------------")){
		        					line = br.readLine();
		        					
		        				}
		        				line = br.readLine();
		        				while(!line.equals("-------------------- Servers After Allocation --------------------")){
		        					allocation += line;
		        					allocation += System.lineSeparator();
		        					line = br.readLine();
		        					}
		        				line = br.readLine();
		        				while(! line.equals("-------------------- Racks After Allocation --------------------")){
		        					servers += line;
		        					servers += System.lineSeparator();
		        					line = br.readLine();
		        				}
		        				line = br.readLine();
		        				while(line!=null  && line.split(" ")[0].equals("Rack")){
		        					racks += line;
		        					racks += System.lineSeparator();
		        					line = br.readLine();
		        				}
		        				foundExperiment.add(allocation);
		        				foundExperiment.add(servers);
		        				foundExperiment.add(racks);
		        				vmList=	getObjectList(foundExperiment,allVMsToBeDeployed);
		        				return true;
		        					        					
	        				}
	        			}
	        			}
	        		
	        	}
		            
		        line = br.readLine();
	        }
	       
	    } finally {
	        br.close();
	    }

	    return false;
		
	}
	
	public static int[] getVmNumber(List<VirtualMachine> allVMsToBeDeployed){
		String[] vmName = {"vm1.tiny", "vm1.small", "vm1.medium", "vm1.large", "vm1.xlarge"};
    	int [] vmNumber = {0,0,0,0,0};
        for(int i=1; i<=5; i++){
			int ct =0;
			for(VirtualMachine vm: allVMsToBeDeployed){
				if(vm.getName().equals(vmName[i-1])){
					ct++;
				}
			}
			vmNumber[i-1] = ct;
		}
        
        return vmNumber;
	}
	
	public static List<VirtualMachine> getObjectList(List<String> foundExperiment, List<VirtualMachine> allVMsToBeDeployed){
		
		String[] toParse = foundExperiment.get(0).split("VirtualMachine");
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
		List<VirtualMachine> vmList = new ArrayList<>();
		List<Server> serverList = new ArrayList<>();

		ServerDAOImpl serverDAO = new ServerDAOImpl();
		RackDAOImpl rackDAO = new RackDAOImpl();
		toParse = foundExperiment.get(2).split("Rack");
		
		for(int i=1; i<toParse.length; i++){
			System.out.println("Parsing through"+ toParse[i]);
			if(toParse[i].split("state=")[1].split(",")[0].equalsIgnoreCase("on") || Float.parseFloat(toParse[i].split("powerValue=")[1].split(",")[0]) != (float)0){
				Rack rack = rackDAO.getRackById(Integer.parseInt(toParse[i].split("rackId=")[1].split(",")[0]));
				rack.setState("on");
				rack.setCoolingValue(Float.parseFloat(toParse[i].split("coolingValue=")[1].split(",")[0]));
				rack.setPowerValue(Float.parseFloat(toParse[i].split("powerValue=")[1].split(",")[0]));
				rack.setUtilization(Float.parseFloat(toParse[i].split("utilization=")[1].split(",")[0]));
				genericDAO.updateInstance(rack);
				
				
				System.out.println("\n\n\nRAck to be updated is "+rack);
			}
		}
		
		toParse = foundExperiment.get(1).split("Server");
		for(int i=1; i<toParse.length; i++){
			Server server = serverDAO.getServerById(Integer.parseInt(toParse[i].split("serverId=")[1].split(",")[0]));
			if(toParse[i].split("state=")[1].split(",")[0].equalsIgnoreCase("on")){
				server.setState("on");
				server.setCoolingValue(Float.parseFloat(toParse[i].split("coolingValue=")[1].split(",")[0]));
				server.setPowerValue(Float.parseFloat(toParse[i].split("powerValue=")[1].split(",")[0]));
				server.setUtilization(Float.parseFloat(toParse[i].split("utilization=")[1].split(",")[0]));
				genericDAO.updateInstance(server);
			}
		}
		
		toParse = foundExperiment.get(0).split("VirtualMachine");
		serverList = serverDAO.getAllServers();
		
		System.out.println("\n\n\n\n\n\n\n\n\n\nServer list is"+ serverList);
		for(int i=1; i<toParse.length ; i++){
			
			System.out.println("\n\n\nIn file we have "+toParse[i]);

			int pos =-1;
			for(VirtualMachine vm: allVMsToBeDeployed){
					System.out.println("\n\n\nChecking vm "+ vm);
					pos ++;
					if(vm.getName().equals(toParse[i].split("name=")[1].split(",")[0]) && vm.getServer()==null ){
						System.out.println("\n\n\nVm name equal and server_id null");
						VirtualMachine virtualMachine = new VirtualMachine();
						virtualMachine = vmDAO.getVirtualMachineById(vm.getVmId());
						virtualMachine.setState(VMState.RUNNING.getValue());
						
						virtualMachine.setServer(serverList.get(Integer.parseInt(toParse[i].split("id: ")[1].split(System.lineSeparator())[0])-1));
						System.out.println("\n\n\nVm "+virtualMachine+"\ngets server_id" + serverList.get(Integer.parseInt(toParse[i].split("id: ")[1].split(System.lineSeparator())[0])-1));
						genericDAO.updateInstance(virtualMachine);
						
						allVMsToBeDeployed.set(pos, virtualMachine);
						pos=-1;
						break;
					}
				}
		}
		
		return vmList;
	}
	
	public static void main(String []args) throws Exception{
		
		Learning l = new Learning();
		//l.learning("historyRBR.txt");
		
	}
	
}
