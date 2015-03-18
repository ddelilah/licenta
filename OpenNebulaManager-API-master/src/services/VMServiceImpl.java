/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import client.OpenNebulaClient;
import intercloudmigration.helpers.InterCloudHelper;
import intercloudmigration.helpers.TemplateHelper;
import intercloudmigration.models.Disk;

import java.util.List;

import exceptions.ServiceCenterAccessException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import logger.CloudLogger;
import models.Datacenter;
import models.DatacenterON;
import models.ServerModel;
import models.TemplateModel;
import models.TemplateModelON;
import models.VMModel;
import models.VMModelON;
import models.VirtualNetworkON;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.template.Template;
import org.opennebula.client.template.TemplatePool;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.opennebula.client.vnet.VirtualNetwork;
import org.opennebula.client.vnet.VirtualNetworkPool;

import config.OpenNebulaConfigurationManager;

import parsers.OpenNebulaInfoXMLParser;
import parsers.OpennebulaUtils;
import responsehelper.ResponseHelper;

import util.ResponseMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class VMServiceImpl extends VMService {

	private Client client = OpenNebulaClient.getInstance();

	@Override
	public VMModelON create(TemplateModel template) {
		// VMModelON templateON = new VMModelON(template.getId(),
		// template.getName());
		// VirtualNetworkPool networkPool = new VirtualNetworkPool(client);
		// networkPool.info();
		// int networkId = Integer.parseInt(OpenNebulaConfigurationManager
		// .getVMNetworkID());
		// VirtualNetwork network = networkPool.getById(networkId);
		// network.info();
		// VirtualNetworkON virtualNetworkInfo = templateON
		// .getVirtualNetworkInfo();
		// String vmTemplate = templateON.getVmTemplateInfo();
		// vmTemplate = OpennebulaUtils.configureVMTemplate(
		// vmTemplate,
		// templateON.getName(),
		// templateON.getRequestedCPU() / 3000,
		// templateON.getRequestedMemory(),
		// OpenNebulaConfigurationManager.getVMNetworkID(),
		// virtualNetworkInfo.getIp(),
		// Integer.parseInt(virtualNetworkInfo.getIp().substring(
		// virtualNetworkInfo.getIp().lastIndexOf(".") + 1)),
		// virtualNetworkInfo.getVncPassword());
		//
		// // create virtual machine for OpenNebula
		// OneResponse createVirtualMachineResponse = VirtualMachine.allocate(
		// client, vmTemplate);
		//
		// // verify virtual machine creation result
		// if (createVirtualMachineResponse.isError()) {
		// // VirtualNetwork.delete(client, template.getNetworkInfo().getId());
		// // throw new
		// //
		// ServiceCenterAccessException(createVirtualMachineResponse.getErrorMessage());
		// } else {
		// template.setId(Integer.parseInt(createVirtualMachineResponse
		// .getMessage()));
		// }
		// VirtualMachine vm = new VirtualMachine(template.getId(), client);
		// vm.info();
		// while (!vm.lcmStateStr().toLowerCase().contains("boot")
		// && !vm.lcmStateStr().toLowerCase().contains("runn")) {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// // throw new ServiceCenterAccessException(e.getMessage(),
		// // e.getCause());
		// }
		// vm.info();
		//
		// }import org.opennebula.client.image.Image;
		return null;
	}

	@Override
	public List<VMModel> getPendingVirtualMachines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VMModel deploy(VMModel vm, ServerModel server) {
		VirtualMachine virtualMachine = new VirtualMachine(vm.getId(), client);

		// deploy the virtual machine onto the server having the OpenNebula ID =
		// physicalHost.getId()
		virtualMachine.deploy(server.getId());
		VMModel toBeReturned = null;
		try {
			toBeReturned = OpenNebulaInfoXMLParser
					.parseVirtualMachineInfo(virtualMachine.info().getMessage());
		} catch (Exception e) {
			CloudLogger.getInstance().LogInfo(e.getMessage());
		}
		return toBeReturned;

	}

	@Override
	public VMModel migrate(VMModel vm, ServerModel server) {

		VirtualMachine virtualMachine = new VirtualMachine(vm.getId(), client);

		OneResponse oneResponse = virtualMachine.migrate(server.getId());
		if (oneResponse.isError()) {
			CloudLogger.getInstance().LogInfo(oneResponse.getErrorMessage());
		} else {
			CloudLogger.getInstance().LogInfo(
					"Successfully migrated " + vm.getId() + " from "
							+ vm.getServerId() + " to " + server.getId());
		}
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		VMModel toBeReturned = null;
		try {
			toBeReturned = OpenNebulaInfoXMLParser
					.parseVirtualMachineInfo(virtualMachine.info().getMessage());
		} catch (Exception e) {
			CloudLogger.getInstance().LogInfo(e.getMessage());
		}
		return toBeReturned;
	}

	@Override
	public ResponseMessage start(VMModel vm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage stop(VMModel vm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VMModel getById(int id) throws ServiceCenterAccessException {
		VirtualMachine virtualMachine = new VirtualMachine(id, client);
		OneResponse response = virtualMachine.info();
		String data = response.getMessage();
		VMModelON vmModel = null;

		try {
			vmModel = OpenNebulaInfoXMLParser.parseVirtualMachineInfo(data);
		} catch (Exception e) {
			CloudLogger.getInstance().LogInfo(e.getMessage() + e.getCause());
		}
		return vmModel;
	}

	@Override
	public List<VMModel> getAll() throws ServiceCenterAccessException {
		List<VMModel> runningVMs = new ArrayList<VMModel>();
		VirtualMachinePool pool = new VirtualMachinePool(client);
		pool.info();
		Iterator<VirtualMachine> hostIterator = pool.iterator();
		try {
			while (hostIterator.hasNext()) {
				VirtualMachine virtualMachine = hostIterator.next();
				OneResponse response = virtualMachine.info();
				String data = response.getMessage();
				runningVMs.add(OpenNebulaInfoXMLParser
						.parseVirtualMachineInfo(data));
			}
		} catch (Exception e) {
			throw new ServiceCenterAccessException(e.toString(), e.getCause());
		}
		return runningVMs;
	}

	@Override
	public ResponseMessage delete(VMModel vm)
			throws ServiceCenterAccessException {
		VirtualMachine virtualMachine = new VirtualMachine(vm.getId(), client);
		OneResponse oneResponse = virtualMachine.delete();
		return ResponseHelper.createResponseMessage(oneResponse);
	}

	@Override
	public boolean contains(VMModel vm) {
		VirtualMachinePool vmp = new VirtualMachinePool(
				OpenNebulaClient.getInstance());
		vmp.info();

		return vmp.getById(vm.getId()) != null;
	}

	@Override
	public ResponseMessage interCloudMigrate(VMModel vm, Datacenter datacenter) {
		VirtualMachine virtualMachine = new VirtualMachine(vm.getId(), client);
		TemplateModelON tm = new TemplateHelper()
				.createTemplateModel(virtualMachine);
		CloudLogger.getInstance().LogInfo(tm.toString());
		InterCloudHelper interCloudHelper = new InterCloudHelper();
		List<String> imageNames = interCloudHelper.createImagesFromDisks(
				tm.getDisks(), virtualMachine);
		List<String> imagePaths = interCloudHelper.getImagesPaths(imageNames);

		interCloudHelper.destroyVirtualMachine(virtualMachine);
		interCloudHelper.sendImagesWithSSH(imagePaths, imageNames,
				(DatacenterON) datacenter);

		interCloudHelper.remoteRestore((DatacenterON) datacenter, tm,
				imageNames);
		return new ResponseMessage();
	}

	@Override
	public ResponseMessage allocateICVirtualMachine(TemplateModel templateModel) {

		TemplateModelON templateModelON = (TemplateModelON) templateModel;

		CloudLogger.getInstance().LogInfo(templateModelON.toString());

		ImageServiceImpl imageService = new ImageServiceImpl();
		TemplateServiceImpl templateService = new TemplateServiceImpl();

		List<OneResponse> oneResponses = imageService
				.allocateImages(templateModelON);

		templateService.allocateTemplate(templateModelON, oneResponses);
		
		return new ResponseMessage();
	}

}
