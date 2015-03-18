package services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import logger.CloudLogger;
import models.ServerModel;
import models.ServerModelON;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.Host;
import org.opennebula.client.host.HostPool;
import os.ServerOperations;
import parsers.OpenNebulaInfoXMLParser;
import responsehelper.ResponseHelper;
import util.ARPTableManager;
import util.Energy;
import util.ResponseMessage;
import client.OpenNebulaClient;
import config.GeneralConfigurationManager;
import exceptions.ServiceCenterAccessException;

/**
 * 
 * @author AM
 */
public class ServerServiceImpl extends ServerService {

	@Override
	public ResponseMessage enable(ServerModel server) {
		Client client = OpenNebulaClient.getInstance();
		HostPool hostPool = new HostPool(client);
		hostPool.info();
		Host host = hostPool.getById(server.getId());
		host.info();
		OneResponse oneResponse = host.enable();
		return ResponseHelper.createResponseMessage(oneResponse);
	}

	@Override
	public ResponseMessage disable(ServerModel server) {
		Client client = OpenNebulaClient.getInstance();
		HostPool hostPool = new HostPool(client);
		hostPool.info();
		Host host = hostPool.getById(server.getId());
		host.info();
		OneResponse oneResponse = host.disable();
		return ResponseHelper.createResponseMessage(oneResponse);
	}

	@Override
	public Energy getEnergyConsumption(ServerModel server) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerModel getById(int id) throws ServiceCenterAccessException {

		Host host = new Host(id, OpenNebulaClient.getInstance());		
		OneResponse response = host.info();
		if (response.isError()) {
			CloudLogger.getInstance().LogInfo(response.getErrorMessage());	
		}
		ServerModelON serverModelON = null;
		while (serverModelON == null) {
			try {
				response = host.info();
				serverModelON = OpenNebulaInfoXMLParser
						.parseServerInfo(response.getMessage());
				serverModelON.setMacAddress(ServerOperations.getServerMAC(host
						.getName()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new ServiceCenterAccessException(e.getMessage(),
						e.getCause());
			}
		}
		return serverModelON;
	}

	@Override
	public List<ServerModel> getAll() {

		List<ServerModel> serverInfos = new ArrayList<ServerModel>();
		Client client = OpenNebulaClient.getInstance();

		HostPool hostPool = new HostPool(client);
		hostPool.info();
		Iterator<Host> hostIterator = hostPool.iterator();
		String arpTableManagerLocation = GeneralConfigurationManager.getARPTableFileLocation();
		ARPTableManager arpTableManager = new ARPTableManager(arpTableManagerLocation);
		
		try {
			while (hostIterator.hasNext()) {
				Host host = hostIterator.next();
				try {
					OneResponse response = host.info();
					String data = response.getMessage();

					ServerModelON serverInfo = OpenNebulaInfoXMLParser
							.parseServerInfo(data);
					String hostname = serverInfo.getName();
					if (arpTableManager.hasMAC(hostname)) {
						serverInfo.setMacAddress(arpTableManager
								.getMAC(hostname));
					} else {
						String mac = ServerOperations.getServerMAC(hostname);
						arpTableManager.addMAC(hostname, mac);
						serverInfo.setMacAddress(mac);
					}

					// read CPU Frequency
					// try to read trough ssh the output of cpufreq-info -l :
					// gives CPU frequency hardware limits
					// if no reponse, read CPU freq from OpenNebula
					{
						try {

							String cmd = "/usr/bin/ssh -t -t Node1 cpufreq-info -l";

							// execute the wakeonlan command
							Process proc = Runtime.getRuntime().exec(cmd);
							OutputStream outputStream = proc.getOutputStream();
							InputStream stdin = proc.getInputStream();
							InputStreamReader isr = new InputStreamReader(stdin);

							// log the wakeonlan output
							BufferedReader br = new BufferedReader(isr);
							String line = br.readLine();
							if (line != null) {
								// ApplicationLoggingSystem.getInstance().LogInfo(line);
								String[] values = line.split(" ");
								String cpuFreq = values[values.length - 1];
								if (cpuFreq.matches("\\d+")) {
									serverInfo.setCpuFrequency(Float
											.parseFloat(cpuFreq));
								}
							}

							br.close();
							isr.close();
							stdin.close();
							outputStream.close();
							proc.getInputStream().close();
							proc.getOutputStream().close();
							proc.getErrorStream().close();
							proc.destroy();

						} catch (Exception ex) {
							CloudLogger.getInstance().LogInfo(
									ex.getMessage());
						}
					}

					serverInfos.add(serverInfo);
				} catch (Exception e) {
					CloudLogger
							.getInstance()
							.LogInfo(
									"Error monitoring "
											+ host.getId()
											+ ". Probably is offline. Because incomplete data about the server was recorded by OpenNebula, it will be IGNORED.");
				}
			}

		} catch (Exception e) {
			CloudLogger.getInstance().LogInfo(e.getMessage() + e.getCause());
		} finally {
			arpTableManager.writeArpTable();
		}
		return serverInfos;
	}

	@Override
	public ResponseMessage delete(ServerModel serverModel) {
		Client client = OpenNebulaClient.getInstance();
		HostPool hostPool = new HostPool(client);
		hostPool.info();
		Host host = hostPool.getById(serverModel.getId());
		host.info();
		OneResponse oneResponse = host.delete();
		return ResponseHelper.createResponseMessage(oneResponse);
	}

	@Override
	public boolean contains(ServerModel serverModel) {
		Client client = OpenNebulaClient.getInstance();
		HostPool hostPool = new HostPool(client);
		hostPool.info();
		return hostPool.getById(serverModel.getId()) != null;
	}

}