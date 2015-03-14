package os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import config.GeneralConfigurationManager;
import exceptions.ServiceCenterAccessException;
import logger.CloudLogger;
import models.ServerModel;
import util.ARPTableManager;

public class ServerOperations {

	private static ARPTableManager arpTableManager;

	static {
		String location = GeneralConfigurationManager.getARPTableFileLocation();
		arpTableManager = new ARPTableManager(location);
	}

	/**
	 * Wakes up a host from the pool
	 * 
	 * @param server
	 *            = the host to be awaked
	 * @return response message from the cloud manager
	 */
	public static void wakeUp(ServerModel server) {
		try {
			System.out.println("waking up server " + server.getId());
			String cmd = GeneralConfigurationManager.getNodesWakeUpMechanism()
					+ " " + server.getMacAddress();

			// execute the wakeonlan command
			Process proc = Runtime.getRuntime().exec(cmd);
			OutputStream outputStream = proc.getOutputStream();
			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);

			// log the wakeonlan output
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			if (line != null) {
				CloudLogger.getInstance().LogInfo(line);
			}

			// wait until server responds to ping
			CloudLogger.getInstance().LogInfo(
					"Waiting for " + server.getName() + " to respond to ping");
			waitUntilTargetIsAlive(server.getName());

			// wait until server responds to SSH connection request
			// needed because SSH is used by OpenNebula to deploy/migrate
			// virtual machines
			CloudLogger.getInstance().LogInfo(
					"Waiting for " + server.getName() + " to respond to SSH");
			waitUntilSSHAvailable(server.getName());

			br.close();
			isr.close();
			stdin.close();
			outputStream.close();
			proc.getInputStream().close();
			proc.getOutputStream().close();
			proc.getErrorStream().close();
			proc.destroy();

		} catch (Exception ex) {
			CloudLogger.getInstance().LogInfo(ex.getMessage());
		}

	}

	/**
	 * Shuts down a host from the pool
	 * 
	 * @param server
	 *            = the host to be shut down
	 * @return response message from the cloud manager
	 */
	public static void shutDown(ServerModel server) {
		if (!checkIfAlive(server.getName())) {
			return;
		}

		try {
			// connect trough ssh to the target server IP = host.getHostname()
			// and issue a shutdown command
			String cmd = "/usr/bin/ssh -t -t " + server.getName()
					+ " sudo /sbin/shutdown -h now";
			Process proc = Runtime.getRuntime().exec(cmd);
			// wait until the server does not respond to ping anymore
			waitUntilTargetIsOff(server.getName());

			proc.getInputStream().close();
			proc.getOutputStream().close();
			proc.getErrorStream().close();
			proc.destroy();

		} catch (Exception ex) {
			CloudLogger.getInstance().LogInfo("Error:" + ex.getMessage());
			CloudLogger.getInstance().LogInfo(ex.toString());
		}
	}

	/**
	 * Checks if a certain server is started
	 * 
	 * @param server
	 *            = the server to be checked
	 * @return true if server is started, false otherwise
	 */
	public static boolean serverIsAlive(ServerModel server) {
		String pingCmd = GeneralConfigurationManager.getPingLocation() + " "
				+ server.getName();
		boolean isAlive = false;

		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(pingCmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String inputLine;
			CloudLogger.getInstance().LogInfo(
					"Checking if " + server.getName()
							+ " responds to ping (if it is alive)");
			inputLine = in.readLine();
			inputLine = in.readLine();
			if (inputLine.toLowerCase().contains("unreachable")) {
				isAlive = false;
			} else {
				isAlive = true;
			}
			p.getInputStream().close();
			p.getOutputStream().close();
			p.getErrorStream().close();
			p.destroy();
			in.close();

		} catch (IOException e) {
			CloudLogger.getInstance().LogInfo(e.getMessage());
		}

		return isAlive;

	}

	/**
	 * Retrieves MAC address of a server based on its ip
	 * 
	 * @param ip
	 *            = the ip of the server
	 * @return the MAC address of the server
	 * @throws ServiceCenterAccessException
	 */
	public static String getServerMAC(String ip) {
		String mac = null;

		if (arpTableManager.hasMAC(ip)) {
			mac = arpTableManager.getMAC(ip);
		} else {
			mac = getMAC(ip);
			arpTableManager.addMAC(ip, mac);
		}

		return mac;
	}

	private static String getMAC(String ip) {
		String mac = null;

		try {
			pingTarget(ip);
			String arpCmd = GeneralConfigurationManager.getArpLocation() + " "
					+ ip;
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(arpCmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			in.readLine();

			mac = in.readLine();

			int count = 0;
			int length = mac.length();
			while ((count < length) && (mac.charAt(count) != ' ')) {
				count++;
			}
			while ((count < length) && (mac.charAt(count) == ' ')) {
				count++;
			}
			while ((count < length) && (mac.charAt(count) != ' ')) {
				count++;
			}
			while ((count < length) && (mac.charAt(count) == ' ')) {
				count++;
			}
			int count_init = count;
			while ((count < length) && (mac.charAt(count) != ' ')) {
				count++;
			}
			String mac_final = mac.substring(count_init, count);

			mac = mac_final;

			in.close();
			p.getInputStream().close();
			p.getOutputStream().close();
			p.getErrorStream().close();
			p.destroy();
		} catch (Exception e) {
			CloudLogger.getInstance().LogInfo(e.getMessage() + e.getCause());
		}
		return mac;
	}

	private static void pingTarget(String ip)
			throws ServiceCenterAccessException {
		String pingCmd = GeneralConfigurationManager.getPingLocation() + " "
				+ ip;
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(pingCmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			in.readLine();

			in.close();
			p.getInputStream().close();
			p.getOutputStream().close();
			p.getErrorStream().close();
			p.destroy();
		} catch (Exception e) {
			throw new ServiceCenterAccessException(e.getMessage(), e.getCause());
		}
	}

	private static void waitUntilTargetIsAlive(String ip) {

		String pingCmd = GeneralConfigurationManager.getPingLocation() + " "
				+ ip;
		boolean ok = false;
		while (!ok) {
			try {
				Runtime r = Runtime.getRuntime();
				Process p = r.exec(pingCmd);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				String inputLine;
				CloudLogger.getInstance().LogInfo(
						"Waiting until " + ip + " responds to ping");
				while ((inputLine = in.readLine()) != null) {

					if (!inputLine.toLowerCase().contains("unreachable")
							&& !inputLine.toLowerCase().equals("")
							&& inputLine.toLowerCase().contains("64 bytes")) {
						ok = true;
						CloudLogger.getInstance().LogInfo(
								ip + " responded to ping");
						in.close();
						p.getInputStream().close();
						p.getOutputStream().close();
						p.getErrorStream().close();
						p.destroy();
						break;

					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						CloudLogger.getInstance().LogInfo(ex.getMessage());
					}

				}
				p.getInputStream().close();
				p.getOutputStream().close();
				p.getErrorStream().close();
				p.destroy();

			} catch (IOException e) {
				CloudLogger.getInstance().LogInfo(e.getMessage());
				;
			}
		}
	}

	private static void waitUntilSSHAvailable(String ip) {

		try {
			boolean sshUp = false;
			String cmd = "/usr/bin/ssh " + ip + " echo ssh_ok";
			while (!sshUp) {
				Process proc = Runtime.getRuntime().exec(cmd);

				InputStreamReader isr = new InputStreamReader(
						proc.getInputStream());
				BufferedReader br = new BufferedReader(isr);

				{
					String line = br.readLine();
					if (line != null) {

						if (!line.contains("ssh_ok")) {

							isr.close();
							br.close();
							proc.getInputStream().close();
							proc.getOutputStream().close();
							proc.getErrorStream().close();
							proc.destroy();
							Thread.sleep(10000);

						} else {
							CloudLogger.getInstance().LogInfo(
									ip + " has responded to SSH");
							sshUp = true;
							isr.close();
							br.close();
							proc.getInputStream().close();
							proc.getOutputStream().close();
							proc.getErrorStream().close();
							proc.destroy();
							break;

						}

					} else {
						proc.getInputStream().close();
						proc.getOutputStream().close();
						proc.getErrorStream().close();
						proc.destroy();
					}
				}
			}

		} catch (Exception ex) {
			CloudLogger.getInstance().LogInfo(ex.toString());
		}
	}

	private static boolean checkIfAlive(String ip) {
		String pingCmd = GeneralConfigurationManager.getPingLocation() + " "
				+ ip;
		boolean ok = false;

		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(pingCmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String inputLine;
			CloudLogger.getInstance()
					.LogInfo("Checking if " + ip + " is alive");
			inputLine = in.readLine();
			inputLine = in.readLine();
			if (inputLine != null) {

				if (inputLine.toLowerCase().contains("unreachable")
						|| inputLine.toLowerCase().equals("")
						&& !inputLine.toLowerCase().contains("64 bytes")) {
					CloudLogger.getInstance().LogInfo(
							ip + " is off. No turn off needed");
					in.close();
					p.getInputStream().close();
					p.getOutputStream().close();
					p.getErrorStream().close();
					p.destroy();
					ok = false;
				} else {
					p.getInputStream().close();
					p.getOutputStream().close();
					p.getErrorStream().close();
					p.destroy();
					ok = true;
				}

			} else {
				in.close();
				p.getInputStream().close();
				p.getOutputStream().close();
				p.getErrorStream().close();
				p.destroy();
				ok = false;
			}

		} catch (IOException e) {
			CloudLogger.getInstance().LogInfo(e.getMessage());
		}

		return ok;

	}

	private static void waitUntilTargetIsOff(String ip) {

		String pingCmd = GeneralConfigurationManager.getPingLocation() + " "
				+ ip;
		boolean ok = false;
		while (!ok) {
			try {
				Runtime r = Runtime.getRuntime();
				Process p = r.exec(pingCmd);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				String inputLine;
				CloudLogger.getInstance().LogInfo(
						"Waiting until " + ip
								+ " Does not respond to ping anymore");
				while ((inputLine = in.readLine()) != null) {

					if (inputLine.toLowerCase().contains("unreachable")
							|| inputLine.toLowerCase().equals("")
							&& !inputLine.toLowerCase().contains("64 bytes")) {
						CloudLogger.getInstance().LogInfo(
								ip + " is off. Not responding to ping anymore");
						ok = true;
						in.close();
						p.getInputStream().close();
						p.getOutputStream().close();
						p.getErrorStream().close();
						p.destroy();
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						CloudLogger.getInstance().LogInfo(ex.getMessage());
					}

				}
				p.getInputStream().close();
				p.getOutputStream().close();
				p.getErrorStream().close();
				p.destroy();

			} catch (IOException e) {
				CloudLogger.getInstance().LogInfo(e.getMessage());
			}
		}
	}
}
