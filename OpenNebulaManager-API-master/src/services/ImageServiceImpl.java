package services;

import intercloudmigration.models.Disk;
import intercloudmigration.models.ImageState;
import java.util.ArrayList;
import java.util.List;
import logger.CloudLogger;
import models.ImageModel;
import models.ImageModelON;
import models.TemplateModelON;
import responsehelper.ResponseHelper;
import util.ResponseMessage;
import exceptions.ServiceCenterAccessException;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.image.Image;
import org.opennebula.client.image.ImagePool;
import config.OpenNebulaConfigurationManager;
import client.OpenNebulaClient;

public class ImageServiceImpl extends ImageService {
	private Client client = OpenNebulaClient.getInstance();

	@Override
	public boolean contains(String imageName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResponseMessage allocate(String description, int dataStoreId) {
		OneResponse oneResponse = Image.allocate(client, description,
				dataStoreId);
		return ResponseHelper.createResponseMessage(oneResponse);
	}

	@Override
	public ImageModel getById(int id) throws ServiceCenterAccessException {
		

		ImagePool imagePool = new ImagePool(OpenNebulaClient.getInstance());
		imagePool.info();
		
		Image image = imagePool.getById(id);
		image.info();
		
		ImageModel imageModel = new ImageModelON(Integer.parseInt(image.getId()), image.getName());
		return imageModel;
		
	}

	@Override
	public List<ImageModel> getAll() throws ServiceCenterAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage delete(ImageModel t)
			throws ServiceCenterAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(ImageModel t) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public List<OneResponse> allocateImages(TemplateModelON tm) {
        List<OneResponse> oneResponses = new ArrayList<OneResponse>();
        for (Disk disk : tm.getDisks()) {
            intercloudmigration.models.Image image = disk.getImage();
            image.setDescription("this is a test");
            image.setImagePath(OpenNebulaConfigurationManager.getIMAGE_PATH_LOCATION() + image.getName());
            OneResponse r = Image.allocate(client, image.toString(), OpenNebulaConfigurationManager.getDataStoreId());
            
            if (r.isError()) {
                System.out.println("An error has occured " + r.getErrorMessage());
            } else {
                System.out.print(r.getMessage());
            }
            oneResponses.add(r);
        }
        
        ImagePool imagePool = new ImagePool(client);
        imagePool.info();
        
        for(OneResponse oneResponse:oneResponses){
            Image image = imagePool.getById(oneResponse.getIntMessage());
            image.info();
            while(!image.stateString().equals(ImageState.READY.getValue())){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                   CloudLogger.getInstance().LogInfo(ex.getMessage());
                }
                image.info();
            }
            
        }
        return oneResponses;
	}

}
