package app.execution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class History {

	
public void writeToFile(List<VirtualMachine> allVMs,int initialNumberOffServers, List<Server> allInitialServers,Map<VirtualMachine, Server> map, String fileName){
	try {
		 
		String[] vmName = {"vm1.tiny", "vm1.small", "vm1.medium", "vm1.large", "vm1.xlarge"};
		History h = new History();
		String content = h.readFromFile(fileName);
		
		content+="-------------------- EXPERIMENT --------------------"+ System.lineSeparator();
		content+="VMs to be deployed"+ System.lineSeparator();
		for(int i=1; i<=5; i++){
			int ct =0;
			for(VirtualMachine vm: allVMs){
				if(vm.getName().equals(vmName[i-1])){
					ct++;
				}
			}
			content += ct + " "+vmName[i-1]+System.lineSeparator();
		}
		content+= initialNumberOffServers + " off servers initially"+System.lineSeparator();
		
		content+="-------------------- Initial Servers --------------------"+ System.lineSeparator();
		for(Server server: allInitialServers)
			content+= server+System.lineSeparator();
		
		content+="-------------------- Allocation --------------------"+ System.lineSeparator();
		for (Map.Entry<VirtualMachine, Server> entry : map.entrySet()){
			content+=entry.getKey().toString();
			content+=" allocated to server with id: ";
			content+=entry.getValue().getServerId();
			content+=System.lineSeparator();
		}
		
		content+="-------------------- Servers After Allocation --------------------"+ System.lineSeparator();
		ServerDAOImpl serverDAO = new ServerDAOImpl();
		List<Server> serverListAfterAllocation = serverDAO.getAllServers();
		for(Server server: serverListAfterAllocation)
			content+= server+System.lineSeparator();
		
		content+="-------------------- Racks After Allocation --------------------"+ System.lineSeparator();
		RackDAOImpl rackDAO = new RackDAOImpl();
		List<Rack> rackListAfterAllocation = rackDAO.getAllRacks();
		for(Rack rack: rackListAfterAllocation)
			content+= rack+System.lineSeparator();
		
		content+=System.lineSeparator();
		
		File file = new File(fileName);
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
	} catch (Exception e) {
		// TODO Auto-generated catch block
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

}