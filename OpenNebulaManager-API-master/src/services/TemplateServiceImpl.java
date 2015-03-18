package services;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.opennebula.client.OneResponse;
import org.opennebula.client.template.Template;
import org.opennebula.client.template.TemplatePool;
import org.opennebula.client.vnet.VirtualNetwork;
import org.opennebula.client.vnet.VirtualNetworkPool;

import client.OpenNebulaClient;

import models.TemplateModel;
import models.TemplateModelON;
import util.ResponseMessage;
import exceptions.ServiceCenterAccessException;

public class TemplateServiceImpl extends TemplateService{

	@Override
	public ResponseMessage allocate(TemplateModel template) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemplateModel getById(int id) throws ServiceCenterAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TemplateModel> getAll() throws ServiceCenterAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage delete(TemplateModel t)
			throws ServiceCenterAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(TemplateModel t) {
		// TODO Auto-generated method stub
		return false;
	}
	
	 public void allocateTemplate(TemplateModelON tm, List<OneResponse> oneResponses) {
	        tm.setName(UUID.randomUUID().toString());
	        for (int i = 0; i < oneResponses.size(); i++) {
	            int imageId = Integer.parseInt(oneResponses.get(i).getMessage());
	            tm.getDisks().get(i).getImage().setImageId(imageId);            
	        }
	        tm.getNic().setNetworkId(getFirstVirtualNetworkId());
	        
	        OneResponse templateResponse =
	                Template.allocate(OpenNebulaClient.getInstance(), tm.toString());
	        
	        if (!templateResponse.isError())
	        {
	            TemplatePool tp = new TemplatePool(OpenNebulaClient.getInstance());
	            System.out.println(
	                    "Template created with ID = " 
	                    + templateResponse.getIntMessage());
	            tp.info();
	           OneResponse instResponse = tp.getById(templateResponse.getIntMessage()).instantiate();
	            if(instResponse.isError()){
	                System.out.println(instResponse.getErrorMessage());
	                
	            }
	            else{
	                System.out.println(instResponse.getMessage());
	            }
	        }
	    }
	    
	    private int getFirstVirtualNetworkId() {
	        VirtualNetworkPool vnp = new VirtualNetworkPool(OpenNebulaClient.getInstance());
	        vnp.info();
	        Iterator<VirtualNetwork> iterator= vnp.iterator();
	        return Integer.parseInt(iterator.next().getId());
	    }

}
