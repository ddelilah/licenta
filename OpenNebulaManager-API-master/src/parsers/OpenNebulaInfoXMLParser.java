package parsers;

import models.ServerModelON;
import models.VMModelON;
import models.VirtualDiskON;
import models.VirtualNetworkON;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import config.GeneralConfigurationManager;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class OpenNebulaInfoXMLParser {

	public static ServerModelON parseServerInfo(String info) throws Exception {

		InputStream stream = new ByteArrayInputStream(info.getBytes());
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document document = builder.parse(stream);

		Node state = document.getElementsByTagName("STATE").item(0);
		Node name = document.getElementsByTagName("NAME").item(0);
		Node id = document.getElementsByTagName("ID").item(0);

		Node memUsage = document.getElementsByTagName("USED_MEM").item(0);
		Node cpuUsage = document.getElementsByTagName("USED_CPU").item(0);
		Node diskUsage = document.getElementsByTagName("USED_DISK").item(0);
		Node currentCPUSpeed = document.getElementsByTagName("CPUSPEED")
				.item(0);

		Node cpuMAX = document.getElementsByTagName("MAX_CPU").item(0);
		Node cpuMAXFrequency = document.getElementsByTagName("CPUSPEED")
				.item(0);
		Node memMAX = document.getElementsByTagName("MAX_MEM").item(0);
		Node diskMAX = document.getElementsByTagName("MAX_DISK").item(0);

		ServerModelON serverModel = new ServerModelON();

		serverModel.setId(Integer.parseInt(id.getTextContent()));

		serverModel.setName(name.getTextContent());

		serverModel.setState(Integer.parseInt(state.getTextContent()));

		serverModel.setCpuFrequency(Float.parseFloat(currentCPUSpeed
				.getTextContent()));

		serverModel
				.setTotalCpu((Integer.parseInt(cpuMAX.getTextContent()) / 100)
						* Integer.parseInt(cpuMAXFrequency.getTextContent()));
		serverModel.setUsedCpu(Integer.parseInt(cpuUsage.getTextContent()));

		serverModel.setTotalMem(Integer.parseInt(memMAX.getTextContent()));
		serverModel
				.setUsedMem(Integer.parseInt(memUsage.getTextContent()) / 1000);

		serverModel.setTotalDisk(Integer.parseInt(diskMAX.getTextContent()));
		serverModel.setUsedDisk(Integer.parseInt(diskUsage.getTextContent()));

		return serverModel;
	}

	public static VMModelON parseVirtualMachineInfo(String vmInfo)
			throws Exception {
		InputStream stream = new ByteArrayInputStream(vmInfo.getBytes());
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document document = builder.parse(stream);

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		XPathExpression idExpr = xpath.compile("//ID");
		XPathExpression stateExpr = xpath.compile("//STATE");
		XPathExpression nameExpr = xpath.compile("//NAME");
		XPathExpression memoryExpr = xpath.compile("//TEMPLATE/MEMORY");
		XPathExpression cpuExpr = xpath.compile("//TEMPLATE/CPU");

		XPathExpression networkExpr = xpath.compile("//TEMPLATE/NIC");
		XPathExpression networkExprIP = xpath.compile("//IP");
		XPathExpression networkExprMAC = xpath.compile("//MAC");
		XPathExpression networkExprNetworkID = xpath.compile("//NETWORK_ID");

		XPathExpression diskExpr = xpath.compile("//TEMPLATE/DISK");
		XPathExpression diskSizeExpr = xpath.compile("//SIZE");
		XPathExpression diskTypeExpr = xpath.compile("//TYPE");
		XPathExpression diskTargetExpr = xpath.compile("//TARGET");
		XPathExpression diskFormatExpr = xpath.compile("//FORMAT");

		XPathExpression hostnameExpr = xpath.compile("//HOSTNAME");
		XPathExpression cpuFreqRequirements = xpath.compile("//REQUIREMENTS");

		String id = idExpr.evaluate(new InputSource(new StringReader(vmInfo)));
		String state = stateExpr.evaluate(new InputSource(new StringReader(
				vmInfo)));
		String name = nameExpr.evaluate(new InputSource(
				new StringReader(vmInfo)));
		String requestedCPU = cpuExpr.evaluate(new InputSource(
				new StringReader(vmInfo)));
		String requestedMEM = memoryExpr.evaluate(new InputSource(
				new StringReader(vmInfo)));
		String requiredCPUFreq = cpuFreqRequirements.evaluate(new InputSource(
				new StringReader(vmInfo)));
		String hostname = hostnameExpr.evaluate(new InputSource(
				new StringReader(vmInfo)));

		if (requiredCPUFreq.contains("=")) {
			requiredCPUFreq = requiredCPUFreq.split("=")[1].trim();
		} // last options maintained for backward compatibility with regular VM
			// specification
		else if (requiredCPUFreq.contains(">")) {
			requiredCPUFreq = requiredCPUFreq.split(">")[1].trim();
		} else if (requiredCPUFreq.contains("<")) {
			requiredCPUFreq = requiredCPUFreq.split("<")[1].trim();
		}

		Object disksInfo = diskExpr.evaluate(new InputSource(new StringReader(
				vmInfo)), XPathConstants.NODESET);
		NodeList nodes = (NodeList) disksInfo;
		List<VirtualDiskON> virtualDisks = new ArrayList<VirtualDiskON>();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			VirtualDiskON virtualDiskInfo = new VirtualDiskON();
			// virtualDiskInfo.setSize(Integer.parseInt(diskSizeExpr.evaluate(node)));
			virtualDiskInfo.setType(diskTypeExpr.evaluate(node));
			virtualDiskInfo.setFormat(diskFormatExpr.evaluate(node));
			virtualDisks.add(virtualDiskInfo);
		}
		int vmId = Integer.parseInt(id);
		VMModelON virtualModel = new VMModelON(vmId, name);

		// vm template does not specify cpu freq
		if (requiredCPUFreq == null || requiredCPUFreq.equals("")) {
			requiredCPUFreq = "2.93";
		}

		virtualModel.setCpu(Float.parseFloat(requiredCPUFreq));
		virtualModel.setCores(Integer.parseInt(requestedCPU));
		virtualModel.setMemory(Integer.parseInt(requestedMEM));
		virtualModel.setVirtualDisks(virtualDisks);
		String stateLowerCase = state.toLowerCase();

		// when a VM is resubmitted the hostname remains. So it is not
		// redeployed.
		// 1 = PENDING, 7 = FAILED
		if (!stateLowerCase.contains("1") && !stateLowerCase.contains("7")) {
			virtualModel.setHostServerHostname(hostname);
		}

		Node node = nodes.item(0);
		VirtualNetworkON virtualNetworkInfo = new VirtualNetworkON();
		String ipContent = networkExprIP.evaluate(node);
		String macContent = networkExprMAC.evaluate(node);

		if (!ipContent.isEmpty()) {
			virtualNetworkInfo.setIp(ipContent);
		} else if (!macContent.isEmpty()) {
			String[] macDigits = macContent.split(":");
			String ip = "";
			for (int j = 2; j < macDigits.length; j++) {
				String digit = macDigits[j];
				ip += Integer.parseInt(digit, 16) + ".";
			}
			ip = ip.substring(0, ip.length() - 1);
			virtualNetworkInfo.setIp(ip);
		}
		virtualModel.setVirtualNetworkInfo(virtualNetworkInfo);
		return virtualModel;
	}
}
