//package intercloudmigration.helpers;
//
//import intercloudmigration.models.Disk;
//import intercloudmigration.models.Image;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.logging.Logger;
//
//import models.DatacenterON;
//import models.TemplateModel;
//import models.VMModel;
//import models.VMModelON;
//
//import org.opennebula.client.Client;
//import org.opennebula.client.OneResponse;
//import org.opennebula.client.vm.VirtualMachine;
//
//import services.ImageService;
//import services.ImageServiceImpl;
//import services.VMService;
//import services.VMServiceImpl;
//import tcp.TCPClient;
//import util.ResponseMessage;
//import client.OpenNebulaClient;
//
//public class InterCloudHelper {
//	private Client client = OpenNebulaClient.getInstance();
//	private ImageService imageService = new ImageServiceImpl();
//	
//
//	public List<String> getImagesPaths(List<String> imageNames) {
//        List<String> imagesPaths = new ArrayList<>();
//        for (String name : imageNames) {
//            String imagePath = OneResponseParser.getImagePath(name);
//            imagesPaths.add(imagePath);
//            System.out.println(imagePath);
//        }
//        return imagesPaths;
//    }
//
//    public List<String> createImagesFromDisks(List<Disk> disks, VirtualMachine vm) {
//        List<String> imageNames = new ArrayList<>();
//        try {
//            for (Disk disk : disks) {
//                String imageName = UUID.randomUUID().toString();
//                System.out.println("Saving image: " + imageName);
//                disk.getImage().setName(imageName);
//                disk.getImage().setIsPublic(true);
//
//                OneResponse saveDiskRespone = vm.savedisk(disk.getDiskID(), imageName);
//                if (saveDiskRespone.isError()) {
//                    System.out.println("Error: " + saveDiskRespone.getErrorMessage());
//                } else {
//                    imageNames.add(imageName);
//                }
//                Thread.sleep(1000);
//            }
//            Thread.sleep(1000);
//        } catch (InterruptedException ex) {
//            
//        }
//        return imageNames;
//    }
//
//    public void destroyVirtualMachine(VirtualMachine virtualMachine) {
//        virtualMachine.shutdown(true);
//        virtualMachine.info();
//        System.out.println("VM state is: " + virtualMachine.status());
//
//        if (virtualMachine.status().equals("shut")) {
//            try {
//                waitForVMToBeDeleted(virtualMachine);
//                Thread.sleep(2000);
//            } catch (InterruptedException ex) {
//                
//            }
//        }
//    }
//
//    public void waitForVMToBeDeleted(VirtualMachine virtualMachine) {
//        VMService ivms = new VMServiceImpl();
//        System.out.println("Waiting for the machine to be shutdown...");
//        VMModel vmModel = new VMModelON(Integer.parseInt(virtualMachine.getId()),
//        		                        virtualMachine.getName());
//        while (ivms.contains(vmModel)) {
//            try {
//                Thread.sleep(4000);
//            } catch (InterruptedException ex) {
//                
//            }
//        }
//        System.out.println("The virtual machine has been shutdown");
//    }
//
//    public List<String> sendImagesWithSSH(
//            List<String> imagePaths,
//            List<String> imageNames,
//            DatacenterON dataCenter) {
//
//        List<String> output = new ArrayList<>();
//        for (int index = 0; index < imagePaths.size(); index++) {
//            StringBuilder command = new StringBuilder();
//            command.append("sshpass -p '");
//            command.append(dataCenter.getPuppeteer().getPassword());
//            command.append("'");
//            command.append(" scp ");
//            command.append("\"");
//            command.append(imagePaths.get(index));
//            command.append("\"");
//            command.append(" \"");
//            command.append(dataCenter.getPuppeteer().getImgPath());
//            command.append(imageNames.get(index));
//            command.append("\"");
//
//            System.out.println("Executing: " + command.toString());
//            executeCommandWithPassword(command.toString());
//        }
//        for (String out : output) {
//            System.out.println(out);
//        }
//
//        System.out.println("Images were sent to " + dataCenter.getPuppeteer().getImgPath());
//        return output;
//    }
//
//    private void executeCommandWithPassword(String command) {
//        String[] SHELL_COMMAND = {"/bin/sh", "-c", command};
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process process = runtime.exec(SHELL_COMMAND);
//            process.waitFor();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        System.out.println("done");
//    }
//
//    public void remoteRestore(DatacenterON dataCenter, TemplateModel tm, List<String> imageNames) {
//        new TCPClient().restore(dataCenter, tm, imageNames);
//    }
//
//    public ResponseMessage allocateReceivedImage(Image image, int datastoreId) {
//
//        return imageService.allocate(
//                image.toString(), datastoreId);
//
//    }
//
//}
