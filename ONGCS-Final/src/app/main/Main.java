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
//				ProcessBuilder builder = new ProcessBuilder(
//		 	            "cmd.exe", "/c", " mysql -u root licenta < init_script.sql "
//		 	            );
		
		//Delia
		ProcessBuilder builder = new ProcessBuilder(
 	            "cmd.exe", "/c", " mysql --user=root --password=password licenta < init_script.sql "
 	            );
 	
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
		
		  Data data = new Data();
		  Monitoring monitoring = new Monitoring();
		  int length = data.getData().length();
		     String[] toParse = new String[length];
		     toParse = data.getData().split("\\r?\\n");

		     for(int i=0; i<toParse.length; i++){
		    	 if(((toParse[i].split(" "))[0]).equalsIgnoreCase("VM")){
		    		String[] task = toParse[i].split(" "); 
		    		try{
			    		VirtualMachine	vm = vmDAO.getVirtualMachineById(Integer.parseInt(task[1]));
			    		int numberOfInstances = Integer.parseInt(task[2]);
			    		String command = task[3];
			    		monitoring.addToQueue(vm,  numberOfInstances, command);
		    		}
		    		catch(NumberFormatException e){
		    			System.out.println("Bad format for "+ toParse[i]);
		    		}
		    		
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

	}

}
