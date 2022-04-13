package ch.ivyteam.ivy.addons.docfactory.response;

import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;

/**
 * Used in the DocFactory for catching any document mail merge response (success, error)...
 * {@link ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#withResponseHandler(ResponseHandler)} or {@link ch.ivyteam.ivy.addons.docfactory.DocumentTemplate#withResponseHandler(ResponseHandler)}
 *
 */
public interface ResponseHandler {

	/**
	 * Method for getting any response from the DocFactory in form of a {@link FileOperationMessage}
	 * @param fileOperationMessage
	 */
	void handleDocFactoryResponse(FileOperationMessage fileOperationMessage);
}
