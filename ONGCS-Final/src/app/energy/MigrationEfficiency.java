package app.energy;

import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;

public class MigrationEfficiency {

	
	public float computeMigrationEfficiency(int numberOfReleasedNodes, int numberOfMigrations){
		
		return (float) numberOfReleasedNodes / numberOfMigrations * 100;
		
	}
	
	public float computeAllocationMigrationRatio(int successfulMigrations, int noVMsToBeMigrated) {
		
		return (float) successfulMigrations/noVMsToBeMigrated * 100;
		

	}
}
