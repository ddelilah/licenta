package services;


import models.TemplateModel;
import util.ResponseMessage;

/**
 * Performs operations related to Virtual Machine Templates
 */
public abstract class TemplateService extends Service<TemplateModel>{
	
	
	
	/**
	 * Adds a specified template in the pool
	 * @param template = the template to be added in the pool
	 * @return response message from the cloud manager
	 */
	public abstract ResponseMessage allocate(TemplateModel template);	
	
	
}
