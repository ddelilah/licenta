/**
 * 
 */
package responsehelper;

import org.opennebula.client.OneResponse;

import util.ResponseMessage;
import util.ResponseType;

/**
 * @author oneadmin
 *
 */
public class ResponseHelper {

	public static ResponseMessage createResponseMessage(OneResponse oneResponse) {
		ResponseMessage responseMessage = new ResponseMessage();
		ResponseType responseType;
		if (oneResponse.isError()) {
        	responseType = ResponseType.ERROR;
        	responseType.setMessage(oneResponse.getErrorMessage());
        	responseMessage.setResponseType(responseType);
            return responseMessage;
        } else {
        	responseType = ResponseType.INFO;
        	responseType.setMessage(oneResponse.getMessage());
        	responseMessage.setResponseType(responseType);
        	return responseMessage;
        }
	}
}
