package app.energy;

import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;

public class MigrationEfficiency {

	// TODO: randomly allocate VMs and then start RBR and compute migration efficiency
	public float computeMigrationEfficiency(){
		
		ServerDAOImpl serverDAO = new ServerDAOImpl();
		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();

		int numberOfReleasedNodes = serverDAO.getAllServersByState("off").size();
		int numberOfMigrations = vmDAO.getAllVMsByState("Running").size();
		
		return (float) numberOfReleasedNodes / numberOfMigrations * 100;
		
	}
	
	public float computeMigrationEfficiency2(int numberOfMigrations){
		
		ServerDAOImpl serverDAO = new ServerDAOImpl();
		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();

		int numberOfReleasedNodes = serverDAO.getAllServersByState("off").size();
		
		System.out.println("Migration Efficiency: \n Number of migrations is: " + numberOfMigrations+"\nNumber of released Servers is: "
				+numberOfReleasedNodes);
		return (float) numberOfReleasedNodes / numberOfMigrations * 100;
		
	}
}
