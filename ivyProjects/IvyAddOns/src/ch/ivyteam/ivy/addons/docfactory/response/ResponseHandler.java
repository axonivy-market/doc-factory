package ch.ivyteam.ivy.addons.docfactory.response;

import ch.ivyteam.ivy.addons.docfactory.BaseDocFactory;
import ch.ivyteam.ivy.addons.docfactory.DocumentTemplate;
import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;

/**
 * Used in the DocFactory for catching any document mail merge response (success, error)...
 * @see {@link BaseDocFactory#withResponseHandler(ResponseHandler)} or {@link DocumentTemplate#withResponseHandler(ResponseHandler)}
 *
 */
public interface ResponseHandler {
	
	/**
	 * Method for getting any response from the DocFactory in form of a {@link FileOperationMessage}
	 * @param fileOperationMessage
	 */
	void handleDocFactoryResponse(FileOperationMessage fileOperationMessage);
}
