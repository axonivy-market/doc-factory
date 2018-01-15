package ch.ivyteam.ivy.addons.docfactory.aspose;

import ch.ivyteam.ivy.addons.docfactory.AsposeDocFactory;

import com.aspose.words.Document;

/**
 * A DocumentWorker can be injected in an AsposeDocFactory ({@link AsposeDocFactory#withDocumentWorker(DocumentWorker)})
 * for applying some custom logic on the com.aspose.words.Document generated
 * by the mail merge with a template. 
 * Please read the com.aspose.words.Document API from Aspose (https://www.aspose.com/products/words/java).<br>
 * No support will be granted for your custom logic.
 *
 */
public interface DocumentWorker {
	
	/**
	 * Will be called before the mail merge logic has been performed on the document
	 * @param document
	 */
	default void prepare(Document document) {
		
	}
	
	/**
	 * Will be called after the mail merge logic has been performed on the document
	 * @param document
	 */
	default void postCreate(Document document){
		
	}
	
}
