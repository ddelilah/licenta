package tests;

import java.util.List;
import models.ServerModel;
import services.ServerService;
import services.ServerServiceImpl;
import exceptions.ServiceCenterAccessException;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	Test1 tests  =new Test1();
	//tests.getAllS();
	//tests.getSById(55);
	//tests.getAll();
	//tests.startServer(55);
	//tests.enableServer(55);
	//tests.migrate(514, 55);
	//tests.contains(590);
	//tests.intermigrate();
	tests.getVMByID(557);
	}

}


