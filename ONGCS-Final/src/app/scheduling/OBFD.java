package app.scheduling;

import java.util.ArrayList;
import java.util.List;

import app.model.*;

public class OBFD {

	private List<Server> serverList;
	private static final int UNDERUTILIZED = 20;
	/** Pmax = 500W */
	private static final int MAXIMUM_POWER = 500; 
	/** fraction of power consumption of an idle server */
	private static final int K = 70;
	/* suppose temperature is 25 degrees*/
	private static final float COP = (float) (0.0068*Math.pow(25,2) + 0.0008*25 + 0.458);
	
	public OBFD(List<Server> serverList){
		this.serverList= serverList;	
	}
	
	/** returns the most appropriate server, power consumption and cooling */
	public List<Object> findAppropriateServer(VirtualMachine vm){
		float minPower= Float.MAX_VALUE;
		float cooling = 0;
		float power = 0;
		float utilization;
		List<Server> allocatedServers= new ArrayList<Server>();
		List<Server> emptyServers = new ArrayList<Server>();
		List<Server> underutilizedServers= new ArrayList<Server>();
		
		for(Server server: serverList){
			/* if server is empty*/
			if(server.getCorrespondingVMs().isEmpty())
				emptyServers.add(server);
				
			else {
				/* underutilized server*/
				if(server.getUtilization() < UNDERUTILIZED)
					underutilizedServers.add(server);
				
				else {
						/**if server has enough resources*/
					if(enoughResources(server, vm)){
				//		float power = K*MAXIMUM_POWER + (1-K)*MAXIMUM_POWER* server.getUtilization();	
				//		float power = server.getCpu().getCpu_utilization()*MAXIMUM_POWER+server.getIdleEnergy();
						
						 utilization = computeUtilization(server);
						 power = server.getIdleEnergy()+(MAXIMUM_POWER - server.getIdleEnergy()) * utilization;
						
						if(power < minPower){
							allocatedServers.add(server);
							minPower = power;
							cooling = power / COP;				
						}
					}
				}
			}
		}	
			if(allocatedServers.isEmpty()){
				for(Server sUnderutilized: underutilizedServers){
					if(enoughResources(sUnderutilized, vm)){
							 utilization = computeUtilization(sUnderutilized);
							 power = sUnderutilized.getIdleEnergy()+(MAXIMUM_POWER - sUnderutilized.getIdleEnergy()) * utilization;
								if(power < minPower){
									allocatedServers.add(sUnderutilized);
									minPower = power;
									cooling = power / COP;				
								}
							}
				}
				
				if(allocatedServers.isEmpty()){
					for(Server sEmpty : emptyServers){
						if(enoughResources(sEmpty, vm)){
							 utilization = computeUtilization(sEmpty);
							 power = sEmpty.getIdleEnergy()+(MAXIMUM_POWER - sEmpty.getIdleEnergy()) * utilization;
								if(power < minPower){
									allocatedServers.add(sEmpty);
									minPower = power;
									cooling = power / COP;				
								}
							}
					}
				}
			}
			
			List<Object> toReturn =new ArrayList<Object>();
			
			if(!allocatedServers.isEmpty())
			{
				toReturn.add(allocatedServers);
				toReturn.add(power);
				toReturn.add(cooling);
			}
			return toReturn;
		
	}
	
	public boolean enoughResources(Server server, VirtualMachine vmToCheck){
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float totalNbCores =0;
		float totalCapacityRam=0;
		float totalCapacityHdd=0;
		
		for(VirtualMachine vm : vmList){
			totalNbCores += vm.getCpu().getNr_cores();
			totalCapacityRam += vm.getRam().getCapacity();
			totalCapacityHdd += vm.getHdd().getCapacity();
		}
		if( server.getCpu().getNr_cores() - totalNbCores > vmToCheck.getCpu().getNr_cores()
				&& server.getRam().getCapacity() - totalCapacityRam > vmToCheck.getRam().getCapacity()
				&& server.getHdd().getCapacity() -totalCapacityHdd > vmToCheck.getHdd().getCapacity() )
			return true;
		
		return false;
	}
	
	
public float computeUtilization(Server server){
	List<VirtualMachine> vmList = server.getCorrespondingVMs();
	float sum = 0;
	for(VirtualMachine vm: vmList){
	   sum += vm.getVmMips();
	}
	return sum / server.getServerMIPS();
	
}


}