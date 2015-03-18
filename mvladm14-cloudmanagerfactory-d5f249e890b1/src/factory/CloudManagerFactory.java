package factory;

import config.OpenNebulaConfigurationManager;
import models.Datacenter;
import models.DatacenterON;
import models.Puppeteer;
import enums.ServiceType;
import services.ImageServiceImpl;
import services.ServerServiceImpl;
import services.Service;
import services.VMService;
import services.VMServiceImpl;

public class CloudManagerFactory {

	public static Service getService(ServiceType serviceType) {
		Service service = null;
		service = getOpenNebulaServiceByServiceType(serviceType);
		return service;
	}

	public static Datacenter getDatacenter() {
		Datacenter datacenter = null;
		String imagePath = OpenNebulaConfigurationManager
				.getIMAGE_PATH_LOCATION();
		Puppeteer puppeteer = new Puppeteer(
				OpenNebulaConfigurationManager.getDatastoreUsername(),
				OpenNebulaConfigurationManager.getDatastorePassword(),
				imagePath, OpenNebulaConfigurationManager.getDatastoreIp(),
				OpenNebulaConfigurationManager.getListeningPort());
		datacenter = new DatacenterON(puppeteer, null, 1);
		return datacenter;
	}

	private static Service getOpenNebulaServiceByServiceType(
			ServiceType serviceType) {
		Service service = null;
		switch (serviceType) {
		case VM:
			service = new VMServiceImpl();
			break;
		case SERVER:
			service = new ServerServiceImpl();
			break;
		case IMAGE:
			service = new ImageServiceImpl();
			break;
		case RACK:
			//service = new RackServiceImpl();
			break;
		default:
			break;
		}
		return service;
	}

}
