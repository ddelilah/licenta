package services;

import models.ServerModel;
import util.Energy;
import util.ResponseMessage;

/**
 * Performs operations related to physical servers
 */
public abstract  class ServerService extends Service<ServerModel> {	

	/**
	 * Enables a host from the pool
	 * @param server = the host to be enabled
	 * @return response message from the cloud manager
	 */
	public abstract ResponseMessage enable(ServerModel server);
	
	/**
	 * Disables a host from the pool
	 * @param server = the host to be disabled
	 * @return response message from the cloud manager
	 */
	public abstract ResponseMessage disable(ServerModel server);
	
	/**
	 * Retrieves the energy consumed by a server
	 * @param server = the host to be monitored
	 * @return energy consumed
	 */
	public abstract Energy getEnergyConsumption(ServerModel server);
}
