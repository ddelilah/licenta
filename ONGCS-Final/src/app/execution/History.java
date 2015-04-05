package app.execution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import app.access.GenericDAO;
import app.access.ServerDAO;
import app.access.VirtualMachineDAO;
import app.access.impl.GenericDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.model.CPU;
import app.model.Server;
import app.model.VirtualMachine;

public class History {

public void History(){}
	
public void writeHistory(Map<VirtualMachine, Server> map, String fileName){
		
		Map<VirtualMachine, Server> toWrite = new HashMap<VirtualMachine, Server>();
		
		toWrite = History.deserialize(fileName);
		
		for (Map.Entry<VirtualMachine, Server> entry : map.entrySet())
		{
			toWrite.put(entry.getKey(), entry.getValue());
		}
		
		History.serialize(toWrite,fileName);
		
		
		Map<VirtualMachine, Server> toWrite2 = new HashMap<VirtualMachine, Server>();
		
		toWrite2 = History.deserialize(fileName);
		for (Map.Entry<VirtualMachine, Server> entry : toWrite2.entrySet()){
			System.out.println(
					"Virtual Machine: "+entry.getKey().toString()+ 
					" is allocated to server:  "+ entry.getValue().toString()
					);
		}
	}
	
public void writeToFile(Map<VirtualMachine, Server> map, String fileName){
	try {
		 
		String content = "";
		
		for (Map.Entry<VirtualMachine, Server> entry : map.entrySet()){
			System.out.println(entry.getKey().toString());
			content+=entry.getKey().toString();
			content+=" allocated to server with id: ";
			content+=entry.getValue().getServerId();
			content+="\n";
		}
		File file = new File(fileName);
		System.out.println("Content: "+content);
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();

		System.out.println("Done");

	} catch (IOException e) {
		e.printStackTrace();
	}
}

public String readFromFile(String filename) throws Exception{
	String data;
	BufferedReader br = new BufferedReader(new FileReader(filename));
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        data = sb.toString();
   //     System.out.println(data);
    } finally {
        br.close();
    }
	return data;
}

	public static void serialize(Map<VirtualMachine,Server> map, String filename){
	 
		   try{
	 
			FileOutputStream fout = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(map);
			oos.close();
			System.out.println("Done");
	 
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
	}
	
	public static Map<VirtualMachine,Server> deserialize(String filename){
			 
		 Map<VirtualMachine, Server> allocation;
		 
			   try{
		 
				   FileInputStream fin = new FileInputStream(filename);
				   ObjectInputStream ois = new ObjectInputStream(fin);
				   allocation= (Map) ois.readObject();
				   ois.close();
		 
				   return allocation;
		 
			   }catch(Exception ex){
				   ex.printStackTrace();
				   return null;
			   } 
		   
	}
	
	public static void main(String []args){
		Map<VirtualMachine,Server> map = new HashMap<VirtualMachine,Server>();
		
		VirtualMachine vm1 = new VirtualMachine();
		vm1.setName(" vm");
		VirtualMachine vm2 = new VirtualMachine();
		vm2.setName("new vm2");
		VirtualMachine vm3 = new VirtualMachine();
		vm3.setName("new vm3");
		Server s1 = new Server();
		Server s2 = new Server();

		CPU cpu = new CPU();
		Server s3 = new Server();
		VirtualMachineDAO vmDAO = new VirtualMachineDAOImpl();
		
		vm1 = vmDAO.getAllVMs().get(0);
		vm2 = vmDAO.getAllVMs().get(1);
		vm3 = vmDAO.getAllVMs().get(2);
		ServerDAO sDAO = new ServerDAOImpl();
		s1 = sDAO.getAllServers().get(0);
		s2 = sDAO.getAllServers().get(1);
		s3 = sDAO.getAllServers().get(2);
		map.put(vm1, s1);
		
		
		History h = new History();
	//	h.serialize(map,"history.ser");
		map.put(vm2, s1);
		map.put(vm3, s2);
	/*	h.writeHistory(map,"history.ser");
		
	//	History h = new History();
		h.serialize(null, "historyNUR.ser");
		h.serialize(null, "historyRBR.ser");
		h.writeHistory(map, "historyNUR.ser");*/
		h.writeToFile(map, "historyRBR.txt");
	}
}
