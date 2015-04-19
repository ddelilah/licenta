package app.energy;

import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;

public class MigrationEfficiency {

	public float computeMigrationEfficiency(){
		
		ServerDAOImpl serverDAO = new ServerDAOImpl();
		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();

		int numberOfReleasedNodes = serverDAO.getAllServersByState("off").size();
		int numberOfMigrations = vmDAO.getAllVMsByState("Running").size();
		
		return (float) numberOfReleasedNodes / numberOfMigrations * 100;
		
	}
}
